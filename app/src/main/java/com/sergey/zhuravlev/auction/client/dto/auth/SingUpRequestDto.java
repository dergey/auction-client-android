package com.sergey.zhuravlev.auction.client.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sergey.zhuravlev.auction.client.dto.AccountRequestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SingUpRequestDto extends AccountRequestDto {

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "password")
    private String password;

}
