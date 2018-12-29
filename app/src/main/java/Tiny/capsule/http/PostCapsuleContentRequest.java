package Tiny.capsule.http;

import Tiny.capsule.model.CapsuleContent;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PostCapsuleContentRequest {
    @POST("postCapsuleContent")
    @FormUrlEncoded
    Call<SimpleResult> postCapsuleContent(@Field("capsuleContent") String capsuleContentJSON);
}
