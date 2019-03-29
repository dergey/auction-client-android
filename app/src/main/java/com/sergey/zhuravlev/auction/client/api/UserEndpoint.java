package com.sergey.zhuravlev.auction.client.api;

import com.sergey.zhuravlev.auction.client.dto.UserDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserEndpoint {

    @GET("api/user")
    Call<UserDto> home(@Header("Authorization") String bearer);


    //    @GET("categories")
    //    Call<List<CategoryDto>> allCategories();
    //
    //    @GET("lots/{id}")
    //    Call<ResponseLotDto> getOne(@Path("id") Long id);
    //
    //    @POST("lots")
    //    Call<ResponseLotDto> createLot(@Body RequestLotDto lotDto, @Header("Cookie") String sessionId);
    //
    //    @PUT("lots/{id}")
    //    Call<ResponseLotDto> updateLot(@Path("id") Long id, @Body RequestLotDto lotDto, @Header("Cookie") String sessionId);
    //
    //    @DELETE("lots/{id}")
    //    Call<Void> deleteLot(@Path("id") Long id, @Header("Cookie") String sessionId);
    //
    //    @GET("lots")
    //    Call<List<ResponseLotDto>> getLots(
    //                       @Query(value = "category") Long categoryID,
    //                       @Query(value = "owner") Long ownerID,
    //                       @Query(value = "title") String title);
}
