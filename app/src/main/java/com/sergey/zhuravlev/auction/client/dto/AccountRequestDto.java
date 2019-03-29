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
public class AccountRequestDto {

    @JsonProperty(value = "username")
    private String username;

    @JsonProperty(value = "photo")
    private String photo;

    @JsonProperty(value = "firstname")
    private String firstname;

    @JsonProperty(value = "lastname")
    private String lastname;

    @JsonProperty(value = "bio")
    private String bio;

}
