package com.example.jax.Note.model;

/**
 * Created by Jax on 12-Dec-16.
 */

public interface ItemClickListener {
    //ham de xoa note
    void onItemClick(int position);
    //ham de eidt note
    void editNote(NoteInfo noteInfo);
}
