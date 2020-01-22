package com.example.shoppinglist.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.shoppinglist.model.Item;
import com.example.shoppinglist.util.Constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SHOPPING_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
                + Constants.KEY_ITEM + " INTEGER,"
                + Constants.KEY_QTY_NUMBER + " INTEGER,"
                + Constants.KEY_DATE_NAME + " LONG);";

        db.execSQL(CREATE_SHOPPING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);
    }

    public void addItem(Item item){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_ITEM, item.getName());
        values.put(Constants.KEY_QTY_NUMBER, item.getQuantity());
        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());

        db.insert(Constants.TABLE_NAME, null, values);
    }

    public Item getItem(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME,
                new String[]{Constants.KEY_ID,
                Constants.KEY_ITEM,
                Constants.KEY_QTY_NUMBER,
                Constants.KEY_DATE_NAME},
                Constants.KEY_ID + "=?",
                new String[] {String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Item item = new Item();

        if (cursor != null){
            item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
            item.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM)));
            item.setQuantity(cursor.getInt(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));

            DateFormat dateFormat = DateFormat.getDateInstance();
            String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());

            item.setDateAdded(formattedDate);
        }

        return item;
    }

    public List<Item> getAllItems(){
        SQLiteDatabase db = this.getReadableDatabase();

        List<Item> itemsList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_NAME,
                new String[]{Constants.KEY_ID,
                        Constants.KEY_ITEM,
                        Constants.KEY_QTY_NUMBER,
                        Constants.KEY_DATE_NAME},
                null, null, null, null,
                Constants.KEY_DATE_NAME + " DESC");

        if (cursor.moveToFirst()){
            do {
                Item item = new Item();
                item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                item.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM)));
                item.setQuantity(cursor.getInt(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));

                DateFormat dateFormat = DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());

                item.setDateAdded(formattedDate);

                itemsList.add(item);
            } while (cursor.moveToNext());
        }

        return itemsList;
    }

    public int updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_ITEM, item.getName());
        values.put(Constants.KEY_QTY_NUMBER, item.getQuantity());
        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());

        return db.update(Constants.TABLE_NAME, values,
                Constants.KEY_ID + "=?",
                new String[]{String.valueOf(item.getId())});
    }

    public void deleteItem(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Constants.TABLE_NAME,
                Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)});

        db.close();
    }

    public int getItemsCount(){
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }
}
