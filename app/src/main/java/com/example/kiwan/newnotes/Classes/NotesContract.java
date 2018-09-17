package com.example.kiwan.newnotes.Classes;

import android.provider.BaseColumns;

public class NotesContract {
    private NotesContract() {
    }

    public static class NotesEntry implements BaseColumns {


        public static final String _ID = "id";
        public static final String TABLE_NAME = "notes";
        public static final String TITLE = "title";
        public static final String SUBJECT = "subject";
        public static final String DATE = "date";
        public static final String COLUMN_TIME_STAMP = "timestamp";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_VOICE = "voice";

    }
}
