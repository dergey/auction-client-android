package com.zhuravlev.sergey.auction;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class LotActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lot);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView titleView = (TextView) findViewById(R.id.titleView);
        TextView priceView = (TextView) findViewById(R.id.priceView);
        TextView description = (TextView) findViewById(R.id.description);

        titleView.setText(getIntent().getStringExtra("title"));
        description.setText(getIntent().getStringExtra("description"));
        priceView.setText("Текущая ставка: " + Double.toString(getIntent().getDoubleExtra("price", 0)) + " " + getIntent().getStringExtra("currency"));
        toolbar.setTitle("О товаре");
        setSupportActionBar(toolbar);
    }

}
