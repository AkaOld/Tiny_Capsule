package Tiny.capsule;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.util.List;

import Tiny.capsule.http.CapsuleInviteRequest;
import Tiny.capsule.http.CreateCapsuleRequest;
import Tiny.capsule.http.SimpleResult;
import Tiny.capsule.model.Capsule;
import Tiny.capsule.model.Global;
import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CapsuleCreateActivity extends TakePhotoActivity {

    //UI
    private ImageView back;                               //返回上一层
    private ImageView Background;                         //展示背景图片
    private Button takeFromCameraBtn, takeFromGalleyBtn;  //拍照以及从相册中选取Button
    private Button success;                               //创建
    private EditText Capsulename;                         //胶囊名称
    private RadioGroup radioGroup;                        //胶囊公开性group
    private RadioButton secret;                           //私密
    private RadioButton open;                             //公开
    private EditText introduction;                        //胶囊简介
    private boolean isPublic;                             //公开性

    //TakePhoto
    private TakePhoto takePhoto;
    private CropOptions cropOptions;                      //裁剪参数
    private CompressConfig compressConfig;                //压缩参数
    private Uri imageUri;                                 //图片保存路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capsule_create);

        introduction = findViewById(R.id.textView6);
        Capsulename = findViewById(R.id.text_ccreate_1);
        Background = findViewById(R.id.imageView5);

        initPermission(); //申请相关权限
        initData();  //设置压缩、裁剪参数

        back = findViewById(R.id.toolbar2_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        takeFromCameraBtn = findViewById(R.id.shot);
        takeFromCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUri = getImageCropUri();
                //拍照并裁剪
                takePhoto.onPickFromCaptureWithCrop(imageUri, cropOptions);
                //仅仅拍照不裁剪
                //takePhoto.onPickFromCapture(imageUri);
                startActivity(new Intent(CapsuleCreateActivity.this, MatisseActivity.class));
            }
        });

        takeFromGalleyBtn = findViewById(R.id.album);
        takeFromGalleyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUri = getImageCropUri();
                //从相册中选取图片并裁剪
                takePhoto.onPickFromGalleryWithCrop(imageUri, cropOptions);
                //从相册中选取不裁剪
                //takePhoto.onPickFromGallery();
            }
        });

        radioGroup = findViewById(R.id.group);
        secret = findViewById(R.id.radioButton2);
        open = findViewById(R.id.radioButton);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(secret.getId() == checkedId){
                    isPublic = true;
                }else if(open.getId() == checkedId){
                    isPublic = false;
                }
            }
        });

        success = findViewById(R.id.successCreate);
        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Capsulename.getText().toString();
                boolean pub = isPublic;
                String info = introduction.getText().toString();
                createCapsuleRequest(new Capsule(name,Global.currentUsername,pub));
                finish();






            }
        });


    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        String iconPath = result.getImage().getOriginalPath();
        //Toast显示图片路径
        Toast.makeText(this, "imagePath:" + iconPath, Toast.LENGTH_SHORT).show();
        //Google Glide库 用于加载图片资源
        Glide.with(this).load(iconPath).into(Background);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        Toast.makeText(CapsuleCreateActivity.this, "Error:" + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    private void initPermission() {
        // 申请权限。
        AndPermission.with(this)
                .requestCode(100)
                .permission(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .send();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 只需要调用这一句，其它的交给AndPermission吧，最后一个参数是PermissionListener。
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, listener);
    }

    //权限申请回调接口
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            if(requestCode == 100) {
                // TODO 相应代码。
                //do nothing
            }
        }
        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。

            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(CapsuleCreateActivity.this, deniedPermissions)) {

                // 用自定义的提示语
                AndPermission.defaultSettingDialog(CapsuleCreateActivity.this, 103)
                        .setTitle("权限申请失败")
                        .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                        .setPositiveButton("好，去设置")
                        .show();
            }
        }
    };

    private void initData() {
        ////获取TakePhoto实例
        takePhoto = getTakePhoto();
        //设置裁剪参数
        cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(false).create();
        //设置压缩参数
        //compressConfig=new CompressConfig.Builder().setMaxSize(50*1024).setMaxPixel(800).create();
        compressConfig=new CompressConfig.Builder().setMaxSize(1080*1920).setMaxPixel(800).create();
        takePhoto.onEnableCompress(compressConfig,true);  //设置为需要压缩
    }

    //获得照片的输出保存Uri
    private Uri getImageCropUri() {
        File file=new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }

    public void createCapsuleRequest(Capsule capsule) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Global.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CreateCapsuleRequest request = retrofit.create(CreateCapsuleRequest.class);
        Call<SimpleResult> call = request.createCapsule(new Gson().toJson(capsule));
        call.enqueue(new Callback<SimpleResult>() {
            @Override
            public void onResponse(Call<SimpleResult> call, Response<SimpleResult> response) {
                if(response.body().getStatus() == 1){
                    System.out.println("发送成功");
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
