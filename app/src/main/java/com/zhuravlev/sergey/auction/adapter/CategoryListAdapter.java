package com.zhuravlev.sergey.auction.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuravlev.sergey.auction.CategoryActivity;
import com.zhuravlev.sergey.auction.ImageLoader;
import com.zhuravlev.sergey.auction.R;
import com.zhuravlev.sergey.auction.dto.Category;

import java.util.List;

public class CategoryListAdapter extends BaseAdapter {

    private Context context;
    private List<Category> categories;

    public CategoryListAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return createView(view, categories.get(position));
    }

    private View createView(View view, final Category category){
        TextView textView = (TextView) view.findViewById(R.id.textView);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);


        textView.setText(category.getName());
        if (category.getImage()!= null)
            new ImageLoader(imageView, context).execute(category.getImage());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CategoryActivity.class);
                intent.putExtra(Category.class.getCanonicalName(), category);
                view.getContext().startActivity(intent);
            }
        });

        return view;
    }


}