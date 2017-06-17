package com.zhuravlev.sergey.auction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.zhuravlev.sergey.auction.client.Client;
import com.zhuravlev.sergey.auction.dto.Bet;
import com.zhuravlev.sergey.auction.dto.Lot;

public class LotActivity extends AppCompatActivity {

    private Lot data;

    private Toolbar toolbar;
    private RatingBar ratingBar;
    private TextView titleView, priceView, descriptionView, sellerNameView, dateView;
    private ImageView imageView;
    private Button makeBetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lot);
        initComponents();
        findExtra();
        Client.getClient().getLastBet(this, data);
        initButton();
    }

    private Context getContext(){
        return this;
    }

    private void initButton(){
        makeBetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BetDialogActivity.class);
                intent.putExtra(Lot.class.getCanonicalName(), data);
                startActivity(intent);

            }
        });
    }

    private void initComponents() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleView = (TextView) findViewById(R.id.titleView);
        priceView = (TextView) findViewById(R.id.priceView);
        descriptionView = (TextView) findViewById(R.id.description);
        sellerNameView = (TextView) findViewById(R.id.sellerName);
        imageView = (ImageView) findViewById(R.id.imageView);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        dateView = (TextView) findViewById(R.id.dateView);
        makeBetButton = (Button) findViewById(R.id.buttonMakeBet);
        toolbar.setTitle("О товаре");
        setSupportActionBar(toolbar);
    }

    private void findExtra() {
        data = getIntent().getParcelableExtra(Lot.class.getCanonicalName());
        imageView.setImageBitmap((Bitmap) getIntent().getParcelableExtra("loadImage"));

        titleView.setText(data.getTitle());
        dateView.setText("Осталось времени: " + stringMillis(data.getExpirationDate().getTime() - System.currentTimeMillis()));
        descriptionView.setText(data.getDescription());
        //TODO Добавить имя
        priceView.setText("Текущая ставка: " + data.getLastPrice() + " руб.");
        sellerNameView.setText("Имя: " + data.getOwner().getFirstname() + " " + data.getOwner().getLastname());
        if (data.getOwner().getRating() != null)
            ratingBar.setRating(data.getOwner().getRating());
            else ratingBar.setRating(0);
        if (data.getStatus() == 0)
          makeBetButton.setVisibility(View.VISIBLE);
        else
          makeBetButton.setVisibility(View.INVISIBLE);
    }

    private String stringMillis(Long millis){
        int minuts = Math.round(millis / 60000);
        int hours = Math.round(minuts / 60);
        minuts = minuts - hours * 60;
        return hours+"ч. "+minuts + "мин.";
    }

    public void setLastBet(Bet lastBet) {
        data.setLastBet(lastBet);
        priceView.setText("Текущая ставка: " + data.getLastPriceInString());
    }
}
