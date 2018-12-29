package Tiny.capsule.http;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginRequest {
    @POST("login")
    @FormUrlEncoded
    Call<RegisterResult> login(@Field("username") String username, @Field("password") String password);
}
