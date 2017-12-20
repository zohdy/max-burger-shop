package com.zohdy.maxburger.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
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

    private TextView textViewCategoryDescription;
    private TextView textViewCategoryName;

    private String categoryId;
    private String categoryDescription;
    private String categoryName;

    private List<String> listSuggestions;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase database;
    private DatabaseReference foodTable;
    private FirebaseRecyclerAdapter<Food, FoodViewHolder> foodListRecyclerAdapter;
    private FirebaseRecyclerAdapter<Food, FoodViewHolder> searchBarRecyclerAdapter;

    private MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        // Used for generating matches through the searchbar
        listSuggestions = new ArrayList<>();

        initLayout();
        setupRecyclerview();
        setupSearchBar();


        database = FirebaseDatabase.getInstance();
        foodTable = database.getReference(Constants.FIREBASE_DB_TABLE_FOOD);

        // Retrieve the extra Intent data from HomeActivity
        if(getIntent() != null) {
            categoryId = getIntent().getStringExtra(Constants.CATEGORY_ID);
            categoryDescription = getIntent().getStringExtra(Constants.CATEGORY_DESCRIPTION);
            categoryName = getIntent().getStringExtra(Constants.CATEGORY_NAME);

            // Fill the textviews with the data passed through the Intent
            if (categoryName != null || categoryDescription != null) {
                textViewCategoryName.setText(categoryName);
                textViewCategoryDescription.setText(categoryDescription);
            }

            // Load the adapter with the items responding to the categoryId
            if(!categoryId.isEmpty() && categoryId != null) {
                setupFireBaseFoodlistAdapter(categoryId);
                loadSearchSuggestions();
            }
        }
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


    /*********************************** SEARCH BAR ************************************************  */

    // Get all the suggestions from firebase DB of that particular food category and store it in listSuggestions arraylist
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

                // check if user input matches any of the items in listSuggestions, if so, add them to updatedSuggestions arraylisted
                for(String search : listSuggestions) {
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())) {
                        updatedSuggestions.add(search);
                    }
                }

                // set the searchbar to display the items in updatedSuggestions arraylist
                materialSearchBar.setLastSuggestions(updatedSuggestions);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override

            // Whenever a new search is performed, set the adapter to display the correct item
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled) {
                    recyclerView.setAdapter(foodListRecyclerAdapter);
                }
            }

            // init the searchAdapter when the match is confirmed by the user
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
        searchBarRecyclerAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
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
                        foodDetail.putExtra(Constants.FOOD_ID, searchBarRecyclerAdapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });
            }
        };
        recyclerView.setAdapter(searchBarRecyclerAdapter);
    }


    /*********************************** SEARCH BAR FINISH *****************************************  */


    // Loads all the food items in the chosen category from db and displays them in a recyclerview
    private void setupFireBaseFoodlistAdapter(String categoryId) {
        foodListRecyclerAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,
                R.layout.item_food,
                FoodViewHolder.class,
                // Display the items where menuId in foodTable is identical to the categoryId passed in from the intent extra
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
                        foodDetail.putExtra(Constants.FOOD_ID, foodListRecyclerAdapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });
            }
        };
        recyclerView.setAdapter(foodListRecyclerAdapter);
    }
}
