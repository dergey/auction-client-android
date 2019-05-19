package com.sergey.zhuravlev.auction.client.api;

import com.sergey.zhuravlev.auction.client.dto.CategoryDto;
import com.sergey.zhuravlev.auction.client.dto.PageDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface CategoryEndpoint {

    @GET("api/categories")
    Call<PageDto<CategoryDto>> page(@Header("Authorization") String bearer);

    @GET("api/categories")
    Call<PageDto<CategoryDto>> page(@Header("Authorization") String bearer, @Query(value = "page") Integer page, @Query(value = "size") Integer size);

}
