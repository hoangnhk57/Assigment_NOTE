package com.example.jax.Note.model;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.jax.Note.activity.MainActivity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jax on 30-Nov-16.
 */
public class NoteInfo implements Serializable {

    public int id;
    public String  title,note,color,currentDateTime, path;
}
