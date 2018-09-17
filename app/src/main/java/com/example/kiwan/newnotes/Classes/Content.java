package com.example.kiwan.newnotes.Classes;

import android.graphics.Bitmap;

public class Content {
    private String title, subject, date;
    private Bitmap imagePhoto;
    private long _id;
    private byte[] array_voice;

    public byte[] getArray_voice() {
        return array_voice;
    }

    public void setArray_voice( byte[] array_voice ) {
        this.array_voice = array_voice;
    }

    public long get_id() {
        return _id;
    }

    public void set_id( long _id ) {
        this._id = _id;
    }

    public Content( String title, String subject, String date, Bitmap imagePhoto ) {
        this.title = title;
        this.subject = subject;
        this.date = date;
        this.imagePhoto = imagePhoto;
    }

    public Content() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject( String subject ) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate( String date ) {
        this.date = date;
    }

    public Bitmap getImagePhoto() {
        return imagePhoto;
    }

    public void setImagePhoto( Bitmap imagePhoto ) {
        this.imagePhoto = imagePhoto;
    }

}
