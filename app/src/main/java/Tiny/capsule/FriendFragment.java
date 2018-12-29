package Tiny.capsule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

import Tiny.capsule.http.AddFriendRequest;
import Tiny.capsule.http.FriendPreviewRequest;
import Tiny.capsule.http.FriendPreviewResult;
import Tiny.capsule.http.SimpleResult;
import Tiny.capsule.model.CapsuleContent;
import Tiny.capsule.model.FriendPreview;
import Tiny.capsule.model.Global;
import Tiny.capsule.model.RealmUtil;
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
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendFragment extends Fragment implements OnMenuItemClickListener, OnMenuItemLongClickListener {
    //item数据
    ArrayList<String> usernames = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private FragmentManager fragmentManager;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private Button addFriend;
    private String user;

    //    声明一个RecycleView变量
    RecyclerView recyclerView;

    public FriendFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_friend, container, false);

        fragmentManager = ((MainActivity)getActivity()).getSupportFragmentManager();
        initMenuFragment();

        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerview_friend);
        //设置线性布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        //Item装饰
        recyclerView.addItemDecoration(new MyDecoration());
        //设置Adapter
       friendPreviewRequest(Global.currentUsername);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        addFriend = (Button) getActivity().findViewById(R.id.toolbar2_add);

        addFriend.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(), "success2", 0).show();
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
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
    public static FriendFragment newInstance(String param1, String param2) {
        FriendFragment fragment = new FriendFragment();
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

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
        mMenuDialogFragment.setItemLongClickListener(this);
    }

    private List<MenuObject> getMenuObjects() {
        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.icn_close);

        MenuObject send = new MenuObject("查找添加");
        send.setResource(R.drawable.icn_1);

        MenuObject like = new MenuObject("扫码添加");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.icn_2);
        like.setBitmap(b);

//        MenuObject addFr = new MenuObject("Add to friends");
//        BitmapDrawable bd = new BitmapDrawable(getResources(),
//                BitmapFactory.decodeResource(getResources(), R.drawable.icn_3));
//        addFr.setDrawable(bd);
//

        menuObjects.add(close);
        menuObjects.add(send);
        menuObjects.add(like);
//        menuObjects.add(addFr);
//        menuObjects.add(addFav);
//        menuObjects.add(block);
        return menuObjects;
    }

//    @Override
//    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_main, menu);
//        super.onCreateOptionsMenu(menu, inflater);
////        return true;
//    }

    @SuppressLint("WrongConstant")
    @Override
    public void onMenuItemClick(View clickedView, int position) {
        Toast.makeText(getActivity(), "Clicked on position: " + position, 0).show();
        if (position == 1){
            final EditText et = new EditText(getActivity());
            new AlertDialog.Builder(getActivity()).setTitle("请输入消息")
                    .setIcon(android.R.drawable.sym_def_app_icon)
                    .setView(et)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //按下确定键后的事件
                            Toast.makeText(getActivity(), et.getText().toString(),Toast.LENGTH_LONG).show();
                            addFriendRequest(Global.currentUsername,et.getText().toString());
                        }
                    }).setNegativeButton("取消",null).show();

        }else if(position == 2){
            callScanQRCode();
        }

    }
    public void callScanQRCode(){
        IntentIntegrator integrator = new IntentIntegrator((Activity) recyclerView.getContext());
        // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setCaptureActivity(ScanActivity.class); //设置打开摄像头的Activity
        integrator.setPrompt("请扫描ISBN"); //底部的提示文字，设为""可以置空
        integrator.setCameraId(0); //前置或者后置摄像头
        integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }


    @SuppressLint("WrongConstant")
    @Override
    public void onMenuItemLongClick(View clickedView, int position) {
        if(position == 1){



        }
//        Toast.makeText(getActivity(), "Clicked on position: " + position, 0).show();
        startActivity(new Intent(getActivity(), FriendAddActivity.class));
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
        ArrayList<FriendPreview> friendPreviews;
        //声明引用
        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private OnItemClickListener mlistener;
        //创建一个构造函数
        public LinearAdapter(Context context, OnItemClickListener listener,ArrayList<FriendPreview> friendPreviews){
            this.fName = fName;
            this.saySomething = saySomething;
            this.imgPath = imgPath;
            this.friendPreviews = friendPreviews;

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
            return new LinearViewHolder(mLayoutInflater.inflate(R.layout.fragment_friend_item,parent,false));
//        }
        }
        //通过holder设置TextView的内容
        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ((LinearViewHolder)holder).textView1.setText(friendPreviews.get(position).getNickname());
            ((LinearViewHolder)holder).textView2.setText("共同胶囊："+friendPreviews.get(position).getSameCapsule());

            String path = FileUtil.getUserIconFilePath(friendPreviews.get(position).getUsername());
            Bitmap bitmap = getLoacalBitmap(path); //从本地取图片(在cdcard中获取)
            ((LinearViewHolder)holder).imageView .setImageBitmap(bitmap); //设置Bitmap
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
            return friendPreviews.size();
        }
        class LinearViewHolder extends RecyclerView.ViewHolder{
            //声明layout_linearrv_item布局控件的变量
            private TextView textView1;
            private TextView textView2;
            private ImageView imageView;
            private RelativeLayout relativeLayout;

            public LinearViewHolder(View itemView) {
                super(itemView);
                textView1 = itemView.findViewById(R.id.text_frienditem_fname);
                textView2 = itemView.findViewById(R.id.text_frienditem_something);
                imageView = itemView.findViewById(R.id.image_frienditem);
                relativeLayout= itemView.findViewById(R.id.relativelayout_frienditem);
            }

        }
        class LinearViewHolder1 extends RecyclerView.ViewHolder{
            //声明layout_linearrv_item布局控件的变量
            private TextView textView;
            private ImageView imageView;
            private RelativeLayout relativeLayout;

            public LinearViewHolder1(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.text_frienditem_fname);
                imageView= itemView.findViewById(R.id.image_frienditem);
                relativeLayout = itemView.findViewById(R.id.relativelayout_frienditem);
            }
        }
        //定义一个接口
        public interface OnItemClickListener{
            //接口默认都是抽象的方法，且类型都是public
            void OnClick(int position);
        }
    }

    public void friendPreviewRequest(String username) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Global.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FriendPreviewRequest request = retrofit.create(FriendPreviewRequest.class);
        Call<FriendPreviewResult> call = request.getFriendPreviews(username);
//        final RealmResults<FriendPreview> data = RealmUtil.getRealm().where(FriendPreview.class).findAll();
        call.enqueue(new Callback<FriendPreviewResult>() {
            @Override
            public void onResponse(Call<FriendPreviewResult> call, Response<FriendPreviewResult> response) {
                ArrayList<FriendPreview> friendPreviews = response.body().getFriendPreviews();
//                RealmUtil.getInstance().deleteFromDatabase(data);
//                RealmUtil.getInstance().saveInDatabase(friendPreviews);
                RealmResults<FriendPreview> contents = RealmUtil.getRealm().where(FriendPreview.class).findAll();
                RealmUtil.getInstance().deleteFromDatabase(contents);
                RealmUtil.getInstance().saveInDatabase(friendPreviews);


                Global.downloadCount = 0;
                int total = friendPreviews.size();
                for(FriendPreview friendPreview:friendPreviews){
                    usernames.add(friendPreview.getUsername());
                    FileUtil.startDownload(FileUtil.getUserIconUrl(friendPreview.getUsername()),FileUtil.getUserIconFilePath(friendPreview.getUsername()));
                }
                System.out.println("total "+total);
//                while(total != Global.downloadCount);

                recyclerView.setAdapter(new LinearAdapter(recyclerView.getContext(), new LinearAdapter.OnItemClickListener() {
                    @Override
                    public void OnClick(int position) {
                        Toast.makeText(recyclerView.getContext(),"点击位置"+position,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), FriendDetailActivity.class);
                        intent.putExtra("username", usernames.get(position));
                        startActivity(intent);
                    }
                },friendPreviews));
            }

            @Override
            public void onFailure(Call<FriendPreviewResult> call, Throwable throwable) {
                RealmResults<FriendPreview> contents = RealmUtil.getRealm().where(FriendPreview.class).findAll();
                ArrayList<FriendPreview> friendPreviews = new ArrayList<>();
                for(FriendPreview friendPreview:contents){
                    friendPreviews.add(friendPreview);
                }
                recyclerView.setAdapter(new LinearAdapter(recyclerView.getContext(), new LinearAdapter.OnItemClickListener() {
                    @Override
                    public void OnClick(int position) {
                        Toast.makeText(recyclerView.getContext(),"点击位置"+position,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), FriendDetailActivity.class);
                        intent.putExtra("username", usernames.get(position));
                        startActivity(intent);
                    }
                },friendPreviews));
            }
        });
    }

    public void addFriendRequest(String requestUser, String receiveUser) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Global.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AddFriendRequest request = retrofit.create(AddFriendRequest.class);
        Call<SimpleResult> call = request.addFriend(requestUser,receiveUser);
        call.enqueue(new Callback<SimpleResult>() {
            @Override
            public void onResponse(Call<SimpleResult> call, Response<SimpleResult> response) {
                if(response.body().getStatus() == 1){
                    System.out.println("申请发送成功");
                }
            }

            @Override
            public void onFailure(Call<SimpleResult> call, Throwable throwable) {
                System.out.println("连接失败");
                throwable.printStackTrace();
            }
        });
    }
}




