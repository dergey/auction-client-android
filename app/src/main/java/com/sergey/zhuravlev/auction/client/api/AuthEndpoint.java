package com.sergey.zhuravlev.auction.client.api;

import com.sergey.zhuravlev.auction.client.dto.auth.AuthResponseDto;
import com.sergey.zhuravlev.auction.client.dto.auth.LoginRequestDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthEndpoint {

    @POST("auth/authenticate")
    Call<AuthResponseDto> authenticate(@Body LoginRequestDto loginRequestDto);

}