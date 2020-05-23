package com.sergey.zhuravlev.auction.client.api;

import com.sergey.zhuravlev.auction.client.dto.UserDto;
import com.sergey.zhuravlev.auction.client.dto.auth.AuthResponseDto;
import com.sergey.zhuravlev.auction.client.dto.auth.AuthorizationCodeDto;
import com.sergey.zhuravlev.auction.client.dto.auth.LoginRequestDto;
import com.sergey.zhuravlev.auction.client.dto.auth.SingUpRequestDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AuthEndpoint {

    @POST("auth/authenticate")
    Call<AuthResponseDto> authenticate(@Body LoginRequestDto loginRequestDto);

    @POST("auth/authenticate/{registrationId}")
    Call<AuthResponseDto> authenticate(@Path("registrationId") String registrationId, @Body AuthorizationCodeDto loginRequestDto);

    @POST("auth/register")
    Call<UserDto> register(@Body SingUpRequestDto singUpRequestDto);

}