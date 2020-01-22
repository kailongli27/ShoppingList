package com.example.shoppinglist;

import android.content.Intent;
import android.os.Bundle;

import com.example.shoppinglist.data.DatabaseHandler;
import com.example.shoppinglist.model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button saveButton;
    private EditText shoppingItem;
    private EditText itemQuantity;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHandler = new DatabaseHandler(this);

        bypassActivity();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createPopupDialog();
            }
        });
    }

    private void bypassActivity() {
        if (databaseHandler.getItemsCount() > 0){
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }
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
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        },1200);
    }

    private void createPopupDialog() {

        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        shoppingItem = view.findViewById(R.id.item_popup);
        itemQuantity = view.findViewById(R.id.itemQuantity_popup);
        saveButton = view.findViewById(R.id.saveButton_popup);
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

        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
