package Tiny.capsule;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.IncapableCause;
import com.zhihu.matisse.internal.entity.Item;
import com.zhihu.matisse.internal.utils.PhotoMetadataUtils;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Tiny.capsule.http.PostCapsuleContentRequest;
import Tiny.capsule.http.SimpleResult;
import Tiny.capsule.http.UploadRequest;
import Tiny.capsule.location.LocationActivity;
import Tiny.capsule.model.CapsuleContent;
import Tiny.capsule.model.Global;
import butterknife.BindView;
import io.realm.RealmList;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CapsuleSendActivity extends TakePhotoActivity {

    private ImageView back;                      //返回上一层
    private TextView locate;             //定位
    private TextView picture;            //添加图片视频？
    private ImageView send;                      //发表动态
    private ConstraintLayout selectPic;          //显示选中的图片
    private ImageView image1;                    //选中的第一个图片
    private EditText saySomething;               //说点儿什么吧
    private TextView location;                   //发送位置
    private int capsuleId;
    //TakePhoto
    private TakePhoto takePhoto;
    private CropOptions cropOptions;                      //裁剪参数
    private CompressConfig compressConfig;                //压缩参数
    private Uri imageUri;                                 //图片保存路径

    private String filepath;

    private long times;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capsule_send);

        initPermission(); //申请相关权限
        initData();  //设置压缩、裁剪参数

        capsuleId = getIntent().getIntExtra("id", 0);

        times = System.currentTimeMillis();
        location = findViewById(R.id.textView13);
        saySomething = findViewById(R.id.editText_send);
        selectPic = findViewById(R.id.constraintlayout_capsulesend_image);
        selectPic.setVisibility(View.INVISIBLE);

        back = findViewById(R.id.toolbar2_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        locate = findViewById(R.id.textView33);
        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CapsuleSendActivity.this,LocationActivity.class);
                startActivity(intent);
            }
        });

        send = findViewById(R.id.toolbar2_set);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String something = saySomething.getText().toString();
                String position = "北京交通大学·思源楼";
                String src = "222";
                uploadFile(filepath,FileUtil.getContentPictureUrl (capsuleId,times,1));
                postCapsuleContentRequest(new CapsuleContent(capsuleId,Global.currentUsername,Global.currentUsername,times,something,position,new RealmList<String>()));

                finish();

            }
        });

        image1 = findViewById(R.id.image_1);
        picture = findViewById(R.id.textView31);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUri = getImageCropUri();
                //从相册中选取图片并裁剪
                takePhoto.onPickFromGalleryWithCrop(imageUri, cropOptions);
                //从相册中选取不裁剪
                //takePhoto.onPickFromGallery();

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
        filepath = iconPath;
        selectPic.setVisibility(View.VISIBLE);
        Glide.with(this).load(iconPath).into(image1);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        Toast.makeText(CapsuleSendActivity.this, "Error:" + msg, Toast.LENGTH_SHORT).show();
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
            if (AndPermission.hasAlwaysDeniedPermission(CapsuleSendActivity.this, deniedPermissions)) {

                // 用自定义的提示语
                AndPermission.defaultSettingDialog(CapsuleSendActivity.this, 103)
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
        File file=new File(Environment.getExternalStorageDirectory(), "/capsule/"+capsuleId+"/"+times + "-1.jpg");

        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }

    public void postCapsuleContentRequest(CapsuleContent content) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Global.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PostCapsuleContentRequest request = retrofit.create(PostCapsuleContentRequest.class);
        Call<SimpleResult> call = request.postCapsuleContent(new Gson().toJson(content));
        System.out.println(new Gson().toJson(content));
        call.enqueue(new Callback<SimpleResult>() {
            @Override
            public void onResponse(Call<SimpleResult> call, Response<SimpleResult> response) {
                if(response.body().getStatus() == 1){
                    System.out.println("发送成功");
                    Global.added = true;
                }
            }

            @Override
            public void onFailure(Call<SimpleResult> call, Throwable throwable) {
                System.out.println("连接失败");
                throwable.printStackTrace();
            }
        });
    }

    public void uploadFile(String filepath,String serverUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Global.baseUrl)
                .build();
        UploadRequest service = retrofit.create(UploadRequest.class);
        File file = new File(filepath);//访问手机端的文件资源，保证手机端sdcdrd中必须有这个文件
        System.out.println(filepath+"exist?"+file.exists());
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        //这里设置的是传过去的文件名
        MultipartBody.Part body = MultipartBody.Part.createFormData("File", file.getName(), requestFile);

        //这边传过去的key value是 description - descriptionString
        String descriptionString = filepath.replace(FileUtil.fileDir,"");
        RequestBody description =
                RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);

        Call<ResponseBody> call = service.upload(description, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                System.out.println("success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
