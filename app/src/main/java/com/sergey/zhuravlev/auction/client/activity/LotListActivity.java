package com.sergey.zhuravlev.auction.client.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sergey.zhuravlev.auction.client.R;
import com.sergey.zhuravlev.auction.client.adapter.LotListAdapter;
import com.sergey.zhuravlev.auction.client.dto.ResponseLotDto;

import java.util.ArrayList;
import java.util.List;

public class LotListActivity extends AppCompatActivity {

    public static final String REQUEST_LOTS_EXTRA_NAME = "REQUEST_LOTS";

    private List<ResponseLotDto> data;
    private LotListAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lot_list);
        recyclerView = findViewById(R.id.recycler_view);
        findExtra();
        initAdapter();
    }

    public void initAdapter() {
        adapter = new LotListAdapter(data, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void findExtra(){
        ArrayList<ResponseLotDto> lots = getIntent().getParcelableArrayListExtra(REQUEST_LOTS_EXTRA_NAME);
        if (lots != null) {
            data = lots;
            Log.d("LotListActivity", "Get new array list, with size = " + lots.size());
        } else {
            data = new ArrayList<>();
            Log.w("LotListActivity", "Get nullable array list!");
        }
    }

}
