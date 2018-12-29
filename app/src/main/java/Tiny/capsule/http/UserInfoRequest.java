package Tiny.capsule.http;

import Tiny.capsule.model.UserInfo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserInfoRequest {
    @POST("userInfo")
    @FormUrlEncoded
    Call<UserInfo> getUserInfo(@Field("username") String username);
}
