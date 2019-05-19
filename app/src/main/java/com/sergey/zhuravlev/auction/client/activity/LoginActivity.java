package com.sergey.zhuravlev.auction.client.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.sergey.zhuravlev.auction.client.R;
import com.sergey.zhuravlev.auction.client.client.Client;
import com.sergey.zhuravlev.auction.client.dto.ErrorDto;
import com.sergey.zhuravlev.auction.client.dto.auth.AuthResponseDto;
import com.sergey.zhuravlev.auction.client.exception.ErrorResponseException;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Client client;

    private TextView usernameView;
    private EditText passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        //toolbar.setTitle(R.string.login_activity_title);
        //setSupportActionBar(toolbar);

        client = Client.getInstance();

        usernameView = findViewById(R.id.email);
        passwordView = findViewById(R.id.password);

        Button registrationFormButton = findViewById(R.id.registration);
        registrationFormButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), RegistrationActivity.class);
//                startActivityForResult(intent, 1);
            }
        });
        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mGoogleSignInButton = findViewById(R.id.sign_in_with_google_button);
        mGoogleSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLoginWithGoogle();
            }
        });


    }

    private void attemptLoginWithGoogle() {
        usernameView.setError(null);
        passwordView.setError(null);

        AccountManager am = AccountManager.get(this);
        Bundle options = new Bundle();
        Account[] account = am.getAccountsByType("com.google");
        if (account.length > 0) {
            am.getAuthToken(account[0], "email profile", options, this,
                    new AccountManagerCallback<Bundle>() {
                        @Override
                        public void run(AccountManagerFuture<Bundle> accountManagerFuture) {
                            try {
                                String token = accountManagerFuture.getResult().getString(AccountManager.KEY_AUTHTOKEN);
                                Log.d("TOKEN", " token " + token);
                            } catch (OperationCanceledException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (AuthenticatorException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Handler());
        } else {
            Log.e("OAUTH", "ERROR ACCOUNT NOT FOUND");
        }
    }

    private void attemptLogin() {
        usernameView.setError(null);
        passwordView.setError(null);

        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
            usernameView.setError(getString(R.string.error_field_required));
            focusView = usernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            usernameView.setError(getString(R.string.error_invalid_email));
            focusView = usernameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            final ProgressDialog dialog =
                    ProgressDialog.show(this, "", getString(R.string.loadmessage_signin),
                            true);
            client.authenticate(username, password, new Callback<AuthResponseDto>() {
                @Override
                public void onResponse(@NonNull Call<AuthResponseDto> call, @NonNull Response<AuthResponseDto> response) {
                    Log.d("auction", "AccessToken " + response.body().getAccessToken());
                    dialog.dismiss();
                    setResult(RESULT_OK, new Intent());
                    finish();
                }

                @Override
                public void onFailure(@NonNull Call<AuthResponseDto> call, @NonNull Throwable t) {
                    if (t instanceof ErrorResponseException) {
                        ErrorDto errorDto = ((ErrorResponseException) t).getErrorDto();
                        serverError(errorDto.getMessage());
                    } else if (t instanceof SocketTimeoutException) {
                        Toast.makeText(LoginActivity.this, "Сервер недоступен", Toast.LENGTH_LONG).show();
                    } else if (t instanceof UnrecognizedPropertyException) {
                        Toast.makeText(LoginActivity.this, "API обновилось. Пожалуйста, обновите приложение!", Toast.LENGTH_LONG).show();
                        Log.d("Auction.LoginActivity", "Login exception!\n" + Log.getStackTraceString(t));
                    } else {
                        Toast.makeText(LoginActivity.this, "Неизвестная ошибка подключения", Toast.LENGTH_LONG).show();
                        Log.d("Auction.LoginActivity", "Login exception!\n" + Log.getStackTraceString(t));
                    }
                    dialog.dismiss();
                }
            });
        }
    }

    private boolean isUsernameValid(String username) {
        return username.length() > 5;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 7;
    }

    public void serverError(String string) {
        usernameView.setError(string);
        usernameView.requestFocus();
    }

}

