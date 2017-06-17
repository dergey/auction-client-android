package com.zhuravlev.sergey.auction.client;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

class TaskManager extends AsyncTask<Void, Void, ResponseEntity> {

    private Client client;
    private String appPath;
    private HttpMethod httpMethod;
    private HttpEntity httpEntity;
    private Class objectClass;

    TaskManager(String appPath, HttpMethod httpMethod, HttpEntity httpEntity, Class objectClass) {
        this.client = Client.getClient();
        this.appPath = appPath;
        this.httpMethod = httpMethod;
        this.httpEntity = httpEntity;
        this.objectClass = objectClass;
    }

    @Override
    protected ResponseEntity doInBackground(Void[] voids) {
        ResponseEntity response = null;
        try {
            Log.i("Auction.TaskManager", "Запрос: Метод[" + httpMethod + "], Путь[" + appPath + "], Тело запроса " + httpEntity + ", Запрашиваемый объект [" + objectClass + "]");
            response = client.getTemplate().exchange(client.applicationUrl(appPath), httpMethod, httpEntity, objectClass);
            Log.i("Auction.TaskManager", "Ответ: " + response);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            Log.e("Auction.TaskManager", "Ошибка [" + e.getStatusCode() + "]:\n" + e.getResponseBodyAsString());
            cancel(true);
        }
        return response;
    }


}
