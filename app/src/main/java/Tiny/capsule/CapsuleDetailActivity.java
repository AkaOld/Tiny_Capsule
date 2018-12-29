package Tiny.capsule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Tiny.capsule.http.CapsuleContentsRequest;
import Tiny.capsule.http.CapsuleContentsResult;
import Tiny.capsule.model.CapsuleContent;
import Tiny.capsule.model.Global;
import Tiny.capsule.model.RealmUtil;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CapsuleDetailActivity extends AppCompatActivity {
    private int id;
    private String name;

    private ImageView settings;   //胶囊属性设置
    private ImageView back;       //返回上一层
    private Button add;           //发布新动态
    private Button share;         //分享至qq空间
    QQActivity qqActivity;        //实例化分享类
    private TextView title;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capsule_detail);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerview_cdetail);
        //设置线性布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Item装饰
        recyclerView.addItemDecoration(new MyDecoration());
        //设置Adapter

        id = getIntent().getIntExtra("id", 0);
        name = getIntent().getStringExtra("name");

        title = findViewById(R.id.toolbar2_title);
        title.setText(name);

        share = findViewById(R.id.button4);
//        share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                qqActivity.shareToQZone(v);
//            }
//        });

        back = findViewById(R.id.toolbar2_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Global.downloaded = false;
            }
        });

        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CapsuleDetailActivity.this,CapsuleSendActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        settings = findViewById(R.id.toolbar2_set);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CapsuleDetailActivity.this,CapsuleConfigActivity.class);
                intent.putExtra("title", name);
                startActivity(intent);
            }
        });

        capsuleContentsRequest(id);
    }

    /*
     *    分享至qq空间
     */
    public class QQActivity extends AppCompatActivity implements IUiListener {

        private Tencent mTencent;

        private String APP_ID = "101534454";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_capsule_detail_item);
            mTencent = Tencent.createInstance(APP_ID, getApplicationContext());
        }

        public void shareToQQ(View view) {

        }

        public void shareToQZone(View view) {
            Bundle params = new Bundle();
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            params.putString(QQShare.SHARE_TO_QQ_TITLE, "Hi,叶应是叶");
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "欢迎访问我的博客");
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://blog.csdn.net/new_one_object");
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://avatar.csdn.net/B/0/1/1_new_one_object.jpg");
            params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "HiTips");
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
            mTencent.shareToQQ(this, params, this);
        }

        @Override
        public void onComplete(Object o) {
            Toast.makeText(this, o.toString(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(this, uiError.errorMessage + "--" + uiError.errorCode + "---" + uiError.errorDetail, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (mTencent != null) {
                Tencent.onActivityResultData(requestCode, resultCode, data, this);
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 加载本地图片
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    class MyDecoration extends RecyclerView.ItemDecoration{
        //复写getItemOffsets方法
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0,0,0,getResources().getDimensionPixelOffset(R.dimen.dividerHeight));
        }
    }

    static class LinearAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder>{
        //item数据
        private String cName[];
        private String timeSendMsg[];
        private String msg[];
        private String number[];
        private String imgPath[];
        ArrayList<CapsuleContent> capsuleContents;


        //声明引用
        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private CapsuleFragment.LinearAdapter.OnItemClickListener mlistener;
        //创建一个构造函数
        public LinearAdapter(Context context, CapsuleFragment.LinearAdapter.OnItemClickListener listener, ArrayList<CapsuleContent> capsuleContents){
            //初始化item数据
            this.capsuleContents = capsuleContents;
            this.mContext=context;
            //利用LayoutInflater把控件所在的布局文件加载到当前类当中
            mLayoutInflater = LayoutInflater.from(context);
            //从外部传进来一个OnItemClickListener子类的变量
            this.mlistener=listener;
        }
        //此方法要返回一个ViewHolder
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType ==0){
                return new LinearViewHolder(mLayoutInflater.inflate(R.layout.activity_capsule_detail_item,parent,false));
            }else if (viewType == 1){
                return new LinearViewHolder1(mLayoutInflater.inflate(R.layout.activity_capsule_detail_item1,parent,false));
            }else {
                return new LinearViewHolder2(mLayoutInflater.inflate(R.layout.activity_capsule_detail_item2,parent,false));
            }
        }
        //通过holder设置TextView的内容
        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            RealmList<String> resources = capsuleContents.get(position).getResourceUrl();
            if (resources.size() == 0){
                ((LinearViewHolder)holder).textView1.setText(getDateToString(capsuleContents.get(position).getTimeStamp(),"yyyy-MM-dd HH:mm:ss"));
                ((LinearViewHolder)holder).textView2.setText(capsuleContents.get(position).getNickname());
                ((LinearViewHolder)holder).textView3.setText(capsuleContents.get(position).getContent());
                ((LinearViewHolder)holder).textView_map.setText(capsuleContents.get(position).getLocation());
                //FileUtil.startDownload(FileUtil.serverBaseUrl+"user/"+Global.currentUsername+"/test.jpg",FileUtil.userDir+Global.currentUsername+"/test.jpg");
                FileUtil.startDownload(FileUtil.getUserIconUrl(capsuleContents.get(position).getUsername()),FileUtil.getUserIconFilePath(capsuleContents.get(position).getUsername()));
                String path = FileUtil.getUserIconFilePath(capsuleContents.get(position).getUsername());
                Bitmap bitmap = getLoacalBitmap(path);
                ((LinearViewHolder)holder).imageView1 .setImageBitmap(bitmap); //设置Bitmap
            }else if (resources.get(0).equals("picture")){
                ((LinearViewHolder1)holder).textView1.setText(getDateToString(capsuleContents.get(position).getTimeStamp(),"yyyy-MM-dd HH:mm:ss"));
                ((LinearViewHolder1)holder).textView2.setText(capsuleContents.get(position).getNickname());
                ((LinearViewHolder1)holder).textView3.setText(capsuleContents.get(position).getContent());
                ((LinearViewHolder1)holder).textView_map.setText(capsuleContents.get(position).getLocation());
                if(resources.size() == 3){
                    String path = FileUtil.getContentPictureFilePath(capsuleContents.get(position).getCapsuleId(),capsuleContents.get(position).getTimeStamp(),3);
                    Bitmap bitmap111 = getLoacalBitmap(path);
                    ((LinearViewHolder1)holder).image_msg_3 .setImageBitmap(bitmap111);
                }
                if(resources.size() >= 2){
                    String path = FileUtil.getContentPictureFilePath(capsuleContents.get(position).getCapsuleId(),capsuleContents.get(position).getTimeStamp(),2);
                    Bitmap bitmap111 = getLoacalBitmap(path);
                    ((LinearViewHolder1)holder).image_msg_2 .setImageBitmap(bitmap111);
                }
                if(resources.size() >= 1){
                    String path = FileUtil.getContentPictureFilePath(capsuleContents.get(position).getCapsuleId(),capsuleContents.get(position).getTimeStamp(),1);
                    Bitmap bitmap111 = getLoacalBitmap(path);
                    ((LinearViewHolder1)holder).image_msg_1 .setImageBitmap(bitmap111);
                }
                if(resources.size() == 1){
                    ((LinearViewHolder1)holder).image_msg_2.setVisibility(View.INVISIBLE);
                    ((LinearViewHolder1)holder).image_msg_3.setVisibility(View.INVISIBLE);
                }if(resources.size() == 2){
                    ((LinearViewHolder1)holder).image_msg_3.setVisibility(View.INVISIBLE);
                }

                FileUtil.startDownload(FileUtil.getUserIconUrl(capsuleContents.get(position).getUsername()),FileUtil.getUserIconFilePath(capsuleContents.get(position).getUsername()));
                String path = FileUtil.getUserIconFilePath(capsuleContents.get(position).getUsername());
                Bitmap bitmap1 = getLoacalBitmap(path);
                ((LinearViewHolder1)holder).imageView1 .setImageBitmap(bitmap1);
            }else if(resources.get(0).equals("video")){
                ((LinearViewHolder2)holder).textView1.setText(getDateToString(capsuleContents.get(position).getTimeStamp(),"yyyy-MM-dd HH:mm:ss"));
                ((LinearViewHolder2)holder).textView2.setText(capsuleContents.get(position).getNickname());
                ((LinearViewHolder2)holder).textView3.setText(capsuleContents.get(position).getContent());
                ((LinearViewHolder2)holder).textView_map.setText(capsuleContents.get(position).getLocation());

                FileUtil.startDownload(FileUtil.getUserIconUrl(capsuleContents.get(position).getUsername()),FileUtil.getUserIconFilePath(capsuleContents.get(position).getUsername()));
                String path1 = FileUtil.getUserIconFilePath(capsuleContents.get(position).getUsername());
                Bitmap bitmap1 = getLoacalBitmap(path1);
                ((LinearViewHolder2)holder).imageView1 .setImageBitmap(bitmap1);

                String path = FileUtil.getContentVideoFilePath(capsuleContents.get(position).getCapsuleId(),capsuleContents.get(position).getTimeStamp(),1);
//                ((LinearViewHolder2)holder).videoView.setVideoPath("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
                ((LinearViewHolder2)holder).videoView.setVideoPath(path);
                ((LinearViewHolder2)holder).videoView.start();
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistener.OnClick(position);
                    //点击后弹窗，显示点击位置
                    //Toast.makeText(mContext,"点击位置"+position,Toast.LENGTH_SHORT).show();
                }
            });

        }

        //获取位置
        @Override
        public int getItemViewType(int position) {
            RealmList<String> resources = capsuleContents.get(position).getResourceUrl();
            if (resources.size() == 0){
                return 0;
            }else if (resources.get(0).equals("picture")){
              return 1;
            }else{
                return 2;
            }
        }
        @Override
        public int getItemCount() {
            return capsuleContents.size();
        }
        class LinearViewHolder extends RecyclerView.ViewHolder{
            //声明layout_linearrv_item布局控件的变量
            private TextView textView1;
            private TextView textView2;
            private TextView textView3;
            private TextView textView_map;
            private ImageView imageView1;
            private ImageView imageView2;
            private ConstraintLayout constraintLayout;

            public LinearViewHolder(View itemView) {
                super(itemView);
                textView1 =(TextView) itemView.findViewById(R.id.text_detailitem_time);
                textView2 =(TextView) itemView.findViewById(R.id.text_detailitem_mname);
                textView3 =(TextView) itemView.findViewById(R.id.text_detailitem_msg);
                textView_map =(TextView) itemView.findViewById(R.id.text_map);
                imageView1=(ImageView) itemView.findViewById(R.id.image_detailitem_head);
                imageView2=(ImageView) itemView.findViewById(R.id.image_detailitem_timeaxis);
                constraintLayout=(ConstraintLayout) itemView.findViewById(R.id.constraintlayout_msg);
            }

        }
        class LinearViewHolder1 extends RecyclerView.ViewHolder{
            //声明layout_linearrv_item布局控件的变量
            private TextView textView1;
            private TextView textView2;
            private TextView textView3;
            private TextView textView_map;
            private ImageView imageView1;
            private ImageView imageView2;
            private ConstraintLayout constraintLayout;

            private ImageView image_msg_1;
            private ImageView image_msg_2;
            private ImageView image_msg_3;

            public LinearViewHolder1(View itemView) {
                super(itemView);
                textView1 =(TextView) itemView.findViewById(R.id.text_detailitem_time1);
                textView2 =(TextView) itemView.findViewById(R.id.text_detailitem_mname1);
                textView3 =(TextView) itemView.findViewById(R.id.text_detailitem_msg1);
                textView_map =(TextView) itemView.findViewById(R.id.text_map1);
                imageView1=(ImageView) itemView.findViewById(R.id.image_detailitem_head1);
                imageView2=(ImageView) itemView.findViewById(R.id.image_detailitem_timeaxis1);
                constraintLayout=(ConstraintLayout) itemView.findViewById(R.id.constraintlayout_msg1);
                image_msg_1=(ImageView) itemView.findViewById(R.id.image_msg_1);
                image_msg_2=(ImageView) itemView.findViewById(R.id.image_msg_2);
                image_msg_3=(ImageView) itemView.findViewById(R.id.image_msg_3);
            }
        }

        class LinearViewHolder2 extends RecyclerView.ViewHolder{
            //声明layout_linearrv_item布局控件的变量
            private TextView textView1;
            private TextView textView2;
            private TextView textView3;
            private TextView textView_map;
            private ImageView imageView1;
            private ImageView imageView2;
            private ConstraintLayout constraintLayout;

            private VideoView videoView;

            public LinearViewHolder2(View itemView) {
                super(itemView);
                textView1 =(TextView) itemView.findViewById(R.id.text_detailitem_time2);
                textView2 =(TextView) itemView.findViewById(R.id.text_detailitem_mname2);
                textView3 =(TextView) itemView.findViewById(R.id.text_detailitem_msg2);
                textView_map =(TextView) itemView.findViewById(R.id.text_map2);
                imageView1=(ImageView) itemView.findViewById(R.id.image_detailitem_head2);
                imageView2=(ImageView) itemView.findViewById(R.id.image_detailitem_timeaxis2);
                constraintLayout=(ConstraintLayout) itemView.findViewById(R.id.constraintlayout_msg2);
                videoView = (VideoView) itemView.findViewById(R.id.videoView);
            }
        }
        //定义一个接口
        public interface OnItemClickListener{
            //接口默认都是抽象的方法，且类型都是public
            void OnClick(int position);
        }
    }

    public void capsuleContentsRequest(final int capsuleId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Global.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CapsuleContentsRequest request = retrofit.create(CapsuleContentsRequest.class);
        Call<CapsuleContentsResult> call = request.getCapsuleContents(capsuleId);
        call.enqueue(new Callback<CapsuleContentsResult>() {
            @Override
            public void onResponse(Call<CapsuleContentsResult> call, Response<CapsuleContentsResult> response) {
                ArrayList<CapsuleContent> capsuleContents = response.body().getCapsuleContents();

                RealmResults<CapsuleContent> contents = RealmUtil.getRealm().where(CapsuleContent.class).findAll();
                RealmUtil.getInstance().deleteFromDatabase(contents);
                RealmUtil.getInstance().saveInDatabase(capsuleContents);
                if(!Global.downloaded) {
                    for (CapsuleContent capsuleContent : capsuleContents) {
                        RealmList<String> urls = capsuleContent.getResourceUrl();
                        for (int i = 0; i < urls.size(); i++) {
                            if (urls.get(i).equals("picture")) {
                                FileUtil.startDownload(FileUtil.getContentPictureUrl(capsuleId, capsuleContent.getTimeStamp(), i + 1), FileUtil.getContentPictureFilePath(capsuleId, capsuleContent.getTimeStamp(), i + 1));
                            } else {
                                FileUtil.startDownload(FileUtil.getContentVideoUrl(capsuleId, capsuleContent.getTimeStamp(), i + 1), FileUtil.getContentVideoFilePath(capsuleId, capsuleContent.getTimeStamp(), i + 1));
                            }
                        }
                    }
                }
                Global.downloaded = true;



                recyclerView.setAdapter(new LinearAdapter(recyclerView.getContext(), new CapsuleFragment.LinearAdapter.OnItemClickListener() {
                    @Override
                    public void OnClick(int position) {
                        Toast.makeText(getApplicationContext(),"点击位置"+position,Toast.LENGTH_SHORT).show();

                    }
                },capsuleContents));
            }

            @Override
            public void onFailure(Call<CapsuleContentsResult> call, Throwable throwable) {
                RealmResults<CapsuleContent> contents = RealmUtil.getRealm().where(CapsuleContent.class).findAll();
                ArrayList<CapsuleContent> capsuleContents = new ArrayList<>();
                for(CapsuleContent capsuleContent:contents){
                    capsuleContents.add(capsuleContent);
                }

                recyclerView.setAdapter(new LinearAdapter(recyclerView.getContext(), new CapsuleFragment.LinearAdapter.OnItemClickListener() {
                    @Override
                    public void OnClick(int position) {
                        Toast.makeText(getApplicationContext(),"点击位置"+position,Toast.LENGTH_SHORT).show();
                    }
                },capsuleContents));
            }
        });
    }

    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
}
