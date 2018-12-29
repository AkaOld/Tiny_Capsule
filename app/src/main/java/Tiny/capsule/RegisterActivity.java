package Tiny.capsule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;

import Tiny.capsule.http.RegisterRequest;
import Tiny.capsule.http.RegisterResult;
import Tiny.capsule.http.SimpleResult;
import Tiny.capsule.model.Global;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.basgeekball.awesomevalidation.ValidationStyle.UNDERLABEL;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    final AwesomeValidation mAwesomeValidation= new AwesomeValidation(UNDERLABEL);

    @BindView(R.id.username) EditText Username;
    @BindView(R.id.nickname) EditText Nickname;
    @BindView(R.id.password) EditText Password;
    @BindView(R.id.register_button) Button register_button;
    @BindView(R.id.login_button) Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        mAwesomeValidation.setContext(this);  // mandatory for UNDERLABEL style
        mAwesomeValidation.setUnderlabelColorByResource(android.R.color.holo_red_light); // optional for UNDERLABEL style
        mAwesomeValidation.setUnderlabelColor(ContextCompat.getColor(this, android.R.color.holo_red_light)); // optional for UNDERLABEL style
        mAwesomeValidation.addValidation(this, R.id.username, "^[0-9a-zA_Z]+$", R.string.error_username);
        mAwesomeValidation.addValidation(this, R.id.password, "^\\d{6,}$", R.string.error_password);

        register_button.setOnClickListener(new OnClickListener(){
           @Override
           public void onClick(View v) {
                register();
           }
        });
        login_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void register() {
        Log.d(TAG,"Signup");

        if (!validate()) {
            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
            register_button.setEnabled(true);
            return;
        }

        register_button.setEnabled(false);

        String username = Username.getText().toString();
        String nickname = Nickname.getText().toString();
        String password = Password.getText().toString();

//        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Creating Account...");
//        progressDialog.show();

        //HttpPost
        registerRequest(username,nickname,password,null);




    }

    public void onSignupSuccess() {
        register_button.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public boolean validate() {
        boolean valid = true;
        valid = mAwesomeValidation.validate();
        return valid;
    }
    public void registerRequest(final String loginName, String password, String nickname, String thirdParty) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Global.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RegisterRequest request = retrofit.create(RegisterRequest.class);
        Call<RegisterResult> call = request.register(loginName,password,nickname,thirdParty);
        call.enqueue(new Callback<RegisterResult>() {
            @Override
            public void onResponse(Call<RegisterResult> call, Response<RegisterResult> response) {
                if(response.body().getStatus() == 1){
                    System.out.println("注册成功");
                    Global.currentUsername = response.body().getUsername();
                    Intent intent=new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else if(response.body().getStatus() == -1){

                }
            }

            @Override
            public void onFailure(Call<RegisterResult> call, Throwable throwable) {
                System.out.println("连接失败");
                throwable.printStackTrace();
            }
        });
    }


}