package com.zhuravlev.sergey.remindme.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zhuravlev.sergey.remindme.R;
import com.zhuravlev.sergey.remindme.dto.LotDTO;
import java.util.List;

public class LotListAdapter extends RecyclerView.Adapter<LotListAdapter.LotViewHolder> {

    private List<LotDTO> data;

    public LotListAdapter(List<LotDTO> data) {
        this.data = data;
    }

    @Override
    public LotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.remind_item, parent, false);
        return new LotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LotViewHolder holder, int position) {
        holder.title.setText(data.get(position).getTitle());
        holder.description.setText(data.get(position).getDescription());
        holder.price.setText(data.get(position).getPrice().toString() + " " + data.get(position).getCurrency());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class LotViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title, description, price;

        public LotViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            price = (TextView) itemView.findViewById(R.id.price);
            cardView = (CardView) itemView.findViewById(R.id.cardView);

        }

    }


}
