package com.nowshowing.backend;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    // if the database schema is changed, the db version must be incremented
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "nowShowing.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table users(username TEXT primary key, email TEXT, password TEXT); ");
        db.execSQL("create Table favourites(fav_id INTEGER PRIMARY KEY, user TEXT NOT NULL, show_id INTEGER NOT NULL, FOREIGN KEY (user)\n" +
                   "       REFERENCES users (username)); ");
        db.execSQL("create Table watched(id INTEGER PRIMARY KEY, user TEXT NOT NULL, show_id INTEGER NOT NULL," +
                   " ep_id INTEGER NOT NULL, FOREIGN KEY (user) REFERENCES users(username)); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists users; ");
        db.execSQL("drop Table if exists favourites;");
        db.execSQL("drop Table if exists watched;");
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }
}
