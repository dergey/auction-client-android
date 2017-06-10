package com.zhuravlev.sergey.auction;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.zhuravlev.sergey.auction.adapter.LotListAdapter;
import com.zhuravlev.sergey.auction.client.Client;
import com.zhuravlev.sergey.auction.dto.Lot;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private List<Lot> data;
    private LotListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        data = new ArrayList<>();
        adapter = new LotListAdapter(data);
        recyclerView.setAdapter(adapter);

        toolbar.setTitle(getIntent().getStringExtra("name"));
        setSupportActionBar(toolbar);

        new LoadCategoryTask().execute(getIntent().getLongExtra("id", 0));
    }

    public void showConnectionError(){
        Snackbar.make(findViewById(R.id.recyclerView), R.string.error_unable_to_connect, Snackbar.LENGTH_LONG).show();
    }

    private void loadLots(List<Lot> lots){
        data.addAll(lots);
        adapter.notifyDataSetChanged();
    }

    // Переделать на получение категорий
    private class LoadCategoryTask extends AsyncTask<Long, Void, List<Lot>> {

        @Override
        protected List<Lot> doInBackground (Long... longs) {
            RestTemplate template = new RestTemplate();
            List<Lot> lotsList = null;
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            try {
                Lot[] lots = template.getForObject(Constants.GET_ALL_LOTS_BY_CATEGORY_ID + longs[0], Lot[].class);
                Log.d("AuctionD", "Количество товара по категории "+ longs[0] + ": " + lots.length);
                lotsList = Arrays.asList(lots);
            } catch (Exception ignored) {
                Log.d("AuctionD", "Ошибка подключения к серверу - " + ignored.getLocalizedMessage());
                showConnectionError();
            }
            return lotsList;
        }

        @Override
        protected void onPostExecute(List<Lot> lots) {
            if (lots != null)
                loadLots(lots);
            else
                Log.d("AuctionD", "lotList ссылается на несуществующий объект");
        }
    }
}


