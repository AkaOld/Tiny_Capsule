package Tiny.capsule.http;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CapsuleInviteRequest {
    @POST("capsuleInvite")
    @FormUrlEncoded
    Call<SimpleResult> capsuleInvite(@Field("request") String request, @Field("receive") String receive, @Field("capsuleId") int capsuleId);
}
