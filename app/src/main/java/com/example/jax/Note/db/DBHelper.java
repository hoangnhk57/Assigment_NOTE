package com.example.jax.Note.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.jax.Note.model.NoteInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by Jax on 08-Dec-16.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String TB_NAME = "tb_note";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_NOTE = "note";
    private static final String COLUMN_COLOR = "color";
    private static final String COLUMN_CREATE_AT = "create_at";
    private static final String COLUMN_IMAGE ="image";
    private static final String COLUMN_ALARM ="alarm";

    private static final String DB_NAME = "db_note";
    private static final int DB_VERSION = 1;
/*
    public DBHelper(Context context, String title, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, title, factory, version);
    }*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TB_NAME
                + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_NOTE + " TEXT,"
                + COLUMN_COLOR + " TEXT,"
                + COLUMN_CREATE_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + COLUMN_IMAGE+ " BLOB"
                +")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
            onCreate(db);
        }
    }

    private static DBHelper mDBHelper;

    public static synchronized DBHelper getInstance(Context context) {
        if (mDBHelper == null) {
            mDBHelper = new DBHelper(context.getApplicationContext());
        }
        return mDBHelper;
    }

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    public void insertNote(NoteInfo noteInfo) {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_TITLE, noteInfo.title);
            contentValues.put(COLUMN_NOTE, noteInfo.note);
            contentValues.put(COLUMN_COLOR, noteInfo.color);
            contentValues.put(COLUMN_CREATE_AT, getDateTime());
            contentValues.put(COLUMN_IMAGE, noteInfo.image);
            database.insert(TB_NAME, null, contentValues);
            database.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
    }

    private String getDateTime() {
        SimpleDateFormat  dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public int updateNote(NoteInfo noteInfo, int id) {
        SQLiteDatabase database = getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_TITLE, noteInfo.title);
            contentValues.put(COLUMN_NOTE, noteInfo.note);
            contentValues.put(COLUMN_COLOR, noteInfo.color);
            contentValues.put(COLUMN_CREATE_AT, noteInfo.currentDateTime);
            contentValues.put(COLUMN_IMAGE, noteInfo.image);
           int i = database.update(TB_NAME, contentValues, COLUMN_ID + " = " + id, null );
           return  i;
    }

    public void deleteNote(int id) {
        SQLiteDatabase database = getWritableDatabase();

        try {
            database.beginTransaction();
            database.delete(TB_NAME, COLUMN_ID + "=" + id, null);
            database.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.d("DBHelper", "Error while trying delete");
        } finally {
            database.endTransaction();
        }
    }


    public NoteInfo getSingleNote(int id) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TB_NAME, new String[]
                {COLUMN_ID, COLUMN_TITLE, COLUMN_NOTE,COLUMN_COLOR,COLUMN_CREATE_AT,COLUMN_IMAGE},
                COLUMN_ID + "=?",new String[]{String.valueOf(id)}, null, null, null, null);
        NoteInfo noteInfo = new NoteInfo();
        if (cursor != null && cursor.moveToFirst()) {
            noteInfo.id = id;
            noteInfo.title = cursor.getString(1);
            noteInfo.note = cursor.getString(2);
            noteInfo.color = cursor.getString(3);
            noteInfo.currentDateTime = cursor.getString(4);
            noteInfo.image = cursor.getBlob(5);
        }
        return noteInfo;
    }

    public List<NoteInfo> getAllNote() {
        List<NoteInfo> noteList = new ArrayList<>();
        String SELECT_QUERY = "SELECT * FROM " + TB_NAME +" ORDER BY " + COLUMN_ID +" DESC";

        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {

                    NoteInfo noteInfo = new NoteInfo();
                    noteInfo.title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                    noteInfo.note = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE));
                    noteInfo.color = cursor.getString(cursor.getColumnIndex(COLUMN_COLOR));
                    noteInfo.currentDateTime = cursor.getString(cursor.getColumnIndex(COLUMN_CREATE_AT));
                    noteInfo.image = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));
                    noteInfo.id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));

                    noteList.add(noteInfo);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("DBHelper", "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return noteList;
    }

}
