package com.example.jax.Note.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.jax.Note.model.ImageItem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Jax on 18-Dec-16.
 */

public class DbBitmapUtility {

    //convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,0,bos);
        return bos.toByteArray();
    }

    //convert from byte array to bitmap
    public static Bitmap getImage(byte[] image){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        return BitmapFactory.decodeByteArray(image,0,image.length);
    }
}
