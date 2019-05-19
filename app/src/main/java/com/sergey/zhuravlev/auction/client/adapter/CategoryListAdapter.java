package com.sergey.zhuravlev.auction.client.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sergey.zhuravlev.auction.client.R;
import com.sergey.zhuravlev.auction.client.activity.ContextWithCallback;
import com.sergey.zhuravlev.auction.client.activity.LotListActivity;
import com.sergey.zhuravlev.auction.client.client.Client;
import com.sergey.zhuravlev.auction.client.dto.CategoryDto;
import com.sergey.zhuravlev.auction.client.dto.ResponseLotDto;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryListAdapter extends BaseAdapter {

    private Context context;
    private List<CategoryDto> categories;

    public CategoryListAdapter(Context context, List<CategoryDto> categories) {
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
        @SuppressLint("ViewHolder") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return createView(view, categories.get(position));
    }

    private View createView(View view, final CategoryDto category) {
        final TextView textView = view.findViewById(R.id.textView);
        final ImageView imageView = view.findViewById(R.id.imageView);

        textView.setText(category.getName());

        if (category.getImage() != null) {
            Client.getInstance().imageDownload(category.getImage(), new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.body() != null) {
                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                        imageView.setImageBitmap(bmp);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Client.getInstance().lotList(category.getName(), null, null, 0, 20,
                        new Callback<List<ResponseLotDto>>() {
                            @Override
                            public void onResponse(Call<List<ResponseLotDto>> call, Response<List<ResponseLotDto>> response) {
                                List<ResponseLotDto> responseLotDtos = response.body();
                                if (response.isSuccessful() && responseLotDtos != null) {
                                    Intent intent = new Intent(view.getContext(), LotListActivity.class);
                                    intent.putExtra(LotListActivity.REQUEST_TITLE_NAME, category.getName());
                                    if (responseLotDtos.size() > 0) {
                                        intent.putParcelableArrayListExtra(LotListActivity.REQUEST_LOTS_EXTRA_NAME, new ArrayList<>(responseLotDtos));
                                    }
                                    view.getContext().startActivity(intent);
                                }
                            }

                            @Override
                            public void onFailure(Call<List<ResponseLotDto>> call, Throwable t) {
                                if (context instanceof ContextWithCallback)
                                    ((ContextWithCallback) context).showErrorMessage(context.getString(R.string.error_unable_to_connect), t);
                            }
                        });
            }
        });
        return view;
    }


}