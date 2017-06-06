package com.zhuravlev.sergey.auction;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

public class LotActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView titleView, priceView, description;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lot);
        initComponents();
        findExtra();
    }

    private void initComponents() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleView = (TextView) findViewById(R.id.titleView);
        priceView = (TextView) findViewById(R.id.priceView);
        description = (TextView) findViewById(R.id.description);
        imageView = (ImageView) findViewById(R.id.imageView);

        toolbar.setTitle("О товаре");
        setSupportActionBar(toolbar);
    }

    private void findExtra() {
        imageView.setImageBitmap((Bitmap) getIntent().getParcelableExtra("image"));
        titleView.setText(getIntent().getStringExtra("title"));
        description.setText(getIntent().getStringExtra("description"));
        priceView.setText("Текущая ставка: "
                + Double.toString(getIntent().getDoubleExtra("price", 0))
                + " " + getIntent().getStringExtra("currency"));
    }

}
