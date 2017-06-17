package com.zhuravlev.sergey.auction;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhuravlev.sergey.auction.client.Client;
import com.zhuravlev.sergey.auction.dto.User;

public class RegistrationActivity extends AppCompatActivity {

    Toolbar toolbar;
    private EditText  usernameEdit, passwordEdit, confirmPasswordEdit, emailEdit;
    private EditText firstnameEdit, lastnameEdit, aboutMeEdit;

    RegistrationActivity getActivity(){
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initComponents();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.activity_registration_name));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Button button = (Button) findViewById(R.id.regestrationButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!haveError()) {
                    ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                            getString(R.string.loadmessage_registration), true);
                    Client.getClient().registration(getActivity(), dialog, parseForm());
                }
            }
        });

    }

    private boolean haveError() {

        boolean error = false;

        if (TextUtils.isEmpty(usernameEdit.getText().toString())){
            usernameEdit.setError(getString(R.string.error_field_required));
            usernameEdit.requestFocus();
            error = true;
        } else if (usernameEdit.getText().toString().length() < 6
                || usernameEdit.getText().toString().length() > 32) {
            usernameEdit.setError(getString(R.string.error_size));
            usernameEdit.requestFocus();
            error = true;
        }

        if (TextUtils.isEmpty(passwordEdit.getText().toString())){
            passwordEdit.setError(getString(R.string.error_field_required));
            passwordEdit.requestFocus();
        } else if (passwordEdit.getText().toString().length() < 6
                || passwordEdit.getText().toString().length() > 32) {
            passwordEdit.setError(getString(R.string.error_size));
            passwordEdit.requestFocus();
            error = true;
        } else if (!passwordEdit.getText().toString().equals(confirmPasswordEdit.getText().toString())){
            confirmPasswordEdit.setError(getString(R.string.error_password_not_match));
            confirmPasswordEdit.requestFocus();
            error = true;
        }

        if (TextUtils.isEmpty(emailEdit.getText().toString())){
            emailEdit.setError(getString(R.string.error_field_required));
            emailEdit.requestFocus();
            error = true;
        }

        if (TextUtils.isEmpty(firstnameEdit.getText().toString())){
            firstnameEdit.setError(getString(R.string.error_field_required));
            firstnameEdit.requestFocus();
            error = true;
        }

        if (TextUtils.isEmpty(lastnameEdit.getText().toString())){
            lastnameEdit.setError(getString(R.string.error_field_required));
            lastnameEdit.requestFocus();
            error = true;
        }

        return error;
    }

    public void serverError(String error){
        Log.d("Auction.Registration", error);
        switch (error){
            case "\"error.username\"":
                usernameEdit.setError(getString(R.string.error_username_is_busy));
                usernameEdit.requestFocus();
                break;
            case "\"error.password\"":
                passwordEdit.setError(getString(R.string.error_server_password));
                passwordEdit.requestFocus();
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private User parseForm() {
        User user = new  User(emailEdit.getText().toString(), usernameEdit.getText().toString(), passwordEdit.getText().toString(),
                confirmPasswordEdit.getText().toString(), firstnameEdit.getText().toString(),
                lastnameEdit.getText().toString(), aboutMeEdit.getText().toString());
        Log.d("Auction.Registration", "User " + user.toString());
        return user;
    }


    void initComponents(){
        emailEdit = (EditText) findViewById(R.id.email);
        usernameEdit = (EditText) findViewById(R.id.username);
        passwordEdit = (EditText) findViewById(R.id.password);
        confirmPasswordEdit = (EditText) findViewById(R.id.passwordConfirm);
        firstnameEdit = (EditText) findViewById(R.id.firstname);
        lastnameEdit = (EditText) findViewById(R.id.lastname);
        aboutMeEdit = (EditText) findViewById(R.id.history);
    }



}
