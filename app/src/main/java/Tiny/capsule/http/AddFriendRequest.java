package Tiny.capsule.http;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AddFriendRequest {
    @POST("addFriend")
    @FormUrlEncoded
    Call<SimpleResult> addFriend(@Field("request") String request, @Field("receive") String receive);
}
