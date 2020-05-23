package com.sergey.zhuravlev.auction.client.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.sergey.zhuravlev.auction.client.R;
import com.sergey.zhuravlev.auction.client.activity.ContextWithCallback;
import com.sergey.zhuravlev.auction.client.client.Client;
import com.sergey.zhuravlev.auction.client.client.SimpleCallback;
import com.sergey.zhuravlev.auction.client.dto.CategoryDto;
import com.sergey.zhuravlev.auction.client.dto.PageDto;


public class MainFragment extends AbstractTabFragment {

    private static final int LAYOUT = R.layout.fragment_main;

    public static MainFragment getInstance(Context context) {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_main));
        return fragment;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        WebView webView = view.findViewById(R.id.web_view);
        return view;
    }

    public void refresh() {
        Client.getInstance().getCategoriesPage(new SimpleCallback<PageDto<CategoryDto>>() {
            @Override
            public void onResponse(PageDto<CategoryDto> response) {

            }

            @Override
            public void onFailure(Throwable t) {
                if (MainFragment.this.getContext() != null && MainFragment.this.getContext() instanceof ContextWithCallback) {
                    ((ContextWithCallback) MainFragment.this.getContext()).showErrorMessage("Unable to get categories list!", t);
                }
            }
        });
    }
}
