package com.zhuravlev.sergey.auction.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.zhuravlev.sergey.auction.Constants;
import com.zhuravlev.sergey.auction.MainActivity;
import com.zhuravlev.sergey.auction.R;
import com.zhuravlev.sergey.auction.adapter.CategoryListAdapter;
import com.zhuravlev.sergey.auction.client.Client;
import com.zhuravlev.sergey.auction.dto.Category;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoriesFragment extends AbstractTabFragment {

    private static final int LAYOUT = R.layout.fragment_category;

    private  List<Category> data;


    CategoryListAdapter adapter;

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



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        data = new ArrayList<>();
        new LoadCategoryTask().execute();
        //тут мы находим наше View и присваеваем ему адаптер
        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        adapter = new CategoryListAdapter(context, data);
        gridView.setAdapter(adapter);

        return view;
    }

    public void loadCategory(List<Category> categoryList) {
        data.addAll(categoryList);
        adapter.notifyDataSetChanged();
    }

    private class LoadCategoryTask extends AsyncTask<Void, Void, List<Category>> {

        @Override
        protected List<Category> doInBackground (Void... voids) {
            RestTemplate template = new RestTemplate();
            List<Category> categoryList = null;
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            try {
                Category[] categories = template.getForObject(Constants.GET_ALL_CATEGORY, Category[].class);
                Log.d("AuctionD", "Количество категорий: " + categories.length);
                categoryList = Arrays.asList(categories);
            } catch (Exception ignored) {
                Log.d("AuctionD", "Ошибка подключения к серверу - " + ignored.getLocalizedMessage());
                ((MainActivity) context).showConnectionError();
            }
            return categoryList;
        }

        @Override
        protected void onPostExecute(List<Category> categories) {
            if (categories != null)
                loadCategory(categories);
            else
                Log.d("AuctionD", "lotList ссылается на несуществующий объект");
        }
    }

}
