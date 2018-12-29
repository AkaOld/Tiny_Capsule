package Tiny.capsule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Tiny.capsule.http.*;
import Tiny.capsule.model.*;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CapsuleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CapsuleFragment extends Fragment {
    private ArrayList<Integer> ids = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<ArrayList<String>> iconss = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Button createNew;
    RecyclerView recyclerView;

    public CapsuleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_capsule, container, false);

        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerview_home);
        //设置线性布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        //Item装饰
        recyclerView.addItemDecoration(new MyDecoration());
        //设置Adapter
//        Realm.init(recyclerView.getContext());
        capsulePreviewRequest(Global.currentUsername);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        createNew = (Button) getActivity().findViewById(R.id.create);
        createNew.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), CapsuleCreateActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CapsuleFragment newInstance(String param1, String param2) {
        CapsuleFragment fragment = new CapsuleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
        private String lastDate[];
        private String lastMsg[];
        private String number[];
        private String imgPath[];
        ArrayList<CapsulePreview> capsulePreviews;
        ArrayList<ArrayList<String>> iconss;
        //声明引用
        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private OnItemClickListener mlistener;
        //创建一个构造函数
        public LinearAdapter(Context context,OnItemClickListener listener,ArrayList<CapsulePreview> capsulePreviews,ArrayList<ArrayList<String>> iconss){
            //初始化item数据
            this.cName = cName;
            this.lastDate = lastDate;
            this.lastMsg = lastMsg;
            this.number = number;
            this.imgPath = imgPath;
            this.capsulePreviews = capsulePreviews;
            this.iconss = iconss;

            this.mContext=context;
            //利用LayoutInflater把控件所在的布局文件加载到当前类当中
            mLayoutInflater = LayoutInflater.from(context);
            //从外部传进来一个OnItemClickListener子类的变量
            this.mlistener=listener;
        }
        //此方法要返回一个ViewHolder
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType ==0){
            return new LinearViewHolder(mLayoutInflater.inflate(R.layout.fragment_capsule_item,parent,false));
//        }
        }
        //通过holder设置TextView的内容
        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ((LinearViewHolder)holder).textView1.setText(capsulePreviews.get(position).getName());
            ((LinearViewHolder)holder).textView2.setText(getDateToString(capsulePreviews.get(position).getLastPost()));
            ((LinearViewHolder)holder).textView3.setText(capsulePreviews.get(position).getLastContet());
            ((LinearViewHolder)holder).textView4.setText(capsulePreviews.get(position).getIcons().size()+"人");
            if (position%4 ==0){
                ((LinearViewHolder)holder).relativeLayout.setBackgroundResource(R.drawable.shape_massage_item_2);
            }else if (position%4 ==1){
                ((LinearViewHolder)holder).relativeLayout.setBackgroundResource(R.drawable.shape_massage_item_3);
            }else if (position%4 ==2){
                ((LinearViewHolder)holder).relativeLayout.setBackgroundResource(R.drawable.shape_massage_item_4);
            }else{

            }
            String path = FileUtil.getUserIconFilePath(iconss.get(position).get(0));
            Bitmap bitmap = getLoacalBitmap(path);
            ((LinearViewHolder)holder).imageView1.setImageBitmap(bitmap);
            if(iconss.get(position).size()<2){
                ((LinearViewHolder)holder).imageView2.setVisibility(ImageView.INVISIBLE);
                ((LinearViewHolder)holder).imageView3.setVisibility(ImageView.INVISIBLE);
            }
            else if(iconss.get(position).size()<3){
                String path1 = FileUtil.getUserIconFilePath(iconss.get(position).get(1));
                Bitmap bitmap1 = getLoacalBitmap(path1);
                ((LinearViewHolder)holder).imageView2.setImageBitmap(bitmap1);
                ((LinearViewHolder)holder).imageView2.setVisibility(ImageView.VISIBLE);
                ((LinearViewHolder)holder).imageView3.setVisibility(ImageView.INVISIBLE);
            }else{
                String path1 = FileUtil.getUserIconFilePath(iconss.get(position).get(1));
                Bitmap bitmap1 = getLoacalBitmap(path1);
                ((LinearViewHolder)holder).imageView2.setImageBitmap(bitmap1);
                String path2 = FileUtil.getUserIconFilePath(iconss.get(position).get(2));
                Bitmap bitmap2 = getLoacalBitmap(path2);
                ((LinearViewHolder)holder).imageView3.setImageBitmap(bitmap2);
                ((LinearViewHolder)holder).imageView2.setVisibility(ImageView.VISIBLE);
                ((LinearViewHolder)holder).imageView3.setVisibility(ImageView.VISIBLE);
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
//        if(position % 2==0){  //位置是偶数
//            return 0;
//        }else { //位置是奇数
//            return 1;
//        }
            return 0;
        }

        @Override
        public int getItemCount() {
            return capsulePreviews.size();
        }
        class LinearViewHolder extends RecyclerView.ViewHolder{
            //声明layout_linearrv_item布局控件的变量
            private TextView textView1;
            private TextView textView2;
            private TextView textView3;
            private TextView textView4;
            private ImageView imageView1;
            private ImageView imageView2;
            private ImageView imageView3;
            private RelativeLayout relativeLayout;

            public LinearViewHolder(View itemView) {
                super(itemView);
                textView1 =(TextView) itemView.findViewById(R.id.text_homeitem_cname);
                textView2 =(TextView) itemView.findViewById(R.id.text_homeitem_lastdate);
                textView3 =(TextView) itemView.findViewById(R.id.text_homeitem_lastmsg);
                textView4 =(TextView) itemView.findViewById(R.id.text_homeitem_number);
                relativeLayout=(RelativeLayout) itemView.findViewById(R.id.relativelayout_homeitem);
                imageView1=(ImageView) itemView.findViewById(R.id.image_homeitem_1);
                imageView2=(ImageView) itemView.findViewById(R.id.image_homeitem_2);
                imageView3=(ImageView) itemView.findViewById(R.id.image_homeitem_3);
            }

        }
        class LinearViewHolder1 extends RecyclerView.ViewHolder{
            //声明layout_linearrv_item布局控件的变量
            private TextView textView;
            private ImageView imageView;
            private RelativeLayout relativeLayout;

            public LinearViewHolder1(View itemView) {
                super(itemView);
                textView =(TextView) itemView.findViewById(R.id.text_homeitem_cname);
                imageView=(ImageView) itemView.findViewById(R.id.image_homeitem_3);
                relativeLayout=(RelativeLayout) itemView.findViewById(R.id.relativelayout_homeitem);
            }
        }
        //定义一个接口
        public interface OnItemClickListener{
            //接口默认都是抽象的方法，且类型都是public
            void OnClick(int position);
        }
    }
    public void capsulePreviewRequest(String username) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Global.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CapsulePreviewRequest request = retrofit.create(CapsulePreviewRequest.class);
        Call<CapsulePreviewResult> call = request.getCapsulePreviews(username);
//        final RealmResults<CapsulePreview> data = RealmUtil.getRealm().where(CapsulePreview.class).findAll();
        call.enqueue(new Callback<CapsulePreviewResult>() {
            @Override
            public void onResponse(Call<CapsulePreviewResult> call, Response<CapsulePreviewResult> response) {
                ArrayList<CapsulePreview> capsulePreviews = response.body().getCapsulePreviews();

                RealmResults<CapsulePreview> contents = RealmUtil.getRealm().where(CapsulePreview.class).findAll();
                RealmUtil.getInstance().deleteFromDatabase(contents);
                RealmUtil.getInstance().saveInDatabase(capsulePreviews);

                for(CapsulePreview capsulePreview:capsulePreviews){
                    ids.add(capsulePreview.getId());
                    names.add(capsulePreview.getName());
                    ArrayList<String> icons = new ArrayList<>();
                    for(String icon : capsulePreview.getIcons()){
                        icons.add(icon);
                        if(!Global.downloaded) {
                            FileUtil.startDownload(FileUtil.getUserIconUrl(icon), FileUtil.getUserIconFilePath(icon));
                        }
                    }
                    iconss.add(icons);

                }
                Global.downloaded = true;
                recyclerView.setAdapter(new LinearAdapter(recyclerView.getContext(), new LinearAdapter.OnItemClickListener() {
                    @Override
                    public void OnClick(int position) {
                        //Toast.makeText(view.getContext(),"点击位置"+position,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), CapsuleDetailActivity.class);
                        intent.putExtra("id",ids.get(position));
                        intent.putExtra("name",names.get(position));
                        startActivity(intent);
                    }
                },capsulePreviews,iconss));
//                Global.downloadCount = 0;
//                while(Global.downloadCount != totalDownload);
            }

            @Override
            public void onFailure(Call<CapsulePreviewResult> call, Throwable throwable) {
                RealmResults<CapsulePreview> contents = RealmUtil.getRealm().where(CapsulePreview.class).findAll();
                ArrayList<CapsulePreview> capsulePreviews = new ArrayList<>();
                for(CapsulePreview capsulePreview:contents){
                    capsulePreviews.add(capsulePreview);
                    ArrayList<String> icons = new ArrayList<>();
                    for(String icon : capsulePreview.getIcons()){
                        icons.add(icon);
                        if(!Global.downloaded) {
                            FileUtil.startDownload(FileUtil.getUserIconUrl(icon), FileUtil.getUserIconFilePath(icon));
                        }
                    }
                    iconss.add(icons);
                }
                recyclerView.setAdapter(new LinearAdapter(recyclerView.getContext(), new LinearAdapter.OnItemClickListener() {
                    @Override
                    public void OnClick(int position) {
                        //Toast.makeText(view.getContext(),"点击位置"+position,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), CapsuleDetailActivity.class);
                        intent.putExtra("id",ids.get(position));
                        intent.putExtra("name",names.get(position));
                        Global.downloaded = false;
                        Global.icons = iconss.get(position);
                        startActivity(intent);
                    }
                },capsulePreviews,iconss));
            }
        });
    }

    public static String getDateToString(long milSecond) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}

