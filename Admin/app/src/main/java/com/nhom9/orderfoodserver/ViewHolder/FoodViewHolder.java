package com.nhom9.orderfoodserver.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nhom9.orderfoodserver.R;
import com.nhom9.orderfoodserver.Common.Common;
import com.nhom9.orderfoodserver.Interface.ItemClickListener;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {
    public TextView food_name;
    public ImageView food_image;
    public Button btn;


    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FoodViewHolder(View itemView) {
        super(itemView);
        food_name= itemView.findViewById(R.id.food_name);
        food_image = itemView.findViewById(R.id.food_image);
        btn = itemView.findViewById(R.id.btnUpdate);

        btn.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.OnClick(view,getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0,0,getAdapterPosition(), Common.UPDATE);
        menu.add(0,1,getAdapterPosition(), Common.DELETE);
    }
}
