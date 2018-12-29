package Tiny.capsule.http;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CapsuleRequestRequest {
    @POST("capsuleRequest")
    @FormUrlEncoded
    Call<CapsuleRequestResult> getCapsuleRequests(@Field("username") String username);
}
