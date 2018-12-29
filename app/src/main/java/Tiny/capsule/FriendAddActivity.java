package Tiny.capsule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import Tiny.capsule.http.AddFriendRequest;
import Tiny.capsule.http.FriendRequestRequest;
import Tiny.capsule.http.FriendRequestResult;
import Tiny.capsule.http.SimpleResult;
import Tiny.capsule.model.FriendRequest;
import Tiny.capsule.model.Global;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FriendAddActivity extends AppCompatActivity {

    private EditText account;
    private Button button ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_add);
        account = findViewById(R.id.editText_add_friend);

        button = findViewById(R.id.btn_add_friend);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

    }


}
