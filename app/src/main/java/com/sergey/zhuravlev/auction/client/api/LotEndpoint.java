package com.sergey.zhuravlev.auction.client.api;

import com.sergey.zhuravlev.auction.client.dto.PageDto;
import com.sergey.zhuravlev.auction.client.dto.ResponseLotDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface LotEndpoint {

    @GET("api/lots")
    Call<PageDto<ResponseLotDto>> page(@Header("Authorization") String bearer,
                                       @Query(value = "category") String category,
                                       @Query(value = "owner") String owner,
                                       @Query(value = "title") String title,
                                       @Query(value = "page") Integer page,
                                       @Query(value = "size") Integer size);



}
