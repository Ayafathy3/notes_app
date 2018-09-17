package com.example.kiwan.newnotes.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiwan.newnotes.Classes.NotesAdapter;
import com.example.kiwan.newnotes.Classes.NotesHelper;
import com.example.kiwan.newnotes.R;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

public class AddNotes extends AppCompatActivity implements View.OnClickListener, IPickResult {

    //for record voice
    Button recordButton, playButton, stopButton, closeButton;
    Chronometer timer;
    private MediaRecorder myAudioRecorder;
    private LinearLayout linearLayout;
    byte[] fileByteArray;
    public Bitmap imageBitmap;
    TextView save, image, record;
    EditText title, subject;
    String titleText, subjectText, dateString, outputFile;
    ImageView imageView;
    NotesHelper notesHelper;

    private void initialViews() {

        save = findViewById(R.id.save_text);
        title = findViewById(R.id.edit_title);
        subject = findViewById(R.id.edit_subject);
        image = findViewById(R.id.image_text);
        record = findViewById(R.id.voice_text);
        imageView = findViewById(R.id.image_photo);

        recordButton = findViewById(R.id.record_button);
        playButton = findViewById(R.id.play_button);
        stopButton = findViewById(R.id.stop_button);
        closeButton = findViewById(R.id.close_button);
        timer = findViewById(R.id.timer);
        linearLayout = findViewById(R.id.linear_bootom);
    }

    private void listeners() {

        playButton.setEnabled(false);
        stopButton.setEnabled(false);

        save.setOnClickListener(this);
        image.setOnClickListener(this);
        record.setOnClickListener(this);
        recordButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        closeButton.setOnClickListener(this);
    }

    private void objects() {
        notesHelper = new NotesHelper(this);

    }

    private static byte[] getBytes( Bitmap bitmap ) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public byte[] getVoice( String outputFiles ) {

        try {
            FileInputStream fis = new FileInputStream(outputFiles);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, read);
            }
            baos.flush();
            fileByteArray = baos.toByteArray();


        } catch (Exception e) {

        }
        return fileByteArray;
    }

    private void saveButton() {

        titleText = title.getText().toString();
        subjectText = subject.getText().toString();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aaa");

        // the time of write notes
        dateString = sdf.format(cal.getTime());

        if (!titleText.isEmpty() || !subjectText.isEmpty() || imageBitmap != null || outputFile != null) {

            if (imageBitmap != null && outputFile != null) {
                notesHelper.insertDataImageVoice(titleText, subjectText, dateString, getBytes(imageBitmap), getVoice(outputFile));

            } else if (imageBitmap != null && outputFile == null) {
                notesHelper.insertDataImage(titleText, subjectText, dateString, getBytes(imageBitmap));
            } else if (imageBitmap == null && outputFile != null) {
                notesHelper.insertDataVoice(titleText, subjectText, dateString, getVoice(outputFile));

            } else {
                notesHelper.insertData(titleText, subjectText, dateString);

            }

        }
        Intent intent = new Intent(AddNotes.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        initialViews();
        listeners();
        objects();


        // record voice
        outputFile = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/"
                + UUID.randomUUID().toString() + "_audio_record.3gp";

        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);

        myAudioRecorder.setOutputFile(outputFile);


    }


    @Override
    public void onClick( View v ) {
        switch (v.getId()) {
            case R.id.save_text:
                saveButton();
                break;

            case R.id.image_text:
                PickImageDialog.build(new PickSetup()).show(this);
                break;

            case R.id.voice_text:
                linearLayout.setVisibility(View.VISIBLE);
                closeButton.setVisibility(View.VISIBLE);
                //hide keyboard :
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                break;

            case R.id.record_button:
                recordAction();
                break;

            case R.id.stop_button:
                stopVoiceAction();
                break;

            case R.id.play_button:
                playVoiceAction();
                break;

            case R.id.close_button:
                closeAction();
                break;
        }
    }

    private void closeAction() {

        linearLayout.setVisibility(View.INVISIBLE);
        closeButton.setVisibility(View.INVISIBLE);
        if (myAudioRecorder != null && timer != null) {
            myAudioRecorder.release();
            myAudioRecorder = null;
            timer.stop();
        }

    }

    private void playVoiceAction() {
        MediaPlayer mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(outputFile);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(AddNotes.this, "Playing Audio", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            // make something
        }
    }

    private void stopVoiceAction() {

        try {
            myAudioRecorder.stop();
            myAudioRecorder.release();
            myAudioRecorder = null;
            recordButton.setEnabled(true);
            stopButton.setEnabled(false);
            playButton.setEnabled(true);
            timer.stop();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        Toast.makeText(AddNotes.this, "Audio Recorder successfully", Toast.LENGTH_LONG).show();

    }

    private void recordAction() {

        try {
            timer.setBase(SystemClock.elapsedRealtime());
            myAudioRecorder.prepare();
            myAudioRecorder.start();
            timer.start();

        } catch (IllegalStateException ise) {

//            Toast.makeText(this, ise.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException ioe) {

            Toast.makeText(this, ioe.getMessage(), Toast.LENGTH_LONG).show();
        }
        recordButton.setEnabled(false);
        stopButton.setEnabled(true);
        Toast.makeText(AddNotes.this, "Recording started", Toast.LENGTH_LONG).show();

    }


    @Override
    public void onPickResult( PickResult pickResult ) {
        if (pickResult.getError() == null) {

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0, imageView.getBottom(), 0, 0);
            subject.setLayoutParams(layoutParams);
            subject.setHint("");
            subject.requestFocus();

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(subject, InputMethodManager.SHOW_IMPLICIT);

            imageView.setVisibility(View.VISIBLE);
            imageBitmap = pickResult.getBitmap();
            imageView.setImageBitmap(imageBitmap);

        } else {
            Toast.makeText(AddNotes.this, pickResult.getError().toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
