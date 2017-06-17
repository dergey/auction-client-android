package com.zhuravlev.sergey.auction;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhuravlev.sergey.auction.client.Client;
import com.zhuravlev.sergey.auction.dto.Category;
import com.zhuravlev.sergey.auction.dto.Lot;

import java.sql.Timestamp;

public class CreateLotsActivity extends AppCompatActivity {

    private TextView titleView;
    private TextView descriptionView;
    private TextView imageTextView;
    private TextView startingPriceView;
    private TextView auctionStepView;
    private TextView  durationView;
    private Button addButton;

    private Lot data;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lots);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Товар");
        setSupportActionBar(toolbar);

        initComponents();
        client = Client.getClient();
        if (getIntent().getBooleanExtra("edit", false)) loadDataToEdit();
    }

    private void loadDataToEdit(){
        data = getIntent().getParcelableExtra(Lot.class.getCanonicalName());
        titleView.setText(data.getTitle());
        descriptionView.setText(data.getDescription());
        imageTextView.setText(data.getImage());
        startingPriceView.setText(Double.toString(data.getStartingPrice()));
        auctionStepView.setText(Double.toString(data.getAuctionStep()));
        durationView.setText(Long.toString((data.getExpirationDate().getTime() - System.currentTimeMillis()) / 86400000L));
    }

    private void initComponents(){
        titleView = (TextView) findViewById(R.id.title);
        descriptionView = (TextView) findViewById(R.id.description);
        imageTextView = (TextView) findViewById(R.id.image);
        startingPriceView = (TextView) findViewById(R.id.startingPrice);
        auctionStepView = (TextView) findViewById(R.id.auctionStep);
        durationView = (TextView) findViewById(R.id.duration);
        addButton = (Button) findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client.createLot(parseLotForm());
                finish();
            }
        });
    }

    private Lot parseLotForm(){
         /* TODO Мы должны из сомбо бокса выбирать категорию */
        return new Lot(titleView.getText().toString(),
                 descriptionView.getText().toString(),
                 imageTextView.getText().toString(),
                 Double.valueOf(startingPriceView.getText().toString()),
                 Double.valueOf(auctionStepView.getText().toString()),
                 new Timestamp(Long.valueOf(durationView.getText().toString()) * 86400000L));
    }

}
