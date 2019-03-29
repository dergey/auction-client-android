package com.sergey.zhuravlev.auction.client.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {

    @JsonProperty(value = "timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date timestamp;

    @JsonProperty(value = "status")
    private Integer status;

    @JsonProperty(value = "error")
    private String error;

    @JsonProperty(value = "message")
    private String message;

    @JsonProperty(value = "path")
    private String path;

}
