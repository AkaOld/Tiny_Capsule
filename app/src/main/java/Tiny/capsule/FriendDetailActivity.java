package Tiny.capsule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.Image;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Tiny.capsule.http.UserInfoRequest;
import Tiny.capsule.model.CapsuleContent;
import Tiny.capsule.model.Global;
import Tiny.capsule.model.RealmUtil;
import Tiny.capsule.model.UserInfo;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static Tiny.capsule.CapsuleDetailActivity.getLoacalBitmap;

public class FriendDetailActivity extends AppCompatActivity {

    private ImageView back;                //返回上一层

//    private ImageView img = findViewById(R.id.image_frienddetailitem_fhead);
    private TextView textView1;
    private TextView textView2;
    private ImageView icon;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);

        back = findViewById(R.id.toolbar1_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textView1 = findViewById(R.id.text_frienddetailitem_fname1);
        textView2 = findViewById(R.id.text_frienddetailitem_something);
        icon = findViewById(R.id.image_frienddetailitem_fhead);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerview_frienddetail);
        //设置线性布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Item装饰
        recyclerView.addItemDecoration(new MyDecoration());
        //设置Adapter

        String aa = getIntent().getStringExtra("username");
        userInfoRequest(aa);

    }

    @Override
    protected void onStart() {
        super.onStart();

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
        private RealmList<String> cName;
        private RealmList<Integer> timeSendMsg;
        //声明引用
        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private OnItemClickListener mlistener;
        //创建一个构造函数
        public LinearAdapter(Context context, OnItemClickListener listener, RealmList<String> names,RealmList<Integer> numbers){
            //初始化item数据
            this.cName = names;
            this.timeSendMsg = numbers;

            this.mContext=context;
            //利用LayoutInflater把控件所在的布局文件加载到当前类当中
            mLayoutInflater = LayoutInflater.from(context);
            //从外部传进来一个OnItemClickListener子类的变量
            this.mlistener=listener;
        }
        //此方法要返回一个ViewHolder
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LinearViewHolder(mLayoutInflater.inflate(R.layout.activity_friend_detail_item,parent,false));
        }
        //通过holder设置TextView的内容
        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ((LinearViewHolder)holder).textView1.setText(cName.get(position));
            ((LinearViewHolder)holder).textView2.setText(timeSendMsg.get(position)+" 人");
            if (position%4 ==0){
                ((LinearViewHolder)holder).relativeLayout.setBackgroundResource(R.drawable.shape_massage_item_2);
            }else if (position%4 ==1){
                ((LinearViewHolder)holder).relativeLayout.setBackgroundResource(R.drawable.shape_massage_item_3);
            }else if (position%4 ==2){
                ((LinearViewHolder)holder).relativeLayout.setBackgroundResource(R.drawable.shape_massage_item_4);
            }else{

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
            return 0;
        }
        @Override
        public int getItemCount() {
            return cName.size();
        }
        class LinearViewHolder extends RecyclerView.ViewHolder{
            //声明layout_linearrv_item布局控件的变量
            private TextView textView1;
            private TextView textView2;
            private RelativeLayout relativeLayout;

            public LinearViewHolder(View itemView) {
                super(itemView);
                textView1 =(TextView) itemView.findViewById(R.id.text_frienddetailitem_cname);
                textView2 =(TextView) itemView.findViewById(R.id.text_frienddetailitem_number);
                relativeLayout=(RelativeLayout) itemView.findViewById(R.id.relativeLayout_frienddetail_item);
            }

        }

        //定义一个接口
        public interface OnItemClickListener{
            //接口默认都是抽象的方法，且类型都是public
            void OnClick(int position);
        }
    }

    public void userInfoRequest(String username) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Global.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserInfoRequest request = retrofit.create(UserInfoRequest.class);
        Call<UserInfo> call = request.getUserInfo(username);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                UserInfo info = response.body();

                recyclerView.setAdapter(new LinearAdapter(back.getContext(), new LinearAdapter.OnItemClickListener() {
                    @Override
                    public void OnClick(int position) {
                        Toast.makeText(getApplicationContext(),"点击位置"+position,Toast.LENGTH_SHORT).show();

                    }
                },info.getCapsuleNames(),info.getUserCount()));
                textView1.setText(info.getNickname());
                textView2.setText(info.getSignature());

                FileUtil.startDownload(FileUtil.getUserIconUrl(info.getUsername()),FileUtil.getUserIconFilePath(info.getUsername()));
                String path = FileUtil.getUserIconFilePath(info.getUsername());
                Bitmap bitmap = getLoacalBitmap(path);
                icon.setImageBitmap(bitmap); //设置Bitmap
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable throwable) {
                System.out.println("连接失败");
                throwable.printStackTrace();
            }
        });
    }

    public static String getDateToString(long milSecond) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
}
