package com.nhom9.orderfood;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom9.orderfood.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nhom9.orderfood.Common.Common;
import com.nhom9.orderfood.Database.Database;
import com.nhom9.orderfood.Model.Order;
import com.nhom9.orderfood.Model.Request;
import com.nhom9.orderfood.ViewHolder.CartAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity implements CartAdapter.OnItemClickListener {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;
    TextView txtTotalPrice;
    Button btnPlace, btnxoa;
    CartAdapter adapter;
    List<Order> cart = new ArrayList<>();
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice =  findViewById(R.id.total);
        btnPlace = findViewById(R.id.btnPlaceOrder);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cart.size()>0)
                    showAlertDialog();
                else
                    Toast.makeText(Cart.this, "Gi??? h??ng c???a b???n tr???ng!!!", Toast.LENGTH_SHORT).show();
            }
        });

        loadListDocument();
    }
    //????? t???o th??ng b??o dialog cho user bi???t
    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("Qu?? kh??ch vui l??ng gi??? ??i???n tho???i ????? c???a h??ng ti???n li??n l???c");

        alertDialog.setMessage("Nh???p v??o ?????a ch??? c???a b???n:  ");
        final EditText edtAddress = new EditText(Cart.this);
        LinearLayout.LayoutParams lp =new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edtAddress.setLayoutParams(lp);
        alertDialog.setView(edtAddress);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        alertDialog.setPositiveButton("?????ng ??", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        edtAddress.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        cart
                );
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this,"C???m ??n , ch??ng t??i ??ang mang th???c ??n ?????n cho b???n",Toast.LENGTH_LONG).show();
                finish();
            }
        });
        alertDialog.setNegativeButton("Kh??ng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();

    }
    //code n??y ????? hi???n th??? s??? ti???n c???n thanh to??n trong gi??? h??ng (cart)
    private void loadListDocument() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart, this,this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        int total =0;
        for(Order order:cart)
                total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
        //Locale locale = new Locale("vi","VN");
       // NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        txtTotalPrice.setText(formatter.format(total)+" VND");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());
        return true;

    }

    private void deleteCart(int order) {
        cart.remove(order);
        new Database(this).cleanCart();
        for (Order item:cart)
            new Database(this).addToCart(item);
        loadListDocument();
    }
    //x??a c??c m??n v???a order
    @Override
    public void deleteItem(int index) {

        Log.d("clicked", "deleteItem: ====================" + index);
        Toast.makeText(this, "???? x??a", Toast.LENGTH_SHORT).show();
        deleteCart(index);
    }
}
