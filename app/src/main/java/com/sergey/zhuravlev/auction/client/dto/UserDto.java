package com.sergey.zhuravlev.auction.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "phone")
    private String phone;

    @JsonProperty(value = "account")
    private AccountResponseDto account;

}
