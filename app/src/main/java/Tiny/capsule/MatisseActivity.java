package Tiny.capsule;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.List;

public class MatisseActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CHOOSE = 23;
    List<Uri> mSelected;
    private GridView grids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matisse);
        grids = findViewById(R.id.grids);

        Matisse.from(MatisseActivity.this)
                .choose(MimeType.ofAll())//照片视频全部显示
                .countable(true)//有序选择图片
                .maxSelectable(9)//最大选择数量为9
                .gridExpectedSize(240)//图片显示表格的大小
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)//图像选择和预览活动所需的方向。
                .thumbnailScale(0.85f)//缩放比例
                .theme(R.style.Matisse_Dracula)//主题  暗色主题 R.style.Matisse_Dracula
                .imageEngine(new GlideEngine())//加载方式
                .capture(true)//设置是否可以拍照
                .captureStrategy(new CaptureStrategy(true, "test.bawei.com.tup.fileprovider"))//存储到哪里
                .forResult(REQUEST_CODE_CHOOSE);//

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
//            回调要显示的图片
            grids.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return mSelected.size();
                }

                @Override
                public Object getItem(int position) {
                    return mSelected.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    convertView=View.inflate(MatisseActivity.this,R.layout.matisse_item,null);
                    ImageView c= convertView.findViewById(R.id.img);
                    //PhotoViewAttacher attacher= new PhotoViewAttacher(c);

                    Glide.with(MatisseActivity.this).load(mSelected.get(position)).into(c);
                    return convertView;
                }
            });
            Log.e("Matisse", "mSelected: " + mSelected);
        }
    }

}
