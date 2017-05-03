package com.zhuravlev.sergey.remindme.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuravlev.sergey.remindme.R;
import com.zhuravlev.sergey.remindme.dto.LotDTO;

import java.util.List;

public class LotListAdapter extends RecyclerView.Adapter<LotListAdapter.LotViewHolder> {

    // ---------------------------------------------------------------------------------------------

    public static class LotViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        ImageView image;
        TextView title, description, price;
        protected int position;

        public LotViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            title = (TextView) itemView.findViewById(R.id.title);
            //description = (TextView) itemView.findViewById(R.id.description);
            price = (TextView) itemView.findViewById(R.id.price);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "OnClick Event on Holder No" + position, Toast.LENGTH_LONG).show();
        }

    }

    // ---------------------------------------------------------------------------------------------

    private List<LotDTO> data;

    public LotListAdapter(List<LotDTO> data) {
        this.data = data;
    }

    @Override
    public LotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lot_item, parent, false);
        return new LotViewHolder(view);
    }

    // Описание товара
    @Override
    public void onBindViewHolder(LotViewHolder holder, int position) {
        holder.position = position;
        holder.title.setText(data.get(position).getTitle());
        //holder.description.setText(data.get(position).getDescription());
        holder.price.setText(data.get(position).getPrice().toString() + " " + data.get(position).getCurrency());
        if (data.get(position).getImage()!= null)
          holder.image.setImageBitmap(data.get(position).getImage());

        Log.d("LOG", "Создан Holder  с позицией " + position);
        // Тут мы должны запускать AsyncTask
//        if (eventoInfoAux.foto==null){
//            CargarImagen cargarImagen = new CargarImagen(holder.cardView, position);
//            cargarImagen.execute();
//        }else{
//            ((EventosViewHolder) holder).vRelativeLayout.setBackground(eventoInfoAux.foto);
//        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
