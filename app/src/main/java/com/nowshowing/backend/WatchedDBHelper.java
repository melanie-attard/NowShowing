package com.nowshowing.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WatchedDBHelper extends DBHelper{
    public static final String TABLE_NAME = "watched";

    public WatchedDBHelper(Context context) { super(context); }

    public Boolean insertEpisode(String username, int show_id, int ep_id){
        SQLiteDatabase db = super.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user", username);
        values.put("show_id", show_id);
        values.put("ep_id", ep_id);

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public Boolean isWatched(String username, int ep_id){
        // checks whether the given episode has been watched
        SQLiteDatabase db = super.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                "user = ? and ep_id = ?",
                new String[] {username, String.valueOf(ep_id)},
                null,
                null,
                "id ASC"
        );
        int count = cursor.getCount();
        cursor.close(); // free up resources before leaving the method
        return count > 0;
    }

    public Boolean remove(String username, int ep_id){
        // remove the row containing the given episode
        SQLiteDatabase db = super.getWritableDatabase();
        int result = db.delete(TABLE_NAME, "user = ? and ep_id = ?", new String[]{username, String.valueOf(ep_id)});
        // at least 1 row must be affected for the deletion to be successful
        return result > 0;
    }

    public int getWatchedCount(String username, int show_id){
        // count how many episodes the user has watched belonging to the given show
        SQLiteDatabase db = super.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                "user = ? and show_id = ?",
                new String[] {username, String.valueOf(show_id)},
                null,
                null,
                "id ASC"
        );
        int count = cursor.getCount();
        cursor.close(); // free up resources before leaving the method
        return count;
    }

    public boolean resetShow(String username, int show_id){
        // remove all episodes corresponding to the given show from watched
        SQLiteDatabase db = super.getWritableDatabase();
        int result = db.delete(TABLE_NAME, "user = ? and show_id = ?", new String[]{username, String.valueOf(show_id)});
        // at least 1 row must be affected for the deletion to be successful
        return result > 0;
    }
}
