package com.sergey.zhuravlev.auction.client.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.Task;
import com.sergey.zhuravlev.auction.client.R;
import com.sergey.zhuravlev.auction.client.client.Client;
import com.sergey.zhuravlev.auction.client.constrain.RequestActivityCodes;
import com.sergey.zhuravlev.auction.client.dto.ErrorDto;
import com.sergey.zhuravlev.auction.client.dto.auth.AuthResponseDto;
import com.sergey.zhuravlev.auction.client.exception.ErrorResponseException;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Client client;

    private TextView usernameView;
    private EditText passwordView;

    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        client = Client.getInstance();
        googleSignInClient = Client.getGoogleSignInClient();

        usernameView = findViewById(R.id.email);
        passwordView = findViewById(R.id.password);

        Button registrationFormButton = findViewById(R.id.registration);
        registrationFormButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RegistrationActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        ImageButton mGoogleSignInButton = findViewById(R.id.auth_with_google_button);
        mGoogleSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RequestActivityCodes.GOOGLE_SIGN_IN_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestActivityCodes.GOOGLE_SIGN_IN_REQUEST:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
                break;
            case RequestActivityCodes.ACCOUNT_REGISTRATION_REQUEST:
                if (resultCode == RESULT_OK) {
                    setResult(RESULT_OK, new Intent());
                    finish();
                }
                break;
        }
    }

    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        usernameView.setError(null);
        passwordView.setError(null);
        client.authenticate(completedTask, new Callback<AuthResponseDto>() {
            @Override
            public void onResponse(Call<AuthResponseDto> call, Response<AuthResponseDto> response) {
                client.getCurrentUser(userDto -> {
                    if (userDto.isIncomplete()) {
                        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                        intent.putExtra(RequestActivityCodes.REQUEST_ACTIVITY_KEY, RequestActivityCodes.ACCOUNT_REGISTRATION_REQUEST);
                        intent.putExtra("user", userDto);
                        startActivityForResult(intent, RequestActivityCodes.ACCOUNT_REGISTRATION_REQUEST);
                    } else {
                        setResult(RESULT_OK, new Intent());
                        finish();
                    }
                });
            }

            @Override
            public void onFailure(Call<AuthResponseDto> call, Throwable t) {
                if (t instanceof ErrorResponseException) {
                    ErrorDto errorDto = ((ErrorResponseException) t).getErrorDto();
                    serverError(errorDto.getMessage());
                } else if (t instanceof SocketTimeoutException) {
                    Toast.makeText(LoginActivity.this, getString(R.string.connection_error_message), Toast.LENGTH_LONG).show();
                } else if (t instanceof UnrecognizedPropertyException) {
                    Toast.makeText(LoginActivity.this, getString(R.string.api_outdate_message), Toast.LENGTH_LONG).show();
                    Log.d("Auction.Registration", "Registration exception!\n" + Log.getStackTraceString(t));
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.unknown_error_message), Toast.LENGTH_LONG).show();
                    Log.d("Auction.Registration", "Registration exception!\n" + Log.getStackTraceString(t));
                }
            }
        });
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
            final ProgressDialog dialog = ProgressDialog.show(this, "", getString(R.string.progress_dialog_registration), true);
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
                        Toast.makeText(LoginActivity.this, getString(R.string.connection_error_message), Toast.LENGTH_LONG).show();
                    } else if (t instanceof UnrecognizedPropertyException) {
                        Toast.makeText(LoginActivity.this, getString(R.string.api_outdate_message), Toast.LENGTH_LONG).show();
                        Log.d("Auction.Registration", "Registration exception!\n" + Log.getStackTraceString(t));
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.unknown_error_message), Toast.LENGTH_LONG).show();
                        Log.d("Auction.Registration", "Registration exception!\n" + Log.getStackTraceString(t));
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

