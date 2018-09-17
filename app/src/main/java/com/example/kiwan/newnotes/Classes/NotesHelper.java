package com.example.kiwan.newnotes.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.kiwan.newnotes.R;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class NotesHelper extends SQLiteOpenHelper {

    Content content;
    public static final String DATABASE_NAME = "note.dp";
    public static final int DATABASE_VERSION = 1;

    public NotesHelper( Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate( SQLiteDatabase sqliteDatabase ) {

        String SQL_CREATE_PETS_TABLE = "CREATE TABLE " + NotesContract.NotesEntry.TABLE_NAME +
                " (" + NotesContract.NotesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NotesContract.NotesEntry.TITLE + " TEXT, " +
                NotesContract.NotesEntry.SUBJECT + " TEXT NOT NULL, " +
                NotesContract.NotesEntry.DATE + " TEXT NOT NULL, " +
                NotesContract.NotesEntry.COLUMN_IMAGE + " BLOB, " +
                NotesContract.NotesEntry.COLUMN_VOICE + " BLOB, " +
                NotesContract.NotesEntry.COLUMN_TIME_STAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        // Execute the SQL statement
        sqliteDatabase.execSQL(SQL_CREATE_PETS_TABLE);


    }

    @Override
    public void onUpgrade( SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion ) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NotesContract.NotesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    public void insertDataImage( String title, String subject, String date, byte[] image ) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(NotesContract.NotesEntry.TITLE, title);
        contentValues.put(NotesContract.NotesEntry.SUBJECT, subject);
        contentValues.put(NotesContract.NotesEntry.DATE, date);
        contentValues.put(NotesContract.NotesEntry.COLUMN_IMAGE, image);
        sqLiteDatabase.insert(NotesContract.NotesEntry.TABLE_NAME, null, contentValues);
    }

    public void insertDataImageVoice( String title, String subject, String date, byte[] image, byte[] voice ) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(NotesContract.NotesEntry.TITLE, title);
        contentValues.put(NotesContract.NotesEntry.SUBJECT, subject);
        contentValues.put(NotesContract.NotesEntry.DATE, date);
        contentValues.put(NotesContract.NotesEntry.COLUMN_IMAGE, image);
        contentValues.put(NotesContract.NotesEntry.COLUMN_VOICE, voice);
        sqLiteDatabase.insert(NotesContract.NotesEntry.TABLE_NAME, null, contentValues);
    }


    public void insertData( String title, String subject, String date ) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(NotesContract.NotesEntry.TITLE, title);
        contentValues.put(NotesContract.NotesEntry.SUBJECT, subject);
        contentValues.put(NotesContract.NotesEntry.DATE, date);
        contentValues.put(NotesContract.NotesEntry.COLUMN_IMAGE, "");
        contentValues.put(NotesContract.NotesEntry.COLUMN_VOICE, "");
        sqLiteDatabase.insert(NotesContract.NotesEntry.TABLE_NAME, null, contentValues);
    }

    public void insertDataVoice( String title, String subject, String date, byte[] voice ) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(NotesContract.NotesEntry.TITLE, title);
        contentValues.put(NotesContract.NotesEntry.SUBJECT, subject);
        contentValues.put(NotesContract.NotesEntry.DATE, date);
        contentValues.put(NotesContract.NotesEntry.COLUMN_IMAGE, "");
        contentValues.put(NotesContract.NotesEntry.COLUMN_VOICE, voice);
        sqLiteDatabase.insert(NotesContract.NotesEntry.TABLE_NAME, null, contentValues);
    }

    public ArrayList<Content> getData() {

        ArrayList<Content> data = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(NotesContract.NotesEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                NotesContract.NotesEntry.COLUMN_TIME_STAMP + " DESC");

        while (cursor.moveToNext()) {
            content = new Content();

            String title = cursor.getString(cursor.getColumnIndex(NotesContract.NotesEntry.TITLE));
            String subject = cursor.getString(cursor.getColumnIndex(NotesContract.NotesEntry.SUBJECT));
            String date = cursor.getString(cursor.getColumnIndex(NotesContract.NotesEntry.DATE));
            byte[] image_array = cursor.getBlob(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_IMAGE));
            byte[] voice_array = cursor.getBlob(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_VOICE));
            final int id = cursor.getInt(cursor.getColumnIndex(NotesContract.NotesEntry._ID));

            content.setTitle(title);
            content.setSubject(subject);
            content.setDate(date);
            content.set_id(id);
            if (image_array != null) {
                content.setImagePhoto(getImage(image_array));
            }
            if (voice_array != null) {
                content.setArray_voice(voice_array);
            }
            data.add(content);
        }

        return data;
    }


    private String toCommaSeparatedString( List<Long> list ) {

        if (list.size() > 0) {
            StringBuilder nameBuilder = new StringBuilder();
            for (Long item : list) {
                nameBuilder.append(item).append(", ");
            }
            nameBuilder.deleteCharAt(nameBuilder.length() - 1);
            nameBuilder.deleteCharAt(nameBuilder.length() - 1);
            return nameBuilder.toString();
        } else {
            return "";
        }
    }

    public void deleteData( List<Long> ids ) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        String args = toCommaSeparatedString(ids);
        sqLiteDatabase.execSQL(String.format("DELETE FROM " + NotesContract.NotesEntry.TABLE_NAME + " WHERE " + NotesContract.NotesEntry._ID + " IN (%s);", args));
    }

    private Bitmap getImage( byte[] image_array ) {
        return BitmapFactory.decodeByteArray(image_array, 0, image_array.length);
    }

}
