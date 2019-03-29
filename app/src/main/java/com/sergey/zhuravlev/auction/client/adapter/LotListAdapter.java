package com.sergey.zhuravlev.auction.client.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sergey.zhuravlev.auction.client.R;
import com.sergey.zhuravlev.auction.client.activity.CreateLotActivity;
import com.sergey.zhuravlev.auction.client.client.Client;
import com.sergey.zhuravlev.auction.client.client.ImageLoader;
import com.sergey.zhuravlev.auction.client.dto.ResponseLotDto;

import java.util.List;

public class LotListAdapter extends RecyclerView.Adapter<LotListAdapter.LotViewHolder> {

    static class LotViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        ImageView image;
        TextView title, price;
        ImageButton menuButton;
        ResponseLotDto data;

        LotViewHolder(View itemView) {
            super(itemView);
            itemView.getContext();
            itemView.setOnClickListener(this);
            image = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            menuButton = itemView.findViewById(R.id.imageButton);
            cardView = itemView.findViewById(R.id.cardView);
        }

        @Override
        public void onClick(View v) {
//            Intent intent = new Intent(v.getContext(), LotActivity.class);
//            intent.putExtra(Lot.class.getCanonicalName(), data);
//            intent.putExtra("loadImage", ((BitmapDrawable) image.getDrawable()).getBitmap());
//            v.getContext().startActivity(intent);
        }

    }

    private Context context;
    private List<ResponseLotDto> data;

    protected LotListAdapter getAdapter(){
        return this;
    }

    public LotListAdapter(List<ResponseLotDto> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public LotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lot_item, parent, false);
        return new LotViewHolder(view);
    }

    // Заполнение данных Holder-a
    @Override
    public void onBindViewHolder(final LotViewHolder holder, final int position) {
        holder.data = data.get(position);
        holder.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.menuButton, position);
            }
        });
        holder.title.setText(data.get(position).getTitle());
        //holder.price.setText(data.get(position).getLastBid()+ " руб.");
        //if (data.get(position).getImage()!= null)
        //    new ImageLoader(holder.image, context).execute(data.get(position).getImage());
        Log.v("Auction.Custom", "Создан Holder  с позицией " + position);
    }

    private void showPopupMenu(View view, int position) {
        // inflate menu
        //PopupMenu popup = new PopupMenu(view.getContext(),view );
        //MenuInflater inflater = popup.getMenuInflater();
        //inflater.inflate(R.menu.menu_lot_item, popup.getMenu());
        //popup.setOnMenuItemClickListener(new EditItemClickListener(position));
        //popup.show();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

//    private class EditItemClickListener implements PopupMenu.OnMenuItemClickListener {
//
//        private int position;
//
//        public EditItemClickListener(int position) {
//            this.position = position;
//        }
//
//        @Override
//        public boolean onMenuItemClick(MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.delete:
//                    Log.d("Auction.Lot", "Попытка удалить лот id=" + data.get(position).getId() + " name=" + data.get(position).getTitle());
//                    if (data.get(position).getOwner().getId().longValue() == Client.getInstance().getUser().getId().longValue()) {
//                        Client.getClient().deleteLot(getAdapter(), data, position);
//                    } else Toast.makeText(item.getActionView().getContext(), "Это не ваш лот", Toast.LENGTH_LONG).show();
//                    return true;
//                case R.id.edit:
//                    if (data.get(position).getOwner().getId().longValue() == Client.getInstance().getUser().getId().longValue()) {
//                        Intent intent = new Intent(context, CreateLotActivity.class);
//                        intent.putExtra("edit", true);
//                        intent.putExtra(Lot.class.getCanonicalName(), data.get(position));
//                        context.startActivity(intent);
//                    }
//                    return true;
//                default:
//            }
//            return false;
//        }
//    }


}
