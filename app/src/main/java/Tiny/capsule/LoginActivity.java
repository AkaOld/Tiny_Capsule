package Tiny.capsule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import Tiny.capsule.http.LoginRequest;
import Tiny.capsule.http.RegisterResult;
import Tiny.capsule.http.SimpleResult;
import Tiny.capsule.model.Global;
import Tiny.capsule.model.RealmUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.basgeekball.awesomevalidation.ValidationStyle.UNDERLABEL;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    final AwesomeValidation mAwesomeValidation= new AwesomeValidation(UNDERLABEL);
    private static final int REQUEST_SIGNUP = 0;
    private Tencent mTencent;
    BaseUiListener mIUiListener;
    UserInfo mUserInfo;

    @BindView(R.id.username) EditText Username;
    @BindView(R.id.password) EditText Password;
    @BindView(R.id.login_button) Button log_button;
    @BindView(R.id.textView3) TextView ToRegister;
    @BindView(R.id.imageView4) ImageView qqButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mTencent = Tencent.createInstance("101534454", this.getApplicationContext());

        mAwesomeValidation.setContext(this);  // mandatory for UNDERLABEL style
        mAwesomeValidation.setUnderlabelColorByResource(android.R.color.holo_red_light); // optional for UNDERLABEL style
        mAwesomeValidation.setUnderlabelColor(ContextCompat.getColor(this, android.R.color.holo_red_light)); // optional for UNDERLABEL style
        mAwesomeValidation.addValidation(this, R.id.username, "^[0-9a-zA_Z]+$", R.string.error_username);
        //mAwesomeValidation.addValidation(this, R.id.password, "^\\d{6,}$", R.string.error_password);

        // Set up the login form.
        ToRegister.setOnClickListener(new OnClickListener() {     //注册跳转
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        log_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        qqButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mIUiListener = new BaseUiListener();
                mTencent.login(LoginActivity.this, "all", mIUiListener);  //all表示获取所有权限
            }
        });
    }

    public void login(){
        Log.d(TAG, "Login");

        if (!validate()) {
            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
            log_button.setEnabled(true);
            return;
        }

        log_button.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();


        //HttpPost
        String username = Username.getText().toString();
        String password = Password.getText().toString();
        loginRequest(username, password);
        progressDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }else if(requestCode == Constants.REQUEST_LOGIN){
            Tencent.onActivityResultData(requestCode,resultCode,data,mIUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//
//        Tencent.onActivityResultData(requestCode, resultCode, data, new BaseUiListener());
//
//        if(requestCode == Constants.REQUEST_API) {
//            if(resultCode == Constants.REQUEST_LOGIN) {
//                Tencent.handleResultData(data, new BaseUiListener());
//            }
//        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        log_button.setEnabled(true);
        finish();
    }

       public boolean validate() {
        boolean valid = true;
        valid = mAwesomeValidation.validate();
        return valid;
    }

    private class BaseUiListener implements IUiListener{
        @Override
        public void onComplete(Object response) {
            Toast.makeText(LoginActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "response:" + response);
            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken,expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getApplicationContext(),qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        Log.e(TAG,"登录成功"+response.toString());
                    }

                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG,"登录失败"+uiError.toString());
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG,"登录取消");

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onError(UiError uiError) {
            Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
        }
    }

    public void loginRequest(final String username, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Global.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LoginRequest request = retrofit.create(LoginRequest.class);
        Call<RegisterResult> call = request.login(username, password);
        call.enqueue(new Callback<RegisterResult>() {
            @Override
            public void onResponse(Call<RegisterResult> call, Response<RegisterResult> response) {
                if(response.body().getStatus() == 1){
                    Toast.makeText(getApplicationContext(), "successful login" , Toast.LENGTH_LONG).show();
                    onLoginSuccess();
                    Global.currentUsername = response.body().getUsername();
                    LoginStatus status = RealmUtil.getRealm().where(LoginStatus.class).findFirst();
                    if(status !=null){
                        RealmUtil.getRealm().beginTransaction();
                        status.deleteFromRealm();
                        LoginStatus status2 = RealmUtil.getRealm().createObject(LoginStatus.class);
                        status2.setUsername(response.body().getUsername());
                        RealmUtil.getRealm().copyFromRealm(status2);
                        RealmUtil.getRealm().commitTransaction();
                    }else{
                        RealmUtil.getRealm().beginTransaction();
                        LoginStatus status2 = RealmUtil.getRealm().createObject(LoginStatus.class);
                        status2.setUsername(response.body().getUsername());
                        RealmUtil.getRealm().copyFromRealm(status2);
                        RealmUtil.getRealm().commitTransaction();
                    }

                    Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else if (response.body().getStatus() == -1){
                    Toast.makeText(getApplicationContext(), "failed login" , Toast.LENGTH_LONG).show();
                }
                else if (response.body().getStatus() == -2){
                    System.out.println("登录失败");
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