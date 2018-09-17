package com.example.kiwan.newnotes.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiwan.newnotes.Classes.NotesContract.NotesEntry;

import com.example.kiwan.newnotes.Classes.NotesContract;
import com.example.kiwan.newnotes.Classes.NotesHelper;
import com.example.kiwan.newnotes.R;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class ShowNotes extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    EditText titleShow, subjectShow;
    ImageView imageViewShow;
    TextView more, saveShow;
    Cursor cursor;
    NotesHelper notesHelper;
    SQLiteDatabase sqLiteDatabase;
    String idd;
    RelativeLayout relativeLayout;
    ImageView play;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notes);

        more = findViewById(R.id.moreShow);
        titleShow = findViewById(R.id.titleShow);
        subjectShow = findViewById(R.id.subjectShow);
        imageViewShow = findViewById(R.id.imageShow);
        saveShow = findViewById(R.id.saveShow);
        relativeLayout = findViewById(R.id.relative_butoom_show);
        play = findViewById(R.id.play_button_show);


        notesHelper = new NotesHelper(this);
        sqLiteDatabase = notesHelper.getWritableDatabase();


        //hide keyboard :
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final Intent intent = getIntent();
        long _id = intent.getLongExtra("idd", 1);

        idd = String.valueOf(_id);
        cursor = getData();
        if (!cursor.moveToNext()) {
            return;
        }

        String title = cursor.getString(cursor.getColumnIndex(NotesEntry.TITLE));
        String subject = cursor.getString(cursor.getColumnIndex(NotesEntry.SUBJECT));
        byte[] image = cursor.getBlob(cursor.getColumnIndex(NotesEntry.COLUMN_IMAGE));
        final byte[] voice = cursor.getBlob(cursor.getColumnIndex(NotesEntry.COLUMN_VOICE));


        titleShow.setText(title);
        subjectShow.setText(subject);

        if (image != null) {

            imageViewShow.setVisibility(View.VISIBLE);
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            imageViewShow.setImageBitmap(imageBitmap);

        }

        if (voice != null) {
            relativeLayout.setVisibility(View.VISIBLE);
            play.setVisibility(View.VISIBLE);

        }

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(new String(voice));
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Toast.makeText(ShowNotes.this, "Playing Audio", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(ShowNotes.this, e.getMessage(), Toast.LENGTH_LONG
                    ).show();
                }
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                showPopup(v);
            }
        });

        saveShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

                String title = titleShow.getText().toString();
                String subject = subjectShow.getText().toString();

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aaa");

                // the time of write notes
                String date = sdf.format(cal.getTime());

                updateData(idd, title, subject, date);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                Intent intent1 = new Intent(ShowNotes.this, MainActivity.class);
                startActivity(intent1);
            }
        });
    }

    public Cursor getData() {
        String[] selectionArgs = new String[]{idd};
        return sqLiteDatabase.query(NotesContract.NotesEntry.TABLE_NAME,
                null,
                "id=?",
                selectionArgs,
                null,
                null,
                null,
                null);
    }

    public int updateData( String old_id, String new_title, String new_subject, String new_date ) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotesEntry.TITLE, new_title);
        contentValues.put(NotesEntry.SUBJECT, new_subject);
        contentValues.put(NotesEntry.DATE, new_date);

        String selection = NotesEntry._ID + " LIKE ?";
        String selection_args[] = {old_id};

        return sqLiteDatabase.update(NotesEntry.TABLE_NAME, contentValues, selection, selection_args);
    }

    public void showPopup( View v ) {

        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_show, popup.getMenu());
        popup.show();

    }

    @Override
    public boolean onMenuItemClick( MenuItem item ) {
        switch (item.getItemId()) {
            case R.id.editMenu:
                editAction();
                return true;

            case R.id.shareMenu:
                shareAction();
                return true;

            default:
                return false;
        }

    }

    private void shareAction() {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String value_share_text = subjectShow.getText().toString();
        sendIntent.putExtra(Intent.EXTRA_TEXT, value_share_text);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share Notes"));

    }

    private void editAction() {

        saveShow.setVisibility(View.VISIBLE);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

    }

}
