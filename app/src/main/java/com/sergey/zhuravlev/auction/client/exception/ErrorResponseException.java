package com.sergey.zhuravlev.auction.client.exception;

import com.sergey.zhuravlev.auction.client.dto.ErrorDto;

import lombok.Getter;

@Getter
public class ErrorResponseException extends Exception {

    private ErrorDto errorDto;
    private final int status;

    public ErrorResponseException(int status) {
        super("Status " + status);
        this.status = status;
    }

    public ErrorResponseException(int status, ErrorDto errorDto) {
        super();
        this.status = status;
        this.errorDto = errorDto;
    }

}
