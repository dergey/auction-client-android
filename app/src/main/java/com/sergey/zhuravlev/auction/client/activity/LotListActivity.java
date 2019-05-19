package com.sergey.zhuravlev.auction.client.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.sergey.zhuravlev.auction.client.R;
import com.sergey.zhuravlev.auction.client.adapter.LotListAdapter;
import com.sergey.zhuravlev.auction.client.dto.ResponseLotDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LotListActivity extends AppCompatActivity {

    public static final String REQUEST_TITLE_NAME = "REQUEST_TITLE";
    public static final String REQUEST_LOTS_EXTRA_NAME = "REQUEST_LOTS";

    private List<ResponseLotDto> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lot_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        findExtra();
        initRecyclerView();
    }

    public void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LotListAdapter adapter = new LotListAdapter(data, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void findExtra(){
        if (getIntent().getExtras() != null) {
            String title = getIntent().getExtras().getString(REQUEST_TITLE_NAME);
            Objects.requireNonNull(getSupportActionBar()).setTitle(String.format("Category %s", title));
            List<ResponseLotDto> lots = getIntent().getExtras().getParcelableArrayList(REQUEST_LOTS_EXTRA_NAME);
            if (lots != null) {
                data = lots;
                Log.d("LotListActivity", "Get new array list, with size = " + lots.size());
            } else {
                data = new ArrayList<>();
                Log.w("LotListActivity", "Get nullable array list!");
            }
        }
    }

}
