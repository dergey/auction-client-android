package com.sergey.zhuravlev.auction.client.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.sergey.zhuravlev.auction.client.R;
import com.sergey.zhuravlev.auction.client.activity.ContextWithCallback;
import com.sergey.zhuravlev.auction.client.adapter.CategoryListAdapter;
import com.sergey.zhuravlev.auction.client.client.Client;
import com.sergey.zhuravlev.auction.client.client.SimpleCallback;
import com.sergey.zhuravlev.auction.client.dto.CategoryDto;
import com.sergey.zhuravlev.auction.client.dto.PageDto;

import java.util.ArrayList;
import java.util.List;

public class CategoriesFragment extends AbstractTabFragment {

    private static final int LAYOUT = R.layout.fragment_category;

    private List<CategoryDto> data;

    private CategoryListAdapter adapter;

    public static CategoriesFragment getInstance(Context context) {
        Bundle args = new Bundle();
        CategoriesFragment fragment = new CategoriesFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_categories));
        return fragment;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setCategories(List<CategoryDto> categories) {
        data.clear();
        data.addAll(categories);
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        data = new ArrayList<>();
        this.refresh();
        //тут мы находим наше View и присваеваем ему адаптер
        GridView gridView = view.findViewById(R.id.gridView);
        adapter = new CategoryListAdapter(context, data);
        gridView.setAdapter(adapter);
        return view;
    }

    public void refresh() {
        Client.getInstance().getCategoriesPage(new SimpleCallback<PageDto<CategoryDto>>() {
            @Override
            public void onResponse(PageDto<CategoryDto> response) {
                CategoriesFragment.this.setCategories(response.getContent());
            }

            @Override
            public void onFailure(Throwable t) {
                if (CategoriesFragment.this.getContext() != null && CategoriesFragment.this.getContext() instanceof ContextWithCallback) {
                    ((ContextWithCallback) CategoriesFragment.this.getContext()).showErrorMessage("Unable to get categories list!", t);
                }
            }
        });
    }
}
