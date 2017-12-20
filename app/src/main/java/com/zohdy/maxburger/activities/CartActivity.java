package com.zohdy.maxburger.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zohdy.maxburger.R;
import com.zohdy.maxburger.adapters.CartAdapter;
import com.zohdy.maxburger.common.Common;
import com.zohdy.maxburger.database.SQLiteHelper;
import com.zohdy.maxburger.interfaces.Constants;
import com.zohdy.maxburger.interfaces.OnDataChangeListener;
import com.zohdy.maxburger.models.Order;
import com.zohdy.maxburger.models.OrderRequest;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private TextView textViewTotalAmount;

    private Button buttonPlaceOrder;

    private List<Order> cart;

    private CartAdapter cartAdapter;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase database;
    private DatabaseReference requests_table;

    private OrderRequest currentOrderRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database = FirebaseDatabase.getInstance();
        requests_table = database.getReference(Constants.FIREBASE_DB_TABLE_ORDER_REQUESTS);

        recyclerView = findViewById(R.id.rv_cart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        textViewTotalAmount = findViewById(R.id.tv_total_amount);
        buttonPlaceOrder = findViewById(R.id.btn_place_order);

        loadFoodListOrder();
        handlePlaceOrderClick();
    }

    private void handlePlaceOrderClick() {
        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }

    private void loadFoodListOrder() {
        SQLiteHelper dbHelper = SQLiteHelper.getInstance(getApplicationContext());
        cart = dbHelper.getItemsInCart();

        cartAdapter = new CartAdapter(cart, this);
        recyclerView.setAdapter(cartAdapter);

        // Calculate total price
        int totalPrice = 0;
        for (Order order : cart) {
            totalPrice += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
        }
        textViewTotalAmount.setText(String.valueOf(totalPrice) + " kr");

        // Reloads the adapter and re-calculates whenever it's activated from onClick method (deleting an item from cart)
        cartAdapter.setOnDataChangeListener(new OnDataChangeListener() {
            @Override
            public void onDataChanged() {
                loadFoodListOrder();
            }
        });
    }

    private void createRequest(String userInput) {
        currentOrderRequest = new OrderRequest(
                Common.currentUser.getPhoneNumber(),
                textViewTotalAmount.getText().toString(),
                userInput,
                cart);

        //Add to Firebase with currentTimeMillis as document ID
        requests_table.child(String.valueOf(System.currentTimeMillis())).setValue(currentOrderRequest);

        //Clear cart when request is made
        SQLiteHelper dbHelper =  SQLiteHelper.getInstance(getApplicationContext());
        dbHelper.clearCart();

        // set badgecounter back to 0
        Common.badgeCounter = 0;
    }

    private void showAlertDialog() {

        // Create an editText inside the dialog
        final EditText editTextSpecialInstructions = new EditText(CartActivity.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
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
                Common.createToast(CartActivity.this,"Tak - din bestilling er modtaget" );
                finish();


                launchOrderReadyActivity();

            }
        })
        .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }


    private void launchOrderReadyActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent OrderReadyIntent = new Intent(CartActivity.this, OrderReadyActivity.class);
                startActivity(OrderReadyIntent);
                finish();
            }
        }, 10000);
    }

}
