package com.zhuravlev.sergey.auction;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.zhuravlev.sergey.auction.adapter.LotListAdapter;
import com.zhuravlev.sergey.auction.client.Client;
import com.zhuravlev.sergey.auction.dto.Lot;
import com.zhuravlev.sergey.auction.dto.User;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView usernameView, fullnameView;
    private RatingBar ratingBar;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;

    private Client client;

    private List<Lot> data;
    private LotListAdapter adapter;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        client = Client.getClient();
        initComponents();
        initRecyclerView();
    }

    private void initRecyclerView() {
        data = new ArrayList<>();
        adapter = new LotListAdapter(data, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        //TODO переделать под других пользователей!
        Client.getClient().loadUserLots(client.getUser().getId(), this);
    }

    private void initComponents() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        user = getIntent().getParcelableExtra(User.class.getCanonicalName());

        toolbar.setTitle(user.getUsername());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //imageView = (ImageView) findViewById(R.id.imageView);
        //usernameView = (TextView) findViewById(R.id.username);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateLotActivity();
            }
        });
        //fullnameView = (TextView) findViewById(R.id.fullname);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(true);
    }

    private void showCreateLotActivity() {
        Intent intent = new Intent(this, CreateLotsActivity.class);
        startActivity(intent);
    }

    public void loadLots(List<Lot> lots){
        data.addAll(lots);
        adapter.notifyDataSetChanged();
    }
}
