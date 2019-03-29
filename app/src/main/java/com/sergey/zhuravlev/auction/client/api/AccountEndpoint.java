package com.sergey.zhuravlev.auction.client.api;

import com.sergey.zhuravlev.auction.client.dto.AccountRequestDto;
import com.sergey.zhuravlev.auction.client.dto.AccountResponseDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AccountEndpoint {

    @GET("api/account/{username}")
    Call<AccountResponseDto> get(@Header("Authorization") String bearer, @Path("username") String username);

    @POST("/api/account")
    Call<AccountResponseDto> createUpdate(@Header("Authorization") String bearer, @Body AccountRequestDto requestDto);

}
