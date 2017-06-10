package com.zhuravlev.sergey.auction.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhuravlev.sergey.auction.R;
import com.zhuravlev.sergey.auction.client.Client;

import org.springframework.http.ResponseEntity;

public class HomeFragment extends AbstractTabFragment {
    private static final int LAYOUT = R.layout.fragment_home;

    private Client client;

    public static HomeFragment getInstance(Context context, Client client) {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        fragment.setClient(client);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_home));
        return fragment;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
//        new loginTask().execute();
        return view;
    }


//    private class loginTask extends AsyncTask<Void, Void, ResponseEntity> {
//
//        @Override
//        protected ResponseEntity doInBackground (Void... voids) {
//            if (!client.IsAuthorized()) client.login("dergey","12345678"); else Log.d("Auction.Login", "Вы уже авторизованны!");
//            return client.test();
//        }
//
//        @Override
//        protected void onPostExecute(ResponseEntity response) {
//            if (response.hasBody()) Log.d("Auction.Login", "Тело ответа: " + response.getBody());
//                else Log.d("Auction.Login", "Нет тела");
//        }
//    }

}