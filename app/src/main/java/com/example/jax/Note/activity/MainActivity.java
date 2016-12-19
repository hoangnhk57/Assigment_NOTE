package com.example.jax.Note.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;


import com.example.jax.Note.Consts;
import com.example.jax.Note.custom.view.RecyclerView.RecyclerAdapter;
import com.example.jax.Note.custom.view.RecyclerView.ViewHolder;
import com.example.jax.Note.db.DBHelper;
import com.example.jax.Note.model.ItemClickListener;
import com.example.jax.Note.model.NoteInfo;
import com.example.jax.assignment_note.R;

import java.io.Serializable;

public class MainActivity extends Activity implements ItemClickListener {
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private DBHelper dbHelper;
    private ViewHolder viewHolder;
    NoteInfo mGetData = new NoteInfo(), mNoteInfoToInsert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = DBHelper.getInstance(getApplicationContext());

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerAdapter = new RecyclerAdapter(dbHelper.getAllNote(), this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(recyclerAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_new_note:
                createNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createNote() {
        Intent i = new Intent(MainActivity.this, NewNoteActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }



    @Override
    public void onItemClick(int position) {
        dbHelper.deleteNote(position);

        recyclerAdapter = new RecyclerAdapter(dbHelper.getAllNote(),this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void editNote(NoteInfo noteInfo) {
        Intent i = new Intent(MainActivity.this, NewNoteActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Consts.NOTE, noteInfo);
        i.putExtras(bundle);
        startActivity(i);
    }

}
