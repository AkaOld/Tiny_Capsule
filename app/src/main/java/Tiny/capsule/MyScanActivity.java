package Tiny.capsule;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import Tiny.capsule.model.Global;

public class MyScanActivity extends AppCompatActivity {
    private ImageView img_myscan;
    private ImageView back;       //返回上一层

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_scan);

        img_myscan = findViewById(R.id.image_scan);

        img_myscan.setImageBitmap(QRCodeUtil.encodeAsBitmap(Global.currentUsername));

        back = findViewById(R.id.toolbar_qr_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
