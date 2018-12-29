package Tiny.capsule.http;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegisterRequest {
    @POST("register")
    @FormUrlEncoded
    Call<RegisterResult> register(@Field("loginName") String loginName, @Field("password") String password, @Field("nickname") String nickname, @Field("thirdParty") String thirdParty);
}
