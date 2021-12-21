package com.nhom9.orderfoodserver.Common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nhom9.orderfoodserver.R;
import com.nhom9.orderfoodserver.Model.Order;

import java.util.List;

public class OrderAdapter extends BaseAdapter {

    private List<Order> list;
    private Context context;

    public OrderAdapter(List<Order> list, Context context){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewHolder;
        if (view == null) {
            viewHolder = View.inflate(viewGroup.getContext(), R.layout.item_order, null);
        } else viewHolder = view;

        Order order = list.get(i);
        ((TextView) viewHolder.findViewById(R.id.tv_name)).setText("Tên Món: "+order.getProductName().toString());
        ((TextView) viewHolder.findViewById(R.id.tv_quatity)).setText("Số lượng: "+order.getQuantity().toString());
        return viewHolder;
    }
}
// lên gg search adapter trong android là hiểu nó dùng để làm gì
