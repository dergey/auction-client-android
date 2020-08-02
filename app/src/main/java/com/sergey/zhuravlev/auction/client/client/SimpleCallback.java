package com.sergey.zhuravlev.auction.client.client;

@FunctionalInterface
public interface SimpleCallback<T> {

    void onResponse(T response);

    default void onFailure(Throwable t) {

    }

}
