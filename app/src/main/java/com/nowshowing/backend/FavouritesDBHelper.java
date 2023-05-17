package com.nowshowing.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class FavouritesDBHelper extends DBHelper{
    public static final String TABLE_NAME = "favourites";

    public FavouritesDBHelper(Context context) { super(context); }

    public Boolean insertFavourite(String username, int show_id){
        SQLiteDatabase db = super.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user", username);
        values.put("show_id", show_id);

        long result = db.insert(TABLE_NAME, null, values);
        // if result is -1, there was an error when inserting the data
        return result != -1;
    }

    public List<Integer> getFavourites(String user){
        ArrayList<Integer> shows = new ArrayList<>();
        // gets the shows corresponding to the given user
        SQLiteDatabase db = super.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                new String[] {"show_id"},
                "user = ?",
                new String[] {user},
                null,
                null,
                "fav_id ASC"
        );

        // add the returned rows to the list
        while(cursor.moveToNext()) {
            int show_id = cursor.getInt(cursor.getColumnIndexOrThrow("show_id"));
            shows.add(show_id);
        }
        cursor.close();
        return shows;
    }
}
