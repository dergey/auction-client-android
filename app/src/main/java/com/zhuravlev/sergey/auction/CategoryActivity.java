package com.zhuravlev.sergey.auction;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.zhuravlev.sergey.auction.adapter.LotListAdapter;
import com.zhuravlev.sergey.auction.client.Client;
import com.zhuravlev.sergey.auction.dto.Category;
import com.zhuravlev.sergey.auction.dto.Lot;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private List<Lot> data;
    private Category category;
    private LotListAdapter adapter;
    private Toolbar toolbar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        initComponents();
        initAdapter();
        findExtra();
        setSupportActionBar(toolbar);
    }

    public void initAdapter(){
        data = new ArrayList<>();
        adapter = new LotListAdapter(data, this);
        recyclerView.setAdapter(adapter);
    }

    public void initComponents(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void findExtra(){
        category = getIntent().getParcelableExtra(Category.class.getCanonicalName());
        toolbar.setTitle(category.getName());
        Client.getClient().getLots(category.getId(), this);
    }

    public void showConnectionError(){
        Snackbar.make(findViewById(R.id.recyclerView), R.string.error_unable_to_connect, Snackbar.LENGTH_LONG).show();
    }

    public void loadLots(List<Lot> lots){
        data.addAll(lots);
        adapter.notifyDataSetChanged();
    }

}


