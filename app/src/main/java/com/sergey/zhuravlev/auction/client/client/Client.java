package com.sergey.zhuravlev.auction.client.client;

import android.content.Context;
import android.net.Uri;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sergey.zhuravlev.auction.client.api.AccountEndpoint;
import com.sergey.zhuravlev.auction.client.api.AuthEndpoint;
import com.sergey.zhuravlev.auction.client.api.CategoryEndpoint;
import com.sergey.zhuravlev.auction.client.api.ImageEndpoint;
import com.sergey.zhuravlev.auction.client.api.LotEndpoint;
import com.sergey.zhuravlev.auction.client.api.UserEndpoint;
import com.sergey.zhuravlev.auction.client.dto.AccountRequestDto;
import com.sergey.zhuravlev.auction.client.dto.AccountResponseDto;
import com.sergey.zhuravlev.auction.client.dto.CategoryDto;
import com.sergey.zhuravlev.auction.client.dto.ErrorDto;
import com.sergey.zhuravlev.auction.client.dto.ResponseLotDto;
import com.sergey.zhuravlev.auction.client.dto.UserDto;
import com.sergey.zhuravlev.auction.client.dto.auth.AuthResponseDto;
import com.sergey.zhuravlev.auction.client.dto.auth.LoginRequestDto;
import com.sergey.zhuravlev.auction.client.exception.ErrorResponseException;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class Client {

    @Getter
    private static final Client instance = new Client();

    public static final String SERVER_URL = "http://192.168.1.59:8080";
    public static final String OAUTH2_REDIRECT_URI = "myandroidapp://oauth2/redirect";

    public static final String GOOGLE_AUTH_URL = SERVER_URL + "/oauth2/authorize/google?redirect_uri=" + OAUTH2_REDIRECT_URI;
    public static final String FACEBOOK_AUTH_URL = SERVER_URL + "/oauth2/authorize/facebook?redirect_uri=" + OAUTH2_REDIRECT_URI;
    public static final String GITHUB_AUTH_URL = SERVER_URL + "/oauth2/authorize/github?redirect_uri=" + OAUTH2_REDIRECT_URI;

    @Setter
    @Getter
    private Context context;

    private ObjectMapper objectMapper;

    private String accessToken;

    @Getter
    private UserDto currentUser;
    private boolean isCurrentUserActual;

    private LotEndpoint lotEndpoints;
    private AuthEndpoint authEndpoint;
    private UserEndpoint userEndpoint;
    private ImageEndpoint imageEndpoint;
    private AccountEndpoint accountEndpoint;
    private CategoryEndpoint categoryEndpoint;

    public void init(Context context) {
        instance.setContext(context);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cookieJar(new DefaultCookieJar());
        builder.followRedirects(false);
        OkHttpClient httpClient = builder.build();

        objectMapper = new ObjectMapper();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        lotEndpoints = retrofit.create(LotEndpoint.class);
        authEndpoint = retrofit.create(AuthEndpoint.class);
        userEndpoint = retrofit.create(UserEndpoint.class);
        imageEndpoint = retrofit.create(ImageEndpoint.class);
        accountEndpoint = retrofit.create(AccountEndpoint.class);
        categoryEndpoint = retrofit.create(CategoryEndpoint.class);
    }

    public void authenticate(String email, String password, final Callback<AuthResponseDto> callback) {
        authEndpoint
                .authenticate(new LoginRequestDto(email, password))
                .enqueue(new ErrorHandlerCallback<>(new Callback<AuthResponseDto>() {
                    @Override
                    public void onResponse(Call<AuthResponseDto> call, Response<AuthResponseDto> response) {
                        accessToken = response.body().getAccessToken();
                        isCurrentUserActual = false;
                        callback.onResponse(call, response);
                    }

                    @Override
                    public void onFailure(Call<AuthResponseDto> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                }));
    }

    public void categoriesList(SimpleCallback<List<CategoryDto>> callback) {
        categoryEndpoint
                .list(getBearer())
                .enqueue(new ErrorHandlerSimpleCallback<>(callback));
    }

    public void imageUpload(Uri filePath, Callback<Void> callback) {
        try {
            InputStream fileInputStream = context.getContentResolver().openInputStream(filePath);
            if (fileInputStream != null) {
                byte[] file = IOUtils.toByteArray(fileInputStream);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", filePath.getPath().substring(filePath.getPath().lastIndexOf('/') + 1), requestFile);
                imageEndpoint.upload(getBearer(), body).enqueue(new ErrorHandlerCallback<>(callback));
            }
        } catch (IOException ignored) {
        }
    }

    public void imageDownload(String name, Callback<ResponseBody> callback) {
        imageEndpoint.download(name).enqueue(new ErrorHandlerCallback<>(callback));
    }

    public void getCurrentUser(final SimpleCallback<UserDto> callback) {
        if (!isCurrentUserActual) {
            userEndpoint.home(getBearer()).enqueue(new Callback<UserDto>() {
                @Override
                public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                    currentUser = response.body();
                    isCurrentUserActual = true;
                    callback.onResponse(currentUser);
                }

                @Override
                public void onFailure(Call<UserDto> call, Throwable t) {
                    callback.onFailure(t);
                }
            });
        } else {
            callback.onResponse(currentUser);
        }
    }

    public void accountUpdatePhoto(String photo, final SimpleCallback<AccountResponseDto> callback) {
        if (currentUser != null && currentUser.getAccount() != null) {
            final AccountResponseDto currentAccount = currentUser.getAccount();
            AccountRequestDto accountRequestDto = new AccountRequestDto(
                    currentAccount.getUsername(),
                    photo,
                    currentAccount.getFirstname(),
                    currentAccount.getLastname(),
                    currentAccount.getBio());
            accountEndpoint.createUpdate(getBearer(), accountRequestDto).enqueue(new ErrorHandlerSimpleCallback<>(new SimpleCallback<AccountResponseDto>() {
                @Override
                public void onResponse(AccountResponseDto response) {
                    currentUser.setAccount(response);
                    isCurrentUserActual = false;
                    callback.onResponse(response);
                }

                @Override
                public void onFailure(Throwable t) {
                    callback.onFailure(t);
                }
            }));
        }
    }

    public void lotList(String category, String owner, String title, Integer pageNumber, Integer pageSize, final Callback<List<ResponseLotDto>> callback) {
        lotEndpoints.list(getBearer(), category, owner, title, pageNumber, pageSize).enqueue(new ErrorHandlerCallback<>(callback));
    }

    private String getBearer() {
        return "Bearer " + accessToken;
    }

    @AllArgsConstructor
    class ErrorHandlerCallback<T> implements Callback<T> {

        private Callback<T> innerCallback;

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if (successfulCode(response.code()) || redirectionCode(response.code())) {
                innerCallback.onResponse(call, response);
            } else {
                try {
                    if (response.errorBody() != null) {
                        String body = response.errorBody().string();
                        ErrorDto errorDto = objectMapper.readValue(body, ErrorDto.class);
                        innerCallback.onFailure(call, new ErrorResponseException(response.code(), errorDto));
                        return;
                    }
                } catch (IOException ignored) {
                }
                innerCallback.onFailure(call, new ErrorResponseException(response.code()));
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            innerCallback.onFailure(call, t);
        }

        private boolean successfulCode(int code) {
            return code >= 200 && code < 300;
        }

        private boolean redirectionCode(int code) {
            return code >= 300 && code < 400;
        }
    }

    @AllArgsConstructor
    class ErrorHandlerSimpleCallback<T> implements Callback<T> {

        private SimpleCallback<T> innerCallback;

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if (successfulCode(response.code()) || redirectionCode(response.code())) {
                innerCallback.onResponse(response.body());
            } else {
                try {
                    if (response.errorBody() != null) {
                        String body = response.errorBody().string();
                        ErrorDto errorDto = objectMapper.readValue(body, ErrorDto.class);
                        innerCallback.onFailure(new ErrorResponseException(response.code(), errorDto));
                        return;
                    }
                } catch (IOException ignored) {
                }
                innerCallback.onFailure(new ErrorResponseException(response.code()));
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            innerCallback.onFailure(t);
        }

        private boolean successfulCode(int code) {
            return code >= 200 && code < 300;
        }

        private boolean redirectionCode(int code) {
            return code >= 300 && code < 400;
        }
    }

}
