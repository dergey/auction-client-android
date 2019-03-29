package com.sergey.zhuravlev.auction.client.adapter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sergey.zhuravlev.auction.client.R;
import com.sergey.zhuravlev.auction.client.client.ImageLoader;
import com.sergey.zhuravlev.auction.client.constrain.RequestActivityCodes;

import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageViewHolder> {

    private static final int VIEW_TYPE_CELL = 1;
    private static final int VIEW_TYPE_FOOTER = 2;

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CardView cardView;
        String data;

        ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    //------------------------------------------

    private AppCompatActivity context;
    private List<String> urls;

    public ImageListAdapter(AppCompatActivity context, List<String> urls) {
        this.context = context;
        this.urls = urls;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CELL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_preview_item, parent, false);
            return new ImageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_item, parent, false);
            return new ImageViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == urls.size()) ? VIEW_TYPE_FOOTER : VIEW_TYPE_CELL;
    }

    // Заполнение данных Holder-a
    @Override
    public void onBindViewHolder(final ImageViewHolder holder, final int position) {
        if (position + 1 == getItemCount()) {
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent()
                            .setType("image/*")
                            .setAction(Intent.ACTION_GET_CONTENT);
                    context.startActivityForResult(Intent.createChooser(intent, "Select a image"), RequestActivityCodes.IMAGE_SELECT_REQUEST);
                }
            });
            return;
        }
        holder.data = urls.get(position);
        if (urls.get(position) != null)
            new ImageLoader(holder.imageView, context).execute(urls.get(position));
    }

    @Override
    public int getItemCount() {
        return urls.size() + 1;
    }

}