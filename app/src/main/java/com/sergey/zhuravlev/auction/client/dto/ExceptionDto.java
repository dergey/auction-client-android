package com.sergey.zhuravlev.auction.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionDto {

    @JsonProperty(value = "timestamp")
    private Date timestamp;
    @JsonProperty(value = "message")
    private String message;
    @JsonProperty(value = "details")
    private String details;

}