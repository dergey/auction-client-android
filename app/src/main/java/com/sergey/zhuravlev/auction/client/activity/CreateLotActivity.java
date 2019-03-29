package com.sergey.zhuravlev.auction.client.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sergey.zhuravlev.auction.client.R;
import com.sergey.zhuravlev.auction.client.adapter.ImageListAdapter;
import com.sergey.zhuravlev.auction.client.client.Client;
import com.sergey.zhuravlev.auction.client.constrain.RequestActivityCodes;

import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateLotActivity extends AppCompatActivity {

    private ImageListAdapter imageListAdapter;
    private List<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lot);
        RecyclerView recyclerView = findViewById(R.id.imagePreviewRecyclerView);
        data = new ArrayList<>();
        imageListAdapter = new ImageListAdapter(this, data);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(imageListAdapter);

    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RequestActivityCodes.IMAGE_SELECT_REQUEST && resultCode == RESULT_OK) {
            Uri selectedFile = data.getData(); //The uri with the location of the file
            Client.getInstance().imageUpload(selectedFile, new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    if (response.code() == 201) {
                        String url = response.headers().get(HttpHeaders.LOCATION);
                        if (url != null && !url.isEmpty()) {
                            CreateLotActivity.this.data.add(Client.SERVER_URL + url);
                            imageListAdapter.notifyDataSetChanged();
                        }
                    } else {
                        try {
                            if (response.isSuccessful()) {
                                Log.e("TEST", "FAIL response code = " + response.code() + '\n' + response.body().toString());
                            } else {
                                Log.e("TEST", "FAIL response code = " + response.code() + '\n' + response.errorBody().string());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                    Log.e("TEST", "FAIL");
                    t.printStackTrace();
                }
            });
        }
    }

}
