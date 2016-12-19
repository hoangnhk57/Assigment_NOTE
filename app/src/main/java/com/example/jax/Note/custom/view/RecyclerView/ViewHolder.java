package com.example.jax.Note.custom.view.RecyclerView;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jax.Note.db.DBHelper;
import com.example.jax.Note.model.NoteInfo;
import com.example.jax.assignment_note.R;

import java.util.ArrayList;

/**
 * Created by Jax on 30-Nov-16.
 */
public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView mTitle, mNote,mTime;
    public CardView cardView;
    public ViewHolder(final View itemView) {

        super(itemView);
        this.mTitle =(TextView)itemView.findViewById(R.id.title_textview1);
        this.mNote =(TextView)itemView.findViewById(R.id.note_textview1);
        this.mTime = (TextView)itemView.findViewById(R.id.time_textview1);
        this.cardView = (CardView)itemView.findViewById(R.id.cardView);

    }
}
