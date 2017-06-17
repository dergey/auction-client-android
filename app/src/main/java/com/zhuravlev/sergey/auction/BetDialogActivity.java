package com.zhuravlev.sergey.auction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhuravlev.sergey.auction.client.Client;
import com.zhuravlev.sergey.auction.dto.Bet;
import com.zhuravlev.sergey.auction.dto.Lot;

public class BetDialogActivity extends AppCompatActivity {

    Button button;
    EditText editText;
    Lot lot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bet_dialog);
        lot = getIntent().getParcelableExtra(Lot.class.getCanonicalName());
        Log.d("Auction.Bet", "Lot получен " + lot);
        initComponents();
    }

    private void initComponents(){
        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Vereficator
                Client.getClient().makeBet(parseLayout());
                finish();
            }
        });
    }

    private Bet parseLayout() {
        Log.d("Auction.Bet", "Lot получен " + lot);
        return new Bet(Double.valueOf(editText.getText().toString()), lot.getId());
    }
}
