package com.nhom9.orderfood.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nhom9.orderfood.R;
import com.nhom9.orderfood.Interface.ItemClickListener;


public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView food_name;
    public ImageView food_image,fav_image,share_image;
    private ItemClickListener itemClickListener;
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }



    public FoodViewHolder(View itemView) {
        super(itemView);
        food_image= itemView.findViewById(R.id.food_image);
        food_name = itemView.findViewById(R.id.food_name);
        fav_image= itemView.findViewById(R.id.fav);
        share_image = itemView.findViewById(R.id.btnShare);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
