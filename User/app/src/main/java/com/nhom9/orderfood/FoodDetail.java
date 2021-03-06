package com.nhom9.orderfood;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.nhom9.orderfood.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nhom9.orderfood.Common.Common;
import com.nhom9.orderfood.Database.Database;
import com.nhom9.orderfood.Model.Food;
import com.nhom9.orderfood.Model.Order;
import com.nhom9.orderfood.Model.Rating;
import com.nhom9.orderfood.ViewHolder.FoodViewHolder;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;

public class FoodDetail extends AppCompatActivity implements RatingDialogListener {
    ElegantNumberButton numberButton;
    CollapsingToolbarLayout collapsingToolbarLayout;
    TextView food_name,food_price,food_description,txtRating;
    ImageView food_image;
    FloatingActionButton btnRating,btnMua;
    RatingBar ratingBar;
    String foodId="";
    FirebaseDatabase database;
    DatabaseReference food;
    Food currentFood;
    DatabaseReference ratingTbl;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Rating, FoodViewHolder> adapter;




    // chi tiết món ăn như giá bán, tên món ăn,....
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        database = FirebaseDatabase.getInstance();
        food = database.getReference("Foods");
        ratingTbl = database.getReference("Rating");
        food_price =  findViewById(R.id.food_price);
        food_name = findViewById(R.id.food_name);
        food_description =  findViewById(R.id.food_description);
        food_image = findViewById(R.id.img_food);
        collapsingToolbarLayout = findViewById(R.id.collapsing);
        btnRating =  findViewById(R.id.btnRating);
        ratingBar =  findViewById(R.id.ratingBar);
        numberButton = findViewById(R.id.number_button);
        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRatingDialog();
            }
        });
        btnMua =  findViewById(R.id.btnCart);
        btnMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //code đưa món ăn vào giỏ hàng
                new Database(getBaseContext()).addToCart(new Order(
                        foodId,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount()
                ));
                Toast.makeText(FoodDetail.this,"Thêm vào giỏ hàng",Toast.LENGTH_SHORT).show();
            }

        });

        if (getIntent()!=null)
            foodId = getIntent().getStringExtra("FoodId");
        if (!foodId.isEmpty()){
            getDetailFood(foodId);
            getRatingFood(foodId);
        }
    }

    // đánh giá món ăn
    private void getRatingFood(String foodId) {
        com.google.firebase.database.Query foodRating = ratingTbl.orderByChild("foodId").equalTo(foodId);
        foodRating.addValueEventListener(new ValueEventListener() {
            int count =0, sum =0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Rating item = postSnapshot.getValue(Rating.class);
                    sum+= Integer.parseInt(item.getRateValue());
                    count++;
                }
                if (count!=0) {
                    float average = sum / count;
                    ratingBar.setRating(average);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    // mỗi sao ratting sẽ tương ứng với 1 đánh giá ví dụ 1sao thì rất tồi,...
    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Đồng ý")
                .setNegativeButtonText("Hủy")
                .setNoteDescriptions(Arrays.asList("Rất tồi","Không tốt","Tạm được","Rất tốt","Trên tuyệt vời"))
                .setDefaultRating(1)
                .setTitle("Đánh giá tài liệu")
                .setDescription("Hãy chọn số sao và để lại feedback cho chúng tôi")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Viết bình luận của bạn tại đây")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(FoodDetail.this)
                .show();

    }

    private void getDetailFood(String foodId) {
        food.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);
                Picasso.with(getBaseContext()).load(currentFood.getImage()).into(food_image);
                collapsingToolbarLayout.setTitle(currentFood.getName());
                food_name.setText(currentFood.getName());
                food_price.setText("Giá : "+currentFood.getPrice()+" VNĐ");
                food_description.setText(currentFood.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onPositiveButtonClicked(int value, final String comments) {
       final Rating rating = new Rating(Common.currentUser.getPhone(),
                foodId,
                String.valueOf(value),
                comments);
        ratingTbl.child(Common.currentUser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(Common.currentUser.getPhone()).exists()){
                    ratingTbl.child(Common.currentUser.getPhone()).removeValue();
                    ratingTbl.child(Common.currentUser.getPhone()).setValue(rating);
                }
                else
                {
                    ratingTbl.child(Common.currentUser.getPhone()).setValue(rating);
                }
                Toast.makeText(FoodDetail.this, "Cảm ơn bạn đã đánh giá !!!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onNegativeButtonClicked() {

    }
}
