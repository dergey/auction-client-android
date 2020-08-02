package com.sergey.zhuravlev.auction.client.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.sergey.zhuravlev.auction.client.R;
import com.sergey.zhuravlev.auction.client.client.Client;
import com.sergey.zhuravlev.auction.client.constrain.RequestActivityCodes;
import com.sergey.zhuravlev.auction.client.dto.AccountRequestDto;
import com.sergey.zhuravlev.auction.client.dto.ErrorDto;
import com.sergey.zhuravlev.auction.client.dto.UserDto;
import com.sergey.zhuravlev.auction.client.dto.auth.AuthResponseDto;
import com.sergey.zhuravlev.auction.client.dto.auth.SingUpRequestDto;
import com.sergey.zhuravlev.auction.client.exception.ErrorResponseException;

import java.net.SocketTimeoutException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sergey.zhuravlev.auction.client.constrain.RequestActivityCodes.ACCOUNT_REGISTRATION_REQUEST;

public class RegistrationActivity extends AppCompatActivity {

    private EditText aboutMeEdit;
    private EditText confirmPasswordEdit;
    private EditText emailEdit;
    private EditText firstnameEdit;
    private EditText lastnameEdit;
    private EditText passwordEdit;

    private EditText usernameEdit;

    private int requestCode = 0;

    private boolean haveError() {
        boolean containError = false;
        boolean accountRegistration = requestCode == ACCOUNT_REGISTRATION_REQUEST;
        if (TextUtils.isEmpty(this.usernameEdit.getText().toString())) {
            this.usernameEdit.setError(getString(R.string.error_field_required));
            this.usernameEdit.requestFocus();
            containError = true;
        } else if (this.usernameEdit.getText().toString().length() < 6) {
            this.usernameEdit.setError(getString(R.string.error_field_smaller_than, 6));
            this.usernameEdit.requestFocus();
            containError = true;
        } else if (this.usernameEdit.getText().toString().length() > 32) {
            this.usernameEdit.setError(getString(R.string.error_field_bigger_than, 32));
            this.usernameEdit.requestFocus();
            containError = true;
        }
        if (!accountRegistration && TextUtils.isEmpty(this.passwordEdit.getText().toString())) {
            this.passwordEdit.setError(getString(R.string.error_field_required));
            this.passwordEdit.requestFocus();
        } else if (!accountRegistration && this.passwordEdit.getText().toString().length() < 6) {
            this.passwordEdit.setError(getString(R.string.error_field_smaller_than, 6));
            this.passwordEdit.requestFocus();
            containError = true;
        } else if (!accountRegistration && this.passwordEdit.getText().toString().length() > 32) {
            this.passwordEdit.setError(getString(R.string.error_field_bigger_than, 32));
            this.passwordEdit.requestFocus();
            containError = true;
        } else if (!accountRegistration && !this.passwordEdit.getText().toString().equals(this.confirmPasswordEdit.getText().toString())) {
            this.confirmPasswordEdit.setError(getString(R.string.error_password_not_match));
            this.confirmPasswordEdit.requestFocus();
            containError = true;
        }
        if (!accountRegistration && TextUtils.isEmpty(this.emailEdit.getText().toString())) {
            this.emailEdit.setError(getString(R.string.error_field_required));
            this.emailEdit.requestFocus();
            containError = true;
        }
        if (TextUtils.isEmpty(this.firstnameEdit.getText().toString())) {
            this.firstnameEdit.setError(getString(R.string.error_field_required));
            this.firstnameEdit.requestFocus();
            containError = true;
        }
        if (TextUtils.isEmpty(this.lastnameEdit.getText().toString())) {
            this.lastnameEdit.setError(getString(R.string.error_field_required));
            this.lastnameEdit.requestFocus();
            containError = true;
        }
        return containError;
    }

    private SingUpRequestDto parseSingUpForm() {
        SingUpRequestDto singUpRequestDto = new SingUpRequestDto(this.emailEdit.getText().toString(), this.passwordEdit.getText().toString());
        singUpRequestDto.setUsername(this.usernameEdit.getText().toString());
        singUpRequestDto.setFirstname(this.firstnameEdit.getText().toString());
        singUpRequestDto.setLastname(this.lastnameEdit.getText().toString());
        singUpRequestDto.setBio(this.aboutMeEdit.getText().toString());
        Log.d("Auction.Registration", "User " + singUpRequestDto.toString());
        return singUpRequestDto;
    }

    private AccountRequestDto parseAccountForm() {
        AccountRequestDto accountRequestDto = new AccountRequestDto();
        accountRequestDto.setUsername(this.usernameEdit.getText().toString());
        accountRequestDto.setFirstname(this.firstnameEdit.getText().toString());
        accountRequestDto.setLastname(this.lastnameEdit.getText().toString());
        accountRequestDto.setBio(this.aboutMeEdit.getText().toString());
        Log.d("Auction.Registration", "Account " + accountRequestDto.toString());
        return accountRequestDto;
    }

    void initComponents() {
        this.emailEdit = findViewById(R.id.edit_email);
        this.usernameEdit = findViewById(R.id.edit_username);
        this.passwordEdit = findViewById(R.id.edit_password);
        this.confirmPasswordEdit = findViewById(R.id.edit_password_confirm);
        this.firstnameEdit = findViewById(R.id.edit_first_name);
        this.lastnameEdit = findViewById(R.id.edit_last_name);
        this.aboutMeEdit = findViewById(R.id.edit_bio);
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_registration);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        initComponents();
        extractExtras();
    }

    private void extractExtras() {
        int requestCode = getIntent().getExtras().getInt(RequestActivityCodes.REQUEST_ACTIVITY_KEY);
        this.requestCode = requestCode;
        if (requestCode == ACCOUNT_REGISTRATION_REQUEST) {
            UserDto userDto = (UserDto) getIntent().getExtras().getParcelable("user");
            emailEdit.setText(userDto.getEmail());
            emailEdit.setEnabled(false);
            passwordEdit.setEnabled(false);
            passwordEdit.setText("123456");
            confirmPasswordEdit.setEnabled(false);
            confirmPasswordEdit.setText("123456");
        }
    }

    public boolean onCreateOptionsMenu(Menu paramMenu) {
        getMenuInflater().inflate(R.menu.menu_ok, paramMenu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.ok) {
            if (!haveError()) {
                switch (requestCode) {
                    case ACCOUNT_REGISTRATION_REQUEST:
                        Client.getInstance().createUpdateAccount(parseAccountForm(), (response) -> {
                            setResult(RESULT_OK, new Intent());
                            finish();
                        });
                        break;
                    default:
                        ProgressDialog dialog = ProgressDialog.show(this, "", getString(R.string.progress_dialog_registration), true);
                        Client.getInstance().register(parseSingUpForm(), new Callback<AuthResponseDto>() {
                            @Override
                            public void onResponse(Call<AuthResponseDto> call, Response<AuthResponseDto> response) {
                                dialog.dismiss();
                                setResult(RESULT_OK, new Intent());
                                finish();
                            }

                            @Override
                            public void onFailure(Call<AuthResponseDto> call, Throwable t) {
                                if (t instanceof ErrorResponseException) {
                                    ErrorDto errorDto = ((ErrorResponseException) t).getErrorDto();
                                    serverError(errorDto.getMessage());
                                    Log.d("Auction.Registration", "Registration exception!" + errorDto.getMessage());
                                } else if (t instanceof SocketTimeoutException) {
                                    Toast.makeText(RegistrationActivity.this, getString(R.string.connection_error_message), Toast.LENGTH_LONG).show();
                                } else if (t instanceof UnrecognizedPropertyException) {
                                    Toast.makeText(RegistrationActivity.this, getString(R.string.api_outdate_message), Toast.LENGTH_LONG).show();
                                    Log.d("Auction.Registration", "Registration exception!\n" + Log.getStackTraceString(t));
                                } else {
                                    Toast.makeText(RegistrationActivity.this, getString(R.string.unknown_error_message), Toast.LENGTH_LONG).show();
                                    Log.d("Auction.Registration", "Registration exception!\n" + Log.getStackTraceString(t));
                                }
                                dialog.dismiss();
                            }
                        });
                        return true;
                }
            }
        } else {
            setResult(RESULT_CANCELED, new Intent());
            finish();
            return true;
        }
        return false;
    }

    public void serverError(String errorMessage) {
        switch (errorMessage) {
            case "UsernameAlreadyRegistered":
                this.usernameEdit.setError(getString(R.string.username_already_taken_error));
                this.usernameEdit.requestFocus();
                break;
            case "EmailAlreadyRegistered":
                this.emailEdit.setError(getString(R.string.email_already_taken_error));
                this.emailEdit.requestFocus();
                break;
            default:
                Toast.makeText(RegistrationActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                break;
        }
    }
}