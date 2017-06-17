package com.zhuravlev.sergey.auction.client;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zhuravlev.sergey.auction.CategoryActivity;
import com.zhuravlev.sergey.auction.Constants;
import com.zhuravlev.sergey.auction.LoginActivity;
import com.zhuravlev.sergey.auction.LotActivity;
import com.zhuravlev.sergey.auction.MainActivity;
import com.zhuravlev.sergey.auction.ProfileActivity;
import com.zhuravlev.sergey.auction.R;
import com.zhuravlev.sergey.auction.RegistrationActivity;
import com.zhuravlev.sergey.auction.adapter.LotListAdapter;
import com.zhuravlev.sergey.auction.dto.Bet;
import com.zhuravlev.sergey.auction.dto.Category;
import com.zhuravlev.sergey.auction.dto.Lot;
import com.zhuravlev.sergey.auction.dto.User;
import com.zhuravlev.sergey.auction.fragment.CategoriesFragment;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static android.R.attr.id;

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
        client.loadSessionID();
        //client.setHost("zroute.mykeenetic.ru");
        //client.setPort("22080");
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

    /**
     *  Security реализация Клиента
     **/

    public boolean IsAuthorized(){
        return !sessionid.isEmpty();
    }

    public void loadUser(final ProgressDialog dialog) {
        dialog.setTitle(context.getString(R.string.loadmessage_signin));
        TaskManager loadUserTask = new TaskManager("profile", HttpMethod.GET, getHttpEntityWithCookie(), User.class){
            @Override
            protected void onPostExecute(ResponseEntity responseEntity) {
                user = (User) responseEntity.getBody();
                ((MainActivity) context).loadUser();
                dialog.dismiss();
            }
            @Override
            protected void onCancelled() {
                logout();
            }
        };
        loadUserTask.execute();
    }

    public void loadUserLots(Long id, final AppCompatActivity activity){
        TaskManager getLotsTask = new TaskManager("lots?owner=" + id, HttpMethod.GET, new HttpEntity(null), Lot[].class){
                @Override
                protected void onPostExecute(ResponseEntity response) {
                    Lot[] lots = (Lot[]) response.getBody();
                    List<Lot> lotsList = Arrays.asList(lots);
                    ((ProfileActivity) activity).loadLots(lotsList);
                }
            };
            getLotsTask.execute();
    }

    public void login(String username, String password, final ProgressDialog dialog, final LoginActivity loginActivity){
        TaskManager loginTask = new TaskManager(loginPath, HttpMethod.POST, getUsernamePasswordEntity(username, password), String.class){
            @Override
            protected void onPostExecute(ResponseEntity response) {

                if (!response.getHeaders().get("Location").get(0).equals("/login?error")) {
                    loginActivity.finish();
                    setSessionID(response);
                    saveSessionID();
                    loadUser(dialog);

                } else {
                    loginActivity.serverError();
                    dialog.dismiss();
                }
            }
            @Override
            protected void onCancelled() {
                dialog.dismiss();
            }
        };
        loginTask.execute();
    }

    private HttpEntity<MultiValueMap<String, String>> getUsernamePasswordEntity(String username, String password){
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);
        return new HttpEntity<>(map, null);
    }


    public void logout(){
        if (IsAuthorized()) {
            SharedPreferences sPref = ((AppCompatActivity) context).getPreferences(Context.MODE_PRIVATE);
            Editor ed = sPref.edit();
            user = null;
            sessionid = "";
            ed.putString(COOKIE_TEXT, "");
            ed.commit();
            Log.d("Auction.Login", "Выход из аккаунта.");
        } else Log.d("Auction.Login", "Нечего сохранять.");
    }

    public void registration(final RegistrationActivity registrationActivity, final ProgressDialog dialog, final User user) {
        HttpEntity<User> request = new HttpEntity<>(user, getHeaderWithCookie());
        TaskManager createLotTask = new TaskManager("registration", HttpMethod.POST, request, String.class){
            @Override
            protected void onPostExecute(ResponseEntity response) {
                if (response.getBody().equals("\"found\"")) {
                    registrationActivity.finish();
                    setSessionID(response);
                    loadUser(dialog);
                    Intent intent = new Intent();
                    registrationActivity.setResult(Activity.RESULT_OK, intent);
                } else {
                    registrationActivity.serverError((String) response.getBody());
                    dialog.dismiss();
                }
            }
            @Override
            protected void onCancelled(ResponseEntity responseEntity) {
                dialog.dismiss();
            }
        };
        createLotTask.execute();
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

    private HttpHeaders getHeaderWithCookie(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionid);
        return headers;
    }

    private HttpEntity<MultiValueMap<String,String>> getHttpEntityWithCookie(){
        return new HttpEntity<>(null, getHeaderWithCookie());
    }

    /**
     *  Работа с
     *  @link com.zhuravlev.sergey.auction.dto.Lot
     *  реализация Клиента
     **/

    public void getLots(Long categoryId, final AppCompatActivity activity){
        TaskManager getLotsTask = new TaskManager("lots?category="+categoryId, HttpMethod.GET, new HttpEntity(null), Lot[].class){
            @Override
            protected void onPostExecute(ResponseEntity response) {
                Lot[] lots = (Lot[]) response.getBody();
                List<Lot> lotsList = Arrays.asList(lots);
                ((CategoryActivity) activity).loadLots(lotsList);
            }

            @Override
            protected void onCancelled() {
                ((CategoryActivity) activity).showConnectionError();
            }
        };
        getLotsTask.execute();
    }

    public void createLot(Lot lot) {
        HttpEntity<Lot> request = new HttpEntity<>(lot, getHeaderWithCookie());
        TaskManager createLotTask = new TaskManager("lots", HttpMethod.POST, request, Void.class);
        createLotTask.execute();
    }

    public void deleteLot(final LotListAdapter adapter, final List<Lot> lots, final int id){
        TaskManager deleteLotTask = new TaskManager("lots/delete?id=" + lots.get(id).getId(), HttpMethod.POST, getHttpEntityWithCookie(), Void.class){
            @Override
            protected void onPostExecute(ResponseEntity responseEntity) {
                lots.remove(id);
                adapter.notifyDataSetChanged();
            }
        };
        deleteLotTask.execute();
    }

    public void makeBet(Bet bet){
        HttpEntity<Bet> request = new HttpEntity<>(bet, getHeaderWithCookie());
        TaskManager createLotTask = new TaskManager("lots/bet", HttpMethod.POST, request, Void.class);
        createLotTask.execute();
    }

    public User getUser() {
        return user;
    }

    public String serverUrl(){
        return "http://" + host + ":" + port;
    }

    public String applicationUrl(String applicationPath) {
        return serverUrl() + "/" + applicationPath;
    }

    protected RestTemplate getTemplate(){
        return template;
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

    public void getLastBet(final LotActivity lotActivity, final Lot data) {
        TaskManager getLastBetTask = new TaskManager("/lot/" + data.getId() + "/bets/last", HttpMethod.GET, new HttpEntity(null), Bet.class){
            @Override
            protected void onPostExecute(ResponseEntity responseEntity) {
                lotActivity.setLastBet((Bet)responseEntity.getBody());
            }
        };
        getLastBetTask.execute();
    }

    public void getCategories(final CategoriesFragment categoriesFragment) {
        TaskManager getLotsTask = new TaskManager("categories", HttpMethod.GET, new HttpEntity(null), Category[].class){
            @Override
            protected void onPostExecute(ResponseEntity response) {
                Category[] categories = (Category[]) response.getBody();
                List<Category> categoriesList = Arrays.asList(categories);
                categoriesFragment.loadCategory(categoriesList);
            }

            @Override
            protected void onCancelled() {
                ((MainActivity) categoriesFragment.getContext()).showConnectionError();
            }
        };
        getLotsTask.execute();
    }

    public void setSessionID(ResponseEntity response) {
        sessionid = response.getHeaders().get("Set-Cookie").get(0).split(";")[0];
    }
}
