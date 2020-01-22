package com.example.shoppinglist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.shoppinglist.data.DatabaseHandler;
import com.example.shoppinglist.model.Item;
import com.example.shoppinglist.ui.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Item> itemList;
    private DatabaseHandler databaseHandler;
    private FloatingActionButton fab;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private EditText shoppingItem;
    private EditText itemQuantity;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab);
        databaseHandler = new DatabaseHandler(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemList = new ArrayList<>();

        itemList = databaseHandler.getAllItems();

        recyclerViewAdapter = new RecyclerViewAdapter(this, itemList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopDialog();
            }
        });
    }

    private void createPopDialog() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        shoppingItem = view.findViewById(R.id.item_popup);
        itemQuantity = view.findViewById(R.id.itemQuantity_popup);
        saveButton = view.findViewById(R.id.saveButton_popup);

        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!shoppingItem.getText().toString().isEmpty() && !itemQuantity.getText().toString().isEmpty()){
                    saveItem(v);
                } else if (shoppingItem.getText().toString().isEmpty() && !itemQuantity.getText().toString().isEmpty()){
                    Snackbar.make(v, "Enter Item Name", Snackbar.LENGTH_SHORT).show();
                } else if (!shoppingItem.getText().toString().isEmpty() && itemQuantity.getText().toString().isEmpty()){
                    Snackbar.make(v, "Enter Quantity", Snackbar.LENGTH_SHORT).show();
                } else{
                    Snackbar.make(v, "Empty Fields not Allowed", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveItem(View view) {
        // save item to our database
        Item item = new Item();

        String newItem = shoppingItem.getText().toString().trim();
        int quantity = Integer.parseInt(itemQuantity.getText().toString().trim());

        item.setName(newItem);
        item.setQuantity(quantity);

        databaseHandler.addItem(item);

        Snackbar.make(view, "Item Saved", Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
                startActivity(new Intent(ListActivity.this, ListActivity.class));
                finish();
            }
        },1200);
    }
}
