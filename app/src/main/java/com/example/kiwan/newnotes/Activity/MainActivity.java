package com.example.kiwan.newnotes.Activity;


import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.kiwan.newnotes.Classes.Content;
import com.example.kiwan.newnotes.Classes.NotesAdapter;
import com.example.kiwan.newnotes.Classes.NotesHelper;
import com.example.kiwan.newnotes.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public boolean isChecked = false;
    FloatingActionButton addNotes;
    public CheckBox checkAll;
    public TextView delete, share, counterText;
    public boolean is_in_action_mode;
    NotesAdapter notesadapter;
    NotesHelper notesHelper;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ArrayList<Content> arrayList;

    private void initialViews() {

        // find view in xml
        delete = findViewById(R.id.delete);
        share = findViewById(R.id.share);
        counterText = findViewById(R.id.count);
        checkAll = findViewById(R.id.checkallbox);


        recyclerView = findViewById(R.id.recycler);

        addNotes = findViewById(R.id.add_notes);

    }

    private void objects() {

        notesHelper = new NotesHelper(this);
        arrayList = new ArrayList<Content>();
        arrayList = notesHelper.getData();
        notesadapter = new NotesAdapter(this, arrayList);

    }

    private void listeners() {

        addNotes.setOnClickListener(this);
        delete.setOnClickListener(this);
        share.setOnClickListener(this);
    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialViews();
        objects();
        listeners();

        // make item in menu are invisible
        delete.setVisibility(View.INVISIBLE);
        share.setVisibility(View.INVISIBLE);
        counterText.setVisibility(View.INVISIBLE);
        checkAll.setVisibility(View.INVISIBLE);


        // recycler view

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);


        recyclerView.setAdapter(notesadapter);
        recyclerView.setLayoutManager(linearLayoutManager);


        //this is the code of Tool_bar
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Awesome Notes");
        toolbar.setTitleTextColor(Color.DKGRAY);
    }

    public void doOnLongClick() {
        toolbar.getMenu().clear();

        checkAll.setVisibility(View.VISIBLE);
        counterText.setVisibility(View.VISIBLE);
        is_in_action_mode = true;

        notesadapter.notifyDataSetChanged();

    }

    public void clickCheckBox() {

        share.setVisibility(View.VISIBLE);
        delete.setVisibility(View.VISIBLE);

    }


    @Override
    public void onClick( View v ) {
        switch (v.getId()) {
            case R.id.add_notes:
                Intent intent = new Intent(MainActivity.this, AddNotes.class);
                startActivity(intent);

                break;

        }
    }
}

