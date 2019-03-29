package com.sergey.zhuravlev.auction.client.dto.auth;

import lombok.Setter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@Setter
@RequiredArgsConstructor
public class AuthResponseDto {

    private String accessToken;
    private String tokenType;

    public AuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
        this.tokenType = "Bearer";
    }
}
