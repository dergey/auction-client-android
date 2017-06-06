package com.zhuravlev.sergey.auction.adapter;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuravlev.sergey.auction.ImageLoader;
import com.zhuravlev.sergey.auction.LotActivity;
import com.zhuravlev.sergey.auction.R;
import com.zhuravlev.sergey.auction.dto.Lot;

import java.util.List;

public class LotListAdapter extends RecyclerView.Adapter<LotListAdapter.LotViewHolder> {

    // ---------------------------------------------------------------------------------------------

    static class LotViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        ImageView image;
        TextView title, price;
        Lot data;

        LotViewHolder(View itemView) {
            super(itemView);
            itemView.getContext();
            itemView.setOnClickListener(this);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            title = (TextView) itemView.findViewById(R.id.title);
            //description = (TextView) itemView.findViewById(R.id.description);
            price = (TextView) itemView.findViewById(R.id.price);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), LotActivity.class);
            intent.putExtra("title", data.getTitle());
            intent.putExtra("description", data.getDescription());
            intent.putExtra("price", data.getPrice());
            intent.putExtra("currency", data.getCurrency());
            //TODO Error geper size
            intent.putExtra("image", ((BitmapDrawable) image.getDrawable()).getBitmap());
            v.getContext().startActivity(intent);
        }

    }

    // ---------------------------------------------------------------------------------------------

    private List<Lot> data;

    public LotListAdapter(List<Lot> data) {
        this.data = data;
    }

    @Override
    public LotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lot_item, parent, false);
        return new LotViewHolder(view);
    }

    // Заполнение данных Holder-a
    @Override
    public void onBindViewHolder(LotViewHolder holder, int position) {
        holder.data = data.get(position); //передали данные в Holder
        holder.title.setText(data.get(position).getTitle());
        //holder.description.setText(data.get(position).getDescription());
        holder.price.setText(data.get(position).getPrice().toString() + " " + data.get(position).getCurrency());
        if (data.get(position).getImage()!= null)
            new ImageLoader(holder.image).execute(data.get(position).getImage());
        Log.d("AuctionD", "Создан Holder  с позицией " + position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
