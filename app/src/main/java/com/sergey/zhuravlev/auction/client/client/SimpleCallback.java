package com.sergey.zhuravlev.auction.client.client;

public interface SimpleCallback<T> {

    void onResponse(T response);

    void onFailure(Throwable t);

}
