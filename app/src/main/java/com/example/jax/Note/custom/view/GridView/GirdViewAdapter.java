package com.example.jax.Note.custom.view.GridView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.jax.Note.model.ImageItem;
import com.example.jax.assignment_note.R;

import java.util.ArrayList;

/**
 * Created by Jax on 05-Dec-16.
 */
public class GirdViewAdapter extends ArrayAdapter<Bitmap> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<Bitmap> data = new ArrayList<>();

    public GirdViewAdapter(Context context, int layoutResourceId, ArrayList<Bitmap> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Bitmap item = data.get(position);
        holder.imageView.setImageBitmap(item);
        return row;
    }

    public void addItem(Bitmap image) {
        data.add(image);
    }

    public void removeItem(int position){
        data.remove(position);
    }

    static class ViewHolder {
        ImageView imageView;
    }
}


