package com.example.shoppinglist.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.R;
import com.example.shoppinglist.data.DatabaseHandler;
import com.example.shoppinglist.model.Item;
import com.google.android.material.snackbar.Snackbar;

import java.text.MessageFormat;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Item> itemList;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.itemName.setText(item.getName());
        holder.quantity.setText(MessageFormat.format("Qty: {0}", item.getQuantity()));
        holder.dateAdded.setText(MessageFormat.format("Added on: {0}", item.getDateAdded()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView itemName;
        public TextView quantity;
        public TextView dateAdded;
        public int id;
        public Button editButton;
        public Button deleteButton;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            itemName = itemView.findViewById(R.id.name_of_item);
            quantity = itemView.findViewById(R.id.quantity_of_item);
            dateAdded = itemView.findViewById(R.id.date_of_item);

            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            int position;
            position = getAdapterPosition();
            Item item = itemList.get(position);

            switch (v.getId()) {
                case R.id.editButton:
                    editItem(item);
                    break;
                case R.id.deleteButton:
                    deleteItem(item.getId());
                    break;
            }
        }

        private void deleteItem(final int id) {
            builder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_pop, null);

            Button noButton = view.findViewById(R.id.confirmation_no_button);
            Button yesButton = view.findViewById(R.id.confirmation_yes_button);

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(context);
                    db.deleteItem(id);
                    itemList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    dialog.dismiss();
                }
            });

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        private void editItem(final Item newItem) {

            builder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.popup, null);

            Button saveButton;
            final EditText shoppingItem;
            final EditText itemQuantity;
            TextView title;

            shoppingItem = view.findViewById(R.id.item_popup);
            itemQuantity = view.findViewById(R.id.itemQuantity_popup);
            saveButton = view.findViewById(R.id.saveButton_popup);
            saveButton.setText(R.string.update_text);
            title = view.findViewById(R.id.title_popup);

            title.setText(R.string.edit);
            shoppingItem.setText(newItem.getName());
            itemQuantity.setText(String.valueOf(newItem.getQuantity()));

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler databaseHandler = new DatabaseHandler(context);

                    newItem.setName(shoppingItem.getText().toString());
                    newItem.setQuantity(Integer.parseInt(itemQuantity.getText().toString()));

                    if (!shoppingItem.getText().toString().isEmpty() && !itemQuantity.getText().toString().isEmpty()){
                        databaseHandler.updateItem(newItem);
                        notifyItemChanged(getAdapterPosition(), newItem);
                    } else if (shoppingItem.getText().toString().isEmpty() && !itemQuantity.getText().toString().isEmpty()){
                        Snackbar.make(v, "Enter Item Name", Snackbar.LENGTH_SHORT).show();
                    } else if (!shoppingItem.getText().toString().isEmpty() && itemQuantity.getText().toString().isEmpty()){
                        Snackbar.make(v, "Enter Quantity", Snackbar.LENGTH_SHORT).show();
                    } else{
                        Snackbar.make(v, "Empty Fields not Allowed", Snackbar.LENGTH_SHORT).show();
                    }

                    dialog.dismiss();

                }
            });


        }
    }
}
