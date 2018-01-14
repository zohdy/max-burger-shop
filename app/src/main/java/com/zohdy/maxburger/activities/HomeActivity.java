package com.zohdy.maxburger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.zohdy.maxburger.R;
import com.zohdy.maxburger.common.Common;
import com.zohdy.maxburger.interfaces.Constants;
import com.zohdy.maxburger.interfaces.RecyclerViewItemClickListener;
import com.zohdy.maxburger.models.Category;
import com.zohdy.maxburger.viewholders.CategoryViewHolder;

/**
 * Created by peterzohdy on 06/11/2017.
 */

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference categoryTable;
    private FirebaseRecyclerAdapter<Category, CategoryViewHolder> recyclerAdapter;
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar_category);

        //Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        categoryTable = database.getReference(Constants.FIREBASE_DB_TABLE_CATEGORY);

        initLayout();
        setupRecyclerView();

        loadCategoryMenu();
    }

    private void initLayout() {

        // Toggle Navigation
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Nav view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set current logged in user in nav header
        View headerView = navigationView.getHeaderView(0);
        TextView textViewFullName = headerView.findViewById(R.id.tv_full_name);
        textViewFullName.setText(Common.currentUser.getName());

        // Go directly to cart from homescreen
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(cartIntent);
            }
        });
    }

    private void setupRecyclerView() {
        //Init recyclerview
        recyclerView = findViewById(R.id.recyclerview_category);
        recyclerView.setHasFixedSize(true);
        LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
    }

    // Load the category items from Firebase into Recyclerview
    private void loadCategoryMenu() {
        recyclerAdapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(
                Category.class,
                R.layout.item_category,
                CategoryViewHolder.class,
                categoryTable) {
                    @Override
                    protected void populateViewHolder(CategoryViewHolder categoryViewHolder, final Category category, int position) {
                        categoryViewHolder.textViewCategoryName.setText(category.getName());
                        Picasso.with(getBaseContext()).load(category.getImage())
                                .into(categoryViewHolder.imageViewCategory);

                        categoryViewHolder.setRecyclerViewItemClickListener(new RecyclerViewItemClickListener() {
                            @Override
                            public void onRecyclerItemClick(View view, int position) {
                                // Go to FoodActivity and pass along extra data for the selected category
                                Intent foodIntent = new Intent(HomeActivity.this, FoodActivity.class);
                                foodIntent.putExtra(Constants.CATEGORY_NAME, category.getName());
                                foodIntent.putExtra(Constants.CATEGORY_DESCRIPTION, category.getDescription());
                                foodIntent.putExtra(Constants.CATEGORY_ID, recyclerAdapter.getRef(position).getKey());
                                startActivity(foodIntent);
                            }
                        });
                    }
                };
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    // Handle navigation view item clicks here.
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.nav_cart:
                // Navigate to shopping cart
                Intent cartIntent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(cartIntent);

                break;
            case R.id.nav_orders:
                // Navigate to Order menu
                Intent orderIntent = new Intent(HomeActivity.this, OrderActivity.class);
                startActivity(orderIntent);

                break;
            case R.id.nav_log_out:
                // Logout User
                Intent logoutIntent = new Intent(HomeActivity.this, SignInActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logoutIntent);
                break;
        }
        return true;
    }
}