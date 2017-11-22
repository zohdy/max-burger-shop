package com.zohdy.maxburger.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;
import com.zohdy.maxburger.R;
import com.zohdy.maxburger.common.Common;
import com.zohdy.maxburger.interfaces.Constants;
import com.zohdy.maxburger.interfaces.ItemClickListener;
import com.zohdy.maxburger.models.Food;
import com.zohdy.maxburger.viewholders.FoodViewHolder;

import java.util.ArrayList;
import java.util.List;

public class FoodActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodTable;
    String categoryId;
    String categoryDescription;
    String categoryName;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> recyclerAdapter;
    TextView textViewCategoryDescription;
    TextView textViewCategoryName;

    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchAdapter;
    List<String> listSuggestions = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        initLayout();
        setupRecyclerview();
        setupSearchBar();

        database = FirebaseDatabase.getInstance();
        foodTable = database.getReference(Constants.FIREBASE_DB_TABLE_FOOD);

        if(getIntent() != null) {
            categoryId = getIntent().getStringExtra(Constants.CATEGORY_ID);
            categoryDescription = getIntent().getStringExtra(Constants.CATEGORY_DESCRIPTION);
            categoryName = getIntent().getStringExtra(Constants.CATEGORY_NAME);

            if (categoryName != null || categoryDescription != null) {
                textViewCategoryName.setText(categoryName);
                textViewCategoryDescription.setText(categoryDescription);
            }

            if(!categoryId.isEmpty() && categoryId != null) {
                setupFireBaseFoodlistAdapter(categoryId);
                loadSearchSuggestions();
            }
        }
    }

    // Get all the suggestions from firebase DB of that particular food category
    private void loadSearchSuggestions() {
        foodTable.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Food foodItem = snapshot.getValue(Food.class);
                            if (foodItem != null) {
                                // Add the names of the foods to arraylist listSuggestions
                                listSuggestions.add(foodItem.getName());
                            }}
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Common.createToast(getApplicationContext(), "Something went wrong... ");
                        Common.createLog("Database error : " + databaseError.getMessage() + " " + databaseError.getDetails());
                    }
                });
    }


    private void initLayout() {
        recyclerView = findViewById(R.id.recyclerview_food);
        materialSearchBar = findViewById(R.id.material_search_bar);
        textViewCategoryName = findViewById(R.id.tv_category_name);
        textViewCategoryDescription = findViewById(R.id.tv_category_description);
    }

    private void setupRecyclerview() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setupSearchBar() {
        materialSearchBar.setLastSuggestions(listSuggestions);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                List<String> updatedSuggestions = new ArrayList<>();

                for(String search : listSuggestions) {
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())) {
                        updatedSuggestions.add(search);
                    }
                }
                materialSearchBar.setLastSuggestions(updatedSuggestions);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled) {
                    recyclerView.setAdapter(recyclerAdapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                setupFirebaseSearchAdapter(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    // Loads a single food item based on the user search and displays them in a recyclerview
    private void setupFirebaseSearchAdapter(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,
                R.layout.item_food,
                FoodViewHolder.class,
                foodTable.orderByChild("name").equalTo(text.toString())
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, final Food food, int position) {
                foodViewHolder.textViewFood.setText(food.getName());
                Picasso.with(getBaseContext()).load(food.getImage()).into(foodViewHolder.imageViewFood);

                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent foodDetail = new Intent(FoodActivity.this, FoodDetailActivity.class);
                        foodDetail.putExtra(Constants.FOOD_ID, searchAdapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });
            }
        };
        recyclerView.setAdapter(searchAdapter);
    }


    // Loads all the food items in the chosen category from db and displays them in a recyclerview
    private void setupFireBaseFoodlistAdapter(String categoryId) {
        recyclerAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,
                R.layout.item_food,
                FoodViewHolder.class,
                //Query (select * from FoodTable where menuId is equal to the categoryId passed in)
                foodTable.orderByChild("menuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, final Food food, int position) {
                foodViewHolder.textViewFood.setText(food.getName());
                Picasso.with(getBaseContext()).load(food.getImage())
                        .into(foodViewHolder.imageViewFood);

                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent foodDetail = new Intent(FoodActivity.this, FoodDetailActivity.class);
                        foodDetail.putExtra(Constants.FOOD_ID, recyclerAdapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });
            }
        };
        recyclerView.setAdapter(recyclerAdapter);
    }
}
