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

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zohdy.maxburger.R;
import com.zohdy.maxburger.adapters.CartAdapter;
import com.zohdy.maxburger.common.Common;
import com.zohdy.maxburger.database.DBOpenHelper;
import com.zohdy.maxburger.interfaces.OnDataChangeListener;
import com.zohdy.maxburger.models.Order;
import com.zohdy.maxburger.models.Request;
import com.zohdy.maxburger.services.HandleOrderService;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase database;
    private DatabaseReference requests_table;

    private TextView textViewTotalAmount;
    private Button buttonPlaceOrder;

    private List<Order> cart;
    private CartAdapter cartAdapter;

    private Request currentRequest;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        handler = new Handler();

        database = FirebaseDatabase.getInstance();
        requests_table = database.getReference("Requests");

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
        DBOpenHelper dbOpenHelper = DBOpenHelper.newInstance(getApplicationContext());
        cart = dbOpenHelper.getItemsInCart();

        cartAdapter = new CartAdapter(cart, this);
        recyclerView.setAdapter(cartAdapter);

        // Calculate total price
        int totalPrice = 0;
        for (Order order : cart) {
            totalPrice += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
        }
        textViewTotalAmount.setText(String.valueOf(totalPrice) + " kr");

        // Reloads the adapter and re-calculates whenever it's activated from onClick method in cartAdapter
        cartAdapter.setOnDataChangeListener(new OnDataChangeListener() {
            @Override
            public void onDataChanged() {
                loadFoodListOrder();
            }
        });
    }

    private void createRequest(String userInput) {
        currentRequest = new Request(
                Common.currenUser.getName(),
                Common.currenUser.getPhoneNumber(),
                Common.currenUser.getEmail(),
                textViewTotalAmount.getText().toString(),
                userInput,
                cart);

        //Add to Firebase with currentTimeMillis as ID
        requests_table.child(String.valueOf(System.currentTimeMillis())).setValue(currentRequest);

        //Clear cart
        DBOpenHelper dbOpenHelper = DBOpenHelper.newInstance(getApplicationContext());
        dbOpenHelper.clearCart();

        Common.badgeCounter = 0;
    }

    private void showAlertDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CartActivity.this);
        alertDialog.setTitle("Før du bestiller...");
        alertDialog.setMessage("Er der noget vi skal tage højde for ifm. din ordre?");

        final EditText editTextSpecialInstructions = new EditText(CartActivity.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        editTextSpecialInstructions.setLayoutParams(layoutParams);
        alertDialog.setView(editTextSpecialInstructions);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("Bestil", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createRequest(editTextSpecialInstructions.getText().toString());
                Toast.makeText(CartActivity.this, "Tak - din bestilling er modtaget", Toast.LENGTH_SHORT).show();
                finish();
                launchReadyOrderActivity();
            }
        });

        alertDialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void launchReadyOrderActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent readyOrderIntent = new Intent(CartActivity.this, OrderReadyActivity.class);
                startActivity(readyOrderIntent);
                finish();
            }
        }, 10000);
    }
}
