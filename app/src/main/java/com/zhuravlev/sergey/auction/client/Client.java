package com.zhuravlev.sergey.auction.client;

import android.content.Context;
import android.util.Log;

import com.zhuravlev.sergey.auction.Constants;
import com.zhuravlev.sergey.auction.dto.User;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

//TODO Дать более емкое название классу
public class Client {

    User user;

    private String host;
    private String port;
    private String loginPath = "login";
    private String logoutPath = "logout";

    private String sessionid;

    private RestTemplate template;

    public static Client getInstance() {
        Client client = new Client();
        client.setHost("192.168.100.2");
        client.setPort("8080");
        return client;
    }

    private Client() {
        //TODO автоматическая загрузка
        sessionid = "";
        template = new RestTemplate();
        template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    public boolean IsAuthorized(){
        return !sessionid.isEmpty();
    }

    public void login(String username, String password){
        //-----------------------------------------------------------------------------------------------------
        Log.d("Auction.Login", "Имя пользователя и пароль: " + username + ":" + password);
        //-----------------------------------------------------------------------------------------------------
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("username", username);
            map.add("password", password);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, null);
        try {
            ResponseEntity response = template.postForEntity(loginUrl(), request, String.class);
            sessionid = response.getHeaders().get("Set-Cookie").get(0).split(";")[0];
            //-----------------------------------------------------------------------------------------------------
            Log.d("Auction.Login", "Код ответа: " + response.getStatusCode());
            Log.d("Auction.Login", "Заголовок Location ответа: " + response.getHeaders().get("Location").get(0));
            Log.d("Auction.Login", "Куки ответа: " + sessionid);
            //-----------------------------------------------------------------------------------------------------
        } catch (Exception ignored) {
            Log.d("Auction.Login", "Ошибка подключения к серверу - " + ignored.getLocalizedMessage());
        }
    }

    public void registration(User user) {
    }

    public ResponseEntity test(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionid);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
        ResponseEntity response = template.exchange(Constants.HOST + "test", HttpMethod.GET, request, String.class);
        Log.d("Auction.Login", "Ответ от /test : " + response.getBody());
        return response;
    }

    public String serverUrl(){
        return "http://" + host + ":" + port;
    }

    public String applicationUrl(String applicationPath) {
        return serverUrl() + "/" + applicationPath;
    }

    public String loginUrl(){
        return applicationUrl(loginPath);
    }

    public String logoutUrl(){
        return applicationUrl(logoutPath);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getLoginPath() {
        return loginPath;
    }

    public void setLoginPath(String loginPath) {
        this.loginPath = loginPath;
    }

    public String getLogoutPath() {
        return logoutPath;
    }

    public void setLogoutPath(String logoutPath) {
        this.logoutPath = logoutPath;
    }

}
