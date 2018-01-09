package com.zohdy.maxburger.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zohdy.maxburger.R;
import com.zohdy.maxburger.adapters.CartAdapter;
import com.zohdy.maxburger.common.Common;
import com.zohdy.maxburger.database.DatabaseHelper;
import com.zohdy.maxburger.interfaces.Constants;
import com.zohdy.maxburger.interfaces.RecyclerViewItemClickListener;
import com.zohdy.maxburger.models.Order;
import com.zohdy.maxburger.models.OrderRequest;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private TextView textViewTotalAmount;
    private Button buttonPlaceOrder;
    private List<Order> cart;
    private RecyclerView recyclerView;
    private DatabaseReference orderRequestTable;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        orderRequestTable = database.getReference(Constants.FIREBASE_DB_TABLE_ORDER_REQUESTS);

        initLayout();
        setRecyclerView();

        loadFoodListOrder();
        handlePlaceOrderClick();
    }

    private void initLayout() {
        textViewTotalAmount = findViewById(R.id.tv_total_amount);
        buttonPlaceOrder = findViewById(R.id.btn_place_order);
    }

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.rv_cart);
        recyclerView.setHasFixedSize(true);
        LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void handlePlaceOrderClick() {
        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }

    private void showAlertDialog() {
        // Create an editText inside the dialog
        final EditText editTextSpecialInstructions = new EditText(CartActivity.this);
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
        editTextSpecialInstructions.setLayoutParams(layoutParams);

        new AlertDialog.Builder(CartActivity.this)
                .setTitle("Før du bestiller...")
                .setMessage("Er der noget vi skal tage højde for ifm. din ordre?")
                .setView(editTextSpecialInstructions)
                .setIcon(R.drawable.ic_shopping_cart_black_24dp)
                .setPositiveButton("Bestil", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createRequest(editTextSpecialInstructions.getText().toString());
                        Intent orderConfirmationActivity = new Intent(CartActivity.this, OrderConfirmationActivity.class);
                        startActivity(orderConfirmationActivity);
                        finish();
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    // Loading foodlist from SQLite database
    private void loadFoodListOrder() {
        databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
        cart = databaseHelper.getItemsInCart();

        // Calculate the total price of shopping cart items
        int totalPrice = 0;
        for (Order order : cart) {
            totalPrice += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
        }
        textViewTotalAmount.setText(String.valueOf(totalPrice) + " kr");

        CartAdapter cartAdapter = new CartAdapter(cart, this);
        recyclerView.setAdapter(cartAdapter);

        // Handles the trash/delete icon on the recycler item
        cartAdapter.setOnRecyclerViewClicklickListener(new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                databaseHelper = DatabaseHelper.getInstance(CartActivity.this);
                databaseHelper.deleteOrderItem(cart, position);

                // Update the counter on the cartImage correctly
                int numOfItemsToRemove = Integer.parseInt(cart.get(position).getQuantity());
                Common.badgeCounter -= numOfItemsToRemove;

                // By calling the same method again it recalculates the price and remove the cart item from the list
                loadFoodListOrder();
            }
        });
    }

    // Create a new OrderRequest object
    private void createRequest(String userInput) {
        OrderRequest currentOrderRequest = new OrderRequest(
                Common.currentUser.getPhoneNumber(),
                textViewTotalAmount.getText().toString(),
                userInput,
                cart);

        // Add to Firebase with currentTimeMillis as document ID
        String orderId = String.valueOf(System.currentTimeMillis());
        orderRequestTable.child(orderId).setValue(currentOrderRequest);

        // Clear shoppingcart after OrderRequest is made
        databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
        databaseHelper.clearCart();

        // set badgecounter back to 0
        Common.badgeCounter = 0;
    }

}


