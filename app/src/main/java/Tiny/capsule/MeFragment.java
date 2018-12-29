package Tiny.capsule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Tiny.capsule.http.CapsuleRequestRequest;
import Tiny.capsule.http.CapsuleRequestResult;
import Tiny.capsule.http.FriendRequestRequest;
import Tiny.capsule.http.FriendRequestResult;
import Tiny.capsule.http.UserInfoRequest;
import Tiny.capsule.model.CapsuleContent;
import Tiny.capsule.model.CapsuleRequest;
import Tiny.capsule.model.FriendRequest;
import Tiny.capsule.model.Global;
import Tiny.capsule.model.RealmUtil;
import Tiny.capsule.model.UserInfo;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static Tiny.capsule.CapsuleDetailActivity.getLoacalBitmap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ImageView log;

    private LinearLayout linearlayout_mypage;
    private RecyclerView recycler_friendapply;
    private RecyclerView recycler_capsuleinvitation;
    private RecyclerView recycler_sysnotification;

    private TextView user;
    private TextView signature;
    private ImageView icon;

    public MeFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_me, container, false);

        linearlayout_mypage = view.findViewById(R.id.linearlayout_mypage);
        recycler_friendapply = view.findViewById(R.id.recycler_friendapply);
        recycler_capsuleinvitation = view.findViewById(R.id.recycler_capsuleinvitation);
        recycler_sysnotification = view.findViewById(R.id.recycler_sysnotification);
        icon = view.findViewById(R.id.imageView1);

        user = view.findViewById(R.id.nameTxt);
        signature = view.findViewById(R.id.text_homeitem_number);

        recycler_friendapply.setVisibility(View.INVISIBLE);
        recycler_capsuleinvitation.setVisibility(View.INVISIBLE);
        recycler_sysnotification.setVisibility(View.INVISIBLE);
//        linearlayout_mypage.setVisibility(View.INVISIBLE);

        recycler_friendapply.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler_capsuleinvitation.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler_sysnotification.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler_friendapply.addItemDecoration(new MyDecoration());
        recycler_capsuleinvitation.addItemDecoration(new MyDecoration());
        recycler_sysnotification.addItemDecoration(new MyDecoration());

//        recycler_sysnotification.setAdapter(new LinearAdapter2(view.getContext(), new LinearAdapter2.OnItemClickListener() {
//            @Override
//            public void OnClick(int position) {
//                Toast.makeText(view.getContext(),"点击位置"+position,Toast.LENGTH_SHORT).show();
//            }
//        }));

        // Inflate the layout for this fragment
        friendRequestRequest(Global.currentUsername);
        capsuleRequestRequest(Global.currentUsername);
        userInfoRequest2(Global.currentUsername);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().findViewById(R.id.image_friendapply).setOnClickListener(new View.OnClickListener() {

            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                recycler_friendapply.setVisibility(View.VISIBLE);
                recycler_capsuleinvitation.setVisibility(View.INVISIBLE);
                recycler_sysnotification.setVisibility(View.INVISIBLE);
                linearlayout_mypage.setVisibility(View.INVISIBLE);
            }
        });

        getActivity().findViewById(R.id.image_capsuleinvitation).setOnClickListener(new View.OnClickListener() {

            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                recycler_friendapply.setVisibility(View.INVISIBLE);
                recycler_capsuleinvitation.setVisibility(View.VISIBLE);
                recycler_sysnotification.setVisibility(View.INVISIBLE);
                linearlayout_mypage.setVisibility(View.INVISIBLE);
            }
        });

        getActivity().findViewById(R.id.image_sysnotification).setOnClickListener(new View.OnClickListener() {

            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                recycler_friendapply.setVisibility(View.INVISIBLE);
                recycler_capsuleinvitation.setVisibility(View.INVISIBLE);
                recycler_sysnotification.setVisibility(View.VISIBLE);
                linearlayout_mypage.setVisibility(View.INVISIBLE);
            }
        });

        getActivity().findViewById(R.id.image_mypage).setOnClickListener(new View.OnClickListener() {

            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                recycler_friendapply.setVisibility(View.INVISIBLE);
                recycler_capsuleinvitation.setVisibility(View.INVISIBLE);
                recycler_sysnotification.setVisibility(View.INVISIBLE);
                linearlayout_mypage.setVisibility(View.VISIBLE);
            }
        });

        getActivity().findViewById(R.id.image_myscan).setOnClickListener(new View.OnClickListener() {

            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(getActivity(), MyScanActivity.class));
            }
        });

        log = getActivity().findViewById(R.id.imageView1);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RegisterActivity.class));
            }
        });

        userInfoRequest(Global.currentUsername);
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
    public static MeFragment newInstance(String param1, String param2) {
        MeFragment fragment = new MeFragment();
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
                System.out.println(response.body().getUsername());
                System.out.println(response.body().getNickname());
                System.out.println(response.body().getIcon());
                System.out.println(response.body().getSignature());
                for(int i = 0;i<response.body().getCapsuleId().size();i++){
                    System.out.println(response.body().getCapsuleNames().get(i));
                    System.out.println(response.body().getUserCount().get(i));
                }

            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable throwable) {
                System.out.println("连接失败");
                throwable.printStackTrace();
            }
        });
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
        private String fName[];
        private String saySomething[];
        private String imgPath[];
        ArrayList<FriendRequest> friendRequests;

        //声明引用
        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private LinearAdapter.OnItemClickListener mlistener;
        //创建一个构造函数
        public LinearAdapter(Context context, LinearAdapter.OnItemClickListener listener,ArrayList<FriendRequest> friendRequests){
            this.fName = fName;
            this.saySomething = saySomething;
            this.imgPath = imgPath;
            this.friendRequests = friendRequests;
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
            return new LinearAdapter.LinearViewHolder(mLayoutInflater.inflate(R.layout.z_friendapply_item,parent,false));
//        }
        }
        //通过holder设置TextView的内容
        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            if(Global.currentUsername.equals( friendRequests.get(position).getRequestUsername())){
                ((LinearViewHolder)holder).textFname.setText(friendRequests.get(position).getReceiveNickname());
            }
            else if(Global.currentUsername.equals(friendRequests.get(position).getReceiveUsername())){
                ((LinearViewHolder)holder).textFname.setText(friendRequests.get(position).getRequestNickname());
            }
            ((LinearViewHolder)holder).textTime.setText(getDateToString(friendRequests.get(position).getRequestTime()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistener.OnClick(position,((LinearViewHolder)holder).textaccept);
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
            return friendRequests.size();
        }
        private class LinearViewHolder extends RecyclerView.ViewHolder{
            //声明layout_linearrv_item布局控件的变量
            private TextView textFname;
            private TextView textTime;
            private TextView textaccept;

            public LinearViewHolder(View itemView) {
                super(itemView);
                textFname =(TextView) itemView.findViewById(R.id.text_friendapplyitem_fname);
                textTime =(TextView) itemView.findViewById(R.id.text_friendapplyitem_time);
                textaccept =(TextView) itemView.findViewById(R.id.text_friendapplyitem_accept);
            }

        }
        //定义一个接口
        public interface OnItemClickListener{
            //接口默认都是抽象的方法，且类型都是public
            void OnClick(int position, TextView textview);
        }
    }

    static class LinearAdapter1 extends RecyclerView.Adapter <RecyclerView.ViewHolder>{
        //item数据
        private String fName[];
        private String saySomething[];
        private String imgPath[];
        private ArrayList<CapsuleRequest> capsuleRequests;
        //声明引用
        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private LinearAdapter1.OnItemClickListener mlistener;
        //创建一个构造函数
        public LinearAdapter1(Context context, LinearAdapter1.OnItemClickListener listener,ArrayList<CapsuleRequest> capsuleRequests){
            this.fName = fName;
            this.saySomething = saySomething;
            this.imgPath = imgPath;
            this.capsuleRequests = capsuleRequests;
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
            return new LinearAdapter1.LinearViewHolder(mLayoutInflater.inflate(R.layout.z_capsuleinvitation_item,parent,false));
//        }
        }
        //通过holder设置TextView的内容
        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            ((LinearViewHolder)holder).textCname.setText(capsuleRequests.get(position).getCapsuleName());
            ((LinearViewHolder)holder).textTime.setText(getDateToString(capsuleRequests.get(position).getRequestTime()));
            if(Global.currentUsername.equals(capsuleRequests.get(position).getRequestUsername())){
                ((LinearAdapter1.LinearViewHolder)holder).textFname.setText(capsuleRequests.get(position).getReceiveNickname());
            }
            else if(Global.currentUsername.equals(capsuleRequests.get(position).getReceiveUsername())){
                ((LinearAdapter1.LinearViewHolder)holder).textFname.setText(capsuleRequests.get(position).getRequestNickname());
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistener.OnClick(position, ((LinearViewHolder)holder).textaccept);
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
            return capsuleRequests.size();
        }
        private class LinearViewHolder extends RecyclerView.ViewHolder{
            //声明layout_linearrv_item布局控件的变量
            private TextView textCname;
            private TextView textTime;
            private TextView textFname;
            private TextView textaccept;

            public LinearViewHolder(View itemView) {
                super(itemView);
                textCname =(TextView) itemView.findViewById(R.id.text_invitationitem_cname);
                textTime =(TextView) itemView.findViewById(R.id.text_invitationitem_time);
                textFname =(TextView) itemView.findViewById(R.id.text_invitationitem_fname);
                textaccept =(TextView) itemView.findViewById(R.id.text_invitationitem_accept);
            }

        }
        //定义一个接口
        public interface OnItemClickListener{
            //接口默认都是抽象的方法，且类型都是public
            void OnClick(int position, TextView textview);
        }
    }

    static class LinearAdapter2 extends RecyclerView.Adapter <RecyclerView.ViewHolder>{
        //item数据
        private String fName[];
        private String saySomething[];
        private String imgPath[];
        private ArrayList<CapsuleRequest> capsuleRequests;
        //声明引用
        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private LinearAdapter2.OnItemClickListener mlistener;
        //创建一个构造函数
        public LinearAdapter2(Context context, LinearAdapter2.OnItemClickListener listener){
            this.fName = fName;
            this.saySomething = saySomething;
            this.imgPath = imgPath;
            this.capsuleRequests = capsuleRequests;
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
            return new LinearAdapter2.LinearViewHolder(mLayoutInflater.inflate(R.layout.z_notification_item,parent,false));
//        }
        }
        //通过holder设置TextView的内容
        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            if(position==0)
                ((LinearViewHolder)holder).textSysSomething.setText("来了老弟~~");
            else if (position==1)
                ((LinearViewHolder)holder).textSysSomething.setText("来创建你的第一个胶囊吧！");
            else
                ((LinearViewHolder)holder).textSysSomething.setText("想知道雷佳音是谁吗？");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistener.OnClick(position, ((LinearViewHolder)holder).text_read);
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
            return 3;
        }
        private class LinearViewHolder extends RecyclerView.ViewHolder{
            //声明layout_linearrv_item布局控件的变量
            private TextView textSysSomething;
            private TextView text_read;

            public LinearViewHolder(View itemView) {
                super(itemView);
                textSysSomething =(TextView) itemView.findViewById(R.id.text_notificationitem_something);
                text_read =(TextView) itemView.findViewById(R.id.text_notificationitem_confirm);
            }

        }
        //定义一个接口
        public interface OnItemClickListener{
            //接口默认都是抽象的方法，且类型都是public
            void OnClick(int position, TextView textview);
        }
    }

    public void capsuleRequestRequest(String username) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Global.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CapsuleRequestRequest request = retrofit.create(CapsuleRequestRequest.class);
        Call<CapsuleRequestResult> call = request.getCapsuleRequests(username);
        call.enqueue(new Callback<CapsuleRequestResult>() {
            @Override
            public void onResponse(Call<CapsuleRequestResult> call, Response<CapsuleRequestResult> response) {
                ArrayList<CapsuleRequest> capsuleRequests = response.body().getCapsuleRequests();

                RealmResults<CapsuleRequest> contents = RealmUtil.getRealm().where(CapsuleRequest.class).findAll();
                RealmUtil.getInstance().deleteFromDatabase(contents);
                RealmUtil.getInstance().saveInDatabase(capsuleRequests);

                recycler_capsuleinvitation.setAdapter(new LinearAdapter1(recycler_capsuleinvitation.getContext(), new LinearAdapter1.OnItemClickListener() {
                    @Override
                    public void OnClick(int position, TextView textview) {
                        textview.setBackgroundResource(R.drawable.shape_textview_3gray);
                        textview.setText("已接受");
                        textview.setTextSize(12);
                    }
                },capsuleRequests));
                recycler_sysnotification.setAdapter(new LinearAdapter2(recycler_sysnotification.getContext(), new LinearAdapter2.OnItemClickListener() {
                    @Override
                    public void OnClick(int position, TextView textview) {
                        textview.setBackgroundResource(R.drawable.shape_textview_3gray);
                        textview.setText("已阅读");
                        textview.setTextSize(12);
                    }
                }));
            }

            @Override
            public void onFailure(Call<CapsuleRequestResult> call, Throwable throwable) {
                RealmResults<CapsuleRequest> contents = RealmUtil.getRealm().where(CapsuleRequest.class).findAll();
                ArrayList<CapsuleRequest> capsuleRequests = new ArrayList<>();
                for(CapsuleRequest capsuleRequest:contents){
                    capsuleRequests.add(capsuleRequest);
                }
                recycler_capsuleinvitation.setAdapter(new LinearAdapter1(recycler_capsuleinvitation.getContext(), new LinearAdapter1.OnItemClickListener() {
                    @Override
                    public void OnClick(int position, TextView textview) {
                        textview.setBackgroundResource(R.drawable.shape_textview_3gray);
                        textview.setText("已接受");
                        textview.setTextSize(12);
                    }
                },capsuleRequests));
                recycler_sysnotification.setAdapter(new LinearAdapter2(recycler_sysnotification.getContext(), new LinearAdapter2.OnItemClickListener() {
                    @Override
                    public void OnClick(int position, TextView textview) {
                        textview.setBackgroundResource(R.drawable.shape_textview_3gray);
                        textview.setText("已阅读");
                        textview.setTextSize(12);
                    }
                }));
            }
        });
    }

    public void friendRequestRequest(String username) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Global.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FriendRequestRequest request = retrofit.create(FriendRequestRequest.class);
        Call<FriendRequestResult> call = request.getFriendRequests(username);
        call.enqueue(new Callback<FriendRequestResult>() {
            @Override
            public void onResponse(Call<FriendRequestResult> call, Response<FriendRequestResult> response) {
                ArrayList<FriendRequest> friendRequests = response.body().getFriendRequests();

                RealmResults<FriendRequest> contents = RealmUtil.getRealm().where(FriendRequest.class).findAll();
                RealmUtil.getInstance().deleteFromDatabase(contents);
                RealmUtil.getInstance().saveInDatabase(friendRequests);

                recycler_friendapply.setAdapter(new LinearAdapter(recycler_capsuleinvitation.getContext(), new LinearAdapter.OnItemClickListener() {
                    @Override
                    public void OnClick(int position, TextView textview) {
                        textview.setBackgroundResource(R.drawable.shape_textview_3gray);
                        textview.setText("已同意");
                        textview.setTextSize(12);
                    }
                },friendRequests));
                recycler_sysnotification.setAdapter(new LinearAdapter2(recycler_sysnotification.getContext(), new LinearAdapter2.OnItemClickListener() {
                    @Override
                    public void OnClick(int position, TextView textview) {
                        textview.setBackgroundResource(R.drawable.shape_textview_3gray);
                        textview.setText("已阅读");
                        textview.setTextSize(12);
                    }
                }));
            }

            @Override
            public void onFailure(Call<FriendRequestResult> call, Throwable throwable) {
                RealmResults<FriendRequest> contents = RealmUtil.getRealm().where(FriendRequest.class).findAll();
                ArrayList<FriendRequest> friendRequests = new ArrayList<>();
                for(FriendRequest friendRequest:contents){
                    friendRequests.add(friendRequest);
                }
                recycler_friendapply.setAdapter(new LinearAdapter(recycler_capsuleinvitation.getContext(), new LinearAdapter.OnItemClickListener() {
                    @Override
                    public void OnClick(int position, TextView textview) {
                        textview.setBackgroundResource(R.drawable.shape_textview_3gray);
                        textview.setText("已同意");
                        textview.setTextSize(12);
                    }
                },friendRequests));
                recycler_sysnotification.setAdapter(new LinearAdapter2(recycler_sysnotification.getContext(), new LinearAdapter2.OnItemClickListener() {
                    @Override
                    public void OnClick(int position, TextView textview) {
                        textview.setBackgroundResource(R.drawable.shape_textview_3gray);
                        textview.setText("已阅读");
                        textview.setTextSize(12);
                    }
                }));
            }
        });
    }

    public void userInfoRequest2(final String username) {
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
                FileUtil.startDownload(FileUtil.getUserIconUrl(info.getUsername()),FileUtil.getUserIconFilePath(info.getUsername()));
                String path = FileUtil.getUserIconFilePath(info.getUsername());
                Bitmap bitmap = getLoacalBitmap(path);
                icon.setImageBitmap(bitmap); //设置Bitmap
                user.setText(info.getNickname());
                signature.setText(info.getSignature());
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