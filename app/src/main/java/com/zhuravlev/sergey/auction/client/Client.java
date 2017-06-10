package com.zhuravlev.sergey.auction.client;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.zhuravlev.sergey.auction.Constants;
import com.zhuravlev.sergey.auction.MainActivity;
import com.zhuravlev.sergey.auction.dto.User;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

//TODO Дать более емкое название классу
public class Client {
    private static Client client;
    private Context context;
    private User user;

    private String host;
    private String port;
    private String loginPath = "login";
    private String logoutPath = "logout";

    private static String COOKIE_TEXT = "Cookie";
    private String sessionid;

    private RestTemplate template;

    public static void createClient(Context context) {
        client = new Client();
        client.setContext(context);
        client.setHost("192.168.100.2");
        client.setPort("8080");
    }

    public static Client getClient() {
        return client;
    }

    private Client() {
        //TODO автоматическая загрузка
        sessionid = "";
        template = new RestTemplate();
        template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    public void setContext(Context context) {
        this.context = context;
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
        ((MainActivity) context).loadUser();
    }

    public void logout(){
        if (IsAuthorized()) {
            SharedPreferences sPref = ((AppCompatActivity) context).getPreferences(Context.MODE_PRIVATE);
            Editor ed = sPref.edit();
            user = null;
            sessionid = "";
            ed.putString(COOKIE_TEXT, "");
            ed.commit();
            Log.d("Auction.Login", "Выход!");
        } else Log.d("Auction.Login", "Нечего сохранять!");
    }

    public void registration(User user) {

    }

    public void loadUser() {
        new LoadUserTask().execute();
    }

    public ResponseEntity test(){
        ResponseEntity response = template.exchange(Constants.HOST + "test", HttpMethod.GET, getHttpEntityWithCookie(), String.class);
        Log.d("Auction.Login", "Ответ от /test : " + response.getBody());
       // if (IsAuthorized()) loadUser();
        return response;
    }

    private HttpEntity<MultiValueMap<String,String>> getHttpEntityWithCookie(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionid);
        return new HttpEntity<>(null, headers);
    }

    public User getUser() {
        return user;
    }

    public void saveSessionID() {
        if (IsAuthorized()) {
            SharedPreferences sPref = ((AppCompatActivity) context).getPreferences(Context.MODE_PRIVATE);
            Editor ed = sPref.edit();
            ed.putString(COOKIE_TEXT, sessionid);
            ed.commit();
            Log.d("Auction.Login", "Сохранено!");
        } else Log.d("Auction.Login", "Нечего сохранять!");
    }

    public void loadSessionID() {
        SharedPreferences sPref = ((AppCompatActivity) context).getPreferences(Context.MODE_PRIVATE);
        sessionid = sPref.getString(COOKIE_TEXT, "");
        Log.d("Auction.Login", "Загружено " + sessionid);
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

    private class LoadUserTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground (Void... voids) {
            try {
                ResponseEntity response = template.exchange(Constants.HOST + "profile", HttpMethod.GET, getHttpEntityWithCookie(), User.class);
                user = (User) response.getBody();
                Log.d("Auction.Login", "Имя пользователя " + user.getFirstname());
            } catch (HttpClientErrorException e) {
                Log.d("Auction.Login", "Ошибка ответа сервера, код: " + e.getStatusCode());
                cancel(true);
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            ((MainActivity) context).loadUser();
        }
    }

}
