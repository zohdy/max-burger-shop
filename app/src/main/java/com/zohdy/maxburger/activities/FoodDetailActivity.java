package com.zohdy.maxburger.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.travijuu.numberpicker.library.NumberPicker;
import com.zohdy.maxburger.R;
import com.zohdy.maxburger.common.Common;
import com.zohdy.maxburger.database.DBOpenHelper;
import com.zohdy.maxburger.interfaces.Constants;
import com.zohdy.maxburger.models.Food;
import com.zohdy.maxburger.models.Order;

public class FoodDetailActivity extends AppCompatActivity {

    private TextView textViewFoodName;
    private TextView textViewFoodPrice;
    private TextView textViewFoodDescription;
    private ImageView imageViewFoodImage;
    private ImageView imageViewCart;
    private TextView textViewCartBadge;
    private Button buttonAddToCart;
    private NumberPicker numberPicker;
    private Order currentOrder;


    private String foodId;

    private FirebaseDatabase database;
    private DatabaseReference foodTable;

    private Food currentFood = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);


        database = FirebaseDatabase.getInstance();
        foodTable = database.getReference("Food");

        initViews();

        if(getIntent() != null) {
            foodId = getIntent().getStringExtra(Constants.FOOD_ID);
            if(!foodId.isEmpty()) {
                getFoodDetails(foodId);
            }
        }
        setupCartBadge();
        handleAddToCartButton();
        cartImageClicked();
    }

    private void initViews() {
        textViewFoodName = findViewById(R.id.tv_food_name_detail_view);
        textViewFoodDescription = findViewById(R.id.tv_food_description_detail_view);
        textViewFoodPrice = findViewById(R.id.tv_food_price_detail_view);
        imageViewFoodImage = findViewById(R.id.iv_image_food_detail_view);
        imageViewCart = findViewById(R.id.iv_shopping_cart_image);
        textViewCartBadge = findViewById(R.id.tv_shopping_cart_badge_food_detail);
        buttonAddToCart = findViewById(R.id.btn_add_to_cart);
        numberPicker =  findViewById(R.id.number_picker);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupCartBadge();
    }

    private void setupCartBadge() {

        if (Common.badgeCounter != 0) {
            textViewCartBadge.setText(String.valueOf(Common.badgeCounter));
        } else {
            textViewCartBadge.setVisibility(View.GONE);
        }
    }

    private void handleAddToCartButton() {
        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Common.badgeCounter += numberPicker.getValue();
                    textViewCartBadge.setVisibility(View.VISIBLE);
                    textViewCartBadge.setText(String.valueOf(Common.badgeCounter));
                    addFoodToCart();

                    Toast.makeText(FoodDetailActivity.this, currentFood.getName() + " er lagt i kurven", Toast.LENGTH_SHORT).show();

                Toast.makeText(FoodDetailActivity.this, currentFood.getName() + " er lagt i kurven", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cartImageClicked() {
        imageViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartIntent = new Intent(FoodDetailActivity.this, CartActivity.class);
                startActivity(cartIntent);
            }
        });
    }


    private void addFoodToCart() {
        DBOpenHelper databaseSQL = DBOpenHelper.newInstance(getApplicationContext());
        databaseSQL.addOrderToCart(new Order(
                foodId,
                currentFood.getName(),
                String.valueOf(numberPicker.getValue()),
                currentFood.getPrice()));
    }


    private void getFoodDetails(String foodId) {
        foodTable.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);
                Picasso.with(getBaseContext()).load(currentFood.getImage())
                        .into(imageViewFoodImage);
                textViewFoodName.setText(currentFood.getName());
                textViewFoodPrice.setText(currentFood.getPrice()+" kr");
                textViewFoodDescription.setText(currentFood.getDescription());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
