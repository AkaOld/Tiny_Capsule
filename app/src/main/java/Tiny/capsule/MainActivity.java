package Tiny.capsule;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.gsm.GsmCellLocation;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.*;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.yanzhenjie.permission.AndPermission;

import java.util.ArrayList;
import java.util.List;

import Tiny.capsule.http.AddFriendRequest;
import Tiny.capsule.http.SimpleResult;
import Tiny.capsule.model.Global;
import Tiny.capsule.model.RealmUtil;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener, ViewPager.OnPageChangeListener
        ,CapsuleFragment.OnFragmentInteractionListener, FriendFragment.OnFragmentInteractionListener, MeFragment.OnFragmentInteractionListener {
    private ViewPager viewPager;
    private BottomNavigationBar bottomNavigationBar;
    //private BadgeItem badgeItem; //添加角标
    private List<Fragment> mList; //ViewPager的数据源

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        int id = getIntent().getIntExtra("id", 0);
//        if (id == 1) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.f,new CapsuleFragment())
//                    .addToBackStack(null)
//                    .commit();
//        }
        initBottomNavigationBar();
        initViewPager();
        initPermission(); //申请相关权限
        RealmUtil.init(this);
        LoginStatus status = RealmUtil.getRealm().where(LoginStatus.class).findFirst();
        if(status == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        else{
            Global.currentUsername = status.getUsername();
            System.out.println("============================================="+Global.currentUsername);
        }
    }

    private void initPermission() {
        // 申请权限。
        AndPermission.with(this)
                .requestCode(100)
                .permission(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .send();
    }
    //初始化ViewPager
    private void initViewPager() {
        mList = new ArrayList<>();
        mList.add(new CapsuleFragment());
        mList.add(new FriendFragment());
        mList.add(new MeFragment());

        viewPager = findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(this);
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager(), mList);
        viewPager.setAdapter(mainAdapter); //视图加载适配器
    }

    //初始化底部导航条
    private void initBottomNavigationBar() {
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setTabSelectedListener(this);
//        badgeItem = new BadgeItem()
//                .setHideOnSelect(true) //设置被选中时隐藏角标
//                .setBackgroundColor(Color.RED)
//                .setText("99");
        /**
         * 设置模式
         * 1、BottomNavigationBar.MODE_DEFAULT 默认
         * 如果Item的个数<=3就会使用MODE_FIXED模式，否则使用MODE_SHIFTING模式
         *
         * 2、BottomNavigationBar.MODE_FIXED 固定大小
         * 填充模式，未选中的Item会显示文字，没有换挡动画。
         *
         * 3、BottomNavigationBar.MODE_SHIFTING 不固定大小
         * 换挡模式，未选中的Item不会显示文字，选中的会显示文字。在切换的时候会有一个像换挡的动画
         */
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        /**
         * 设置背景的样式
         * 1、BottomNavigationBar.BACKGROUND_STYLE_DEFAULT 默认
         * 如果设置的Mode为MODE_FIXED，将使用BACKGROUND_STYLE_STATIC 。
         * 如果Mode为MODE_SHIFTING将使用BACKGROUND_STYLE_RIPPLE。
         *
         * 2、BottomNavigationBar.BACKGROUND_STYLE_STATIC 导航条的背景色是白色，
         * 加上setBarBackgroundColor（）可以设置成你所需要的任何背景颜色
         * 点击的时候没有水波纹效果
         *
         * 3、BottomNavigationBar.BACKGROUND_STYLE_RIPPLE 导航条的背景色是你设置的处于选中状态的
         * Item的颜色（ActiveColor），也就是setActiveColorResource这个设置的颜色
         * 点击的时候有水波纹效果
         */
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        //设置导航条背景颜色
        //在BACKGROUND_STYLE_STATIC下，表示整个容器的背景色。
        // 而在BACKGROUND_STYLE_RIPPLE下，表示选中Item的图标和文本颜色。默认 Color.WHITE
        bottomNavigationBar.setBarBackgroundColor(R.color.color5);
        //选中时的颜色,在BACKGROUND_STYLE_STATIC下，表示选中Item的图标和文本颜色。
        // 而在BACKGROUND_STYLE_RIPPLE下，表示整个容器的背景色。默认Theme's Primary Color
        //bottomNavigationBar.setActiveColor(R.color.black);
        //未选中时的颜色，表示未选中Item中的图标和文本颜色。默认为 Color.LTGRAY
        //bottomNavigationBar.setInActiveColor("#FFFFFF");


        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_home_white_24dp, "首页").setActiveColorResource(R.color.white))
                .addItem(new BottomNavigationItem(R.drawable.ic_friend_note_white_24dp, "朋友").setActiveColorResource(R.color.white))
                .addItem(new BottomNavigationItem(R.drawable.ic_me_note_white_24dp, "我的").setActiveColorResource(R.color.white))
                .setFirstSelectedPosition(0)
                .initialise(); //所有的设置需在调用该方法前完成

        //如果使用颜色的变化不足以展示一个选项Item的选中与非选中状态，
        // 可以使用BottomNavigationItem.setInActiveIcon()方法来设置。
//        new BottomNavigationItem(R.drawable.ic_home_white_24dp, "公交")//这里表示选中的图片
//                .setInactiveIcon(ContextCompat.getDrawable(this,R.mipmap.ic_launcher));//非选中的图片
    }

    @Override
    public void onTabSelected(int position) {
        //tab被选中
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //ViewPager滑动
        bottomNavigationBar.selectTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this,"交流,角楼",Toast.LENGTH_LONG).show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            String result = scanResult.getContents();
            addFriendRequest(Global.currentUsername,result);
        }
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
