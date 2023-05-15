package com.nowshowing.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDBHelper extends DBHelper{
    public static final String TABLE_NAME = "users";
    public UserDBHelper(Context context) {
        super(context);
    }

    public Boolean insertUser(String username, String email, String password){
        SQLiteDatabase db = super.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("email", email);
        values.put("password", password);

        long result = db.insert(TABLE_NAME, null, values);
        // if result is -1, there was an error when inserting the data
        return result != -1;
    }

    public Boolean checkUsername(String username){
        // checks whether the username exists already, since usernames must be unique
        SQLiteDatabase db = super.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                "username = ?",
                new String[] {username},
                null,
                null,
                "username ASC"
        );
        int count = cursor.getCount();
        cursor.close(); // free up resources before leaving the method
        return count > 0;
    }

    public Boolean userExists(String username, String password){
        // checks whether the entered credentials match
        SQLiteDatabase db = super.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                "username = ? and password = ?",
                new String[] {username, password},
                null,
                null,
                "username ASC"
        );
        int count = cursor.getCount();
        cursor.close(); // free up resources before leaving the method
        return count > 0;
    }
}
