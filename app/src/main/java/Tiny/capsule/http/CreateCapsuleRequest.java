package Tiny.capsule.http;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CreateCapsuleRequest {
    @POST("createCapsule")
    @FormUrlEncoded
    Call<SimpleResult> createCapsule(@Field("capsule") String capsuleJSON);
}
