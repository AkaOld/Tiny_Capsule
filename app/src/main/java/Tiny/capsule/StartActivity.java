package Tiny.capsule;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;

public class StartActivity extends AppCompatActivity {

    private Handler handler;
    private MyThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        handler = new Handler();
        thread = new MyThread();
        handler.postDelayed(thread, 2500);
    }

    private class MyThread implements Runnable {

        @Override
        public void run() {
            startActivity(new Intent(StartActivity.this, MainActivity.class));
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(thread);//移除回调
    }

}
