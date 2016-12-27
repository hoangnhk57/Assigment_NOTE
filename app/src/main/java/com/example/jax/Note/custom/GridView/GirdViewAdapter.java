package com.example.jax.Note.custom.GridView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.jax.assignment_note.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jax on 05-Dec-16.
 */
public class GirdViewAdapter extends ArrayAdapter<String> {

    private Context context;
    private int layoutResourceId;
    private List<String> data = new ArrayList<>();

    public GirdViewAdapter(Context context, int layoutResourceId, List<String> data) {
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
        String item = data.get(position);
        Bitmap bitmap = BitmapFactory.decodeFile(item);
        if(bitmap!=null) {
            Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);

            holder.imageView.setImageBitmap(resizeBitmap);
        }
        return row;
    }

    public void addItem(String imagePath) {
        data.add(imagePath);
    }

    public boolean removeItem(int position){
        data.remove(position);
        return false;
    }

    static class ViewHolder {
        ImageView imageView;
    }
}


