package com.sergey.zhuravlev.auction.client.api;

import com.sergey.zhuravlev.auction.client.dto.CategoryDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface CategoryEndpoint {

    @GET("api/categories")
    Call<List<CategoryDto>> list(@Header("Authorization") String bearer);

}
