package com.sergey.zhuravlev.auction.client.api;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ImageEndpoint {

    @Multipart
    @POST("api/images")
    Call<Void> upload(@Header("Authorization") String bearer, @Part MultipartBody.Part file);

    @GET("api/images/{name}")
    Call<ResponseBody> download(@Path("name") String name);

}
