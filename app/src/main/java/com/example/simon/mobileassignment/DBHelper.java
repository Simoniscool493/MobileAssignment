package com.example.simon.mobileassignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Simon on 06/11/2016.
 * Database helper class
 */

public class DBHelper //the DBHelper class is a normal version of DBhelper with no huge alterations
{
    private static final String KEY_ROWID 	    = "_id";
    private static final String KEY_SONGNAME 	= "song_name";
    private static final String KEY_SONGKEY   	= "song_key";
    private static final String KEY_SONGLENGTH	= "song_length";

    private static final String DATABASE_NAME 	= "SongDatabase";
    private static final String DATABASE_TABLE 	= "Songs";
    private static final int DATABASE_VERSION 	= 1;

    private static final String DATABASE_CREATE =
            "create table Songs (" +
                    "_id integer primary key autoincrement, " +
                    "song_name text not null, " +
                    "song_key text not null, "  +
                    "song_length integer not null);";

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBHelper(Context c)
    {
        this.context = c;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(DATABASE_CREATE);
            Log.d("Database","Created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion)
        {
            Log.d("Database","Upgraded");
        }
    }

    public DBHelper open() throws SQLException
    {
        this.db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        DBHelper.close();
    }

    public long insertSong(String songName, String songKey, Integer songLength)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_SONGNAME, songName);
        initialValues.put(KEY_SONGKEY, songKey);
        initialValues.put(KEY_SONGLENGTH, songLength);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteSong(long rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor getAllSongs()
    {
        return db.query(DATABASE_TABLE, new String[]
                        {
                                KEY_ROWID,
                                KEY_SONGNAME,
                                KEY_SONGKEY,
                                KEY_SONGLENGTH
                        }, null, null, null, null, null);
    }

    public Cursor getSong(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[]
                                {
                                        KEY_ROWID,
                                        KEY_SONGNAME,
                                        KEY_SONGKEY,
                                        KEY_SONGLENGTH
                                },
                        KEY_ROWID + "=" + rowId,  null, null, null, null, null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateSong(long rowId, String songName, String songKey, Integer songLength)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_SONGNAME, songName);
        args.put(KEY_SONGKEY, songKey);
        args.put(KEY_SONGLENGTH, songLength);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}