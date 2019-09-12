package com.example.aditya.shopit.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aditya.shopit.Interface.ItemClickListener;
import com.example.aditya.shopit.R;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView Item_name;
    public ImageView Item_image;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);

        Item_name = (TextView)itemView.findViewById(R.id.Item_name);
        Item_image = (ImageView)itemView.findViewById(R.id.Item_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
