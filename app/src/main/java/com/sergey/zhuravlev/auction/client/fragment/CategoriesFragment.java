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
import com.sergey.zhuravlev.auction.client.activity.MainActivity;
import com.sergey.zhuravlev.auction.client.adapter.CategoryListAdapter;
import com.sergey.zhuravlev.auction.client.client.Client;
import com.sergey.zhuravlev.auction.client.dto.CategoryDto;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public void loadCategory(List<CategoryDto> categoryList) {
        data.addAll(categoryList);
        adapter.notifyDataSetChanged();
    }

    public void refresh() {
        Client.getInstance().categoriesList(new Callback<List<CategoryDto>>() {
            @Override
            public void onResponse(Call<List<CategoryDto>> call, Response<List<CategoryDto>> response) {
                if (response.isSuccessful()) loadCategory(response.body());
            }

            @Override
            public void onFailure(Call<List<CategoryDto>> call, Throwable t) {
                if (CategoriesFragment.this.getContext() != null && CategoriesFragment.this.getContext() instanceof ContextWithCallback) {
                    ((ContextWithCallback) CategoriesFragment.this.getContext()).showErrorMessage("Unable to get categories list!", t);
                }
            }
        });
    }
}
