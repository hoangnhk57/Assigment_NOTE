package com.example.jax.Note.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jax.Note.config.Consts;
import com.example.jax.Note.custom.GridView.GirdViewAdapter;
import com.example.jax.Note.db.DBHelper;
import com.example.jax.Note.model.ColorNote;
import com.example.jax.Note.model.ImagePath;
import com.example.jax.Note.model.NoteInfo;
import com.example.jax.Note.utils.PathImageUtility;
import com.example.jax.assignment_note.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;


import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NewNoteActivity extends Activity {

    //photo
    private GridView gridView;
    private GirdViewAdapter grGirdViewAdapter;
    private List<String> dataPath = new ArrayList<>();

    //add Item
    private EditText editTitle, editNote;
    private TextView tvTime;
    DBHelper dbHelper;
    NoteInfo mNoteInfoToUpdate;
    RelativeLayout rl;
    ColorNote colorNote = new ColorNote();
    ImagePath imagePath = new ImagePath();
    BottomNavigationViewEx bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        dbHelper = DBHelper.getInstance(getApplicationContext());

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        editTitle = (EditText) findViewById(R.id.title_textview);
        editNote = (EditText) findViewById(R.id.note_textview);
        tvTime = (TextView) findViewById(R.id.time_textview);
        rl = (RelativeLayout) findViewById(R.id.rl);

        String currentDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
        tvTime.setText(currentDateTime);

        gridView = (GridView) findViewById(R.id.grid_image);
        grGirdViewAdapter = new GirdViewAdapter(this, R.layout.grid_item_layout, dataPath);
        gridView.setAdapter(grGirdViewAdapter);

        bottomNavigation = (BottomNavigationViewEx) findViewById(R.id.navigation_bottom);
        bottomNavigation.enableShiftingMode(false);
        bottomNavigation.setTextVisibility(false);
        bottomNavigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelect);

        Bundle data = getIntent().getExtras();
        if (data != null && data.containsKey(Consts.NOTE)) {
            mNoteInfoToUpdate = (NoteInfo) data.getSerializable(Consts.NOTE);
        }
        if (data == null) {
            bottomNavigation.setVisibility(View.GONE);
        }
        initValuesForUi();

    }

    private boolean onNavigationItemSelect(MenuItem item) {
        switch (item.getItemId()) {
             case R.id.action_previous:
                break;
             case R.id.action_share:
                 String title = mNoteInfoToUpdate.title;
                 String note = mNoteInfoToUpdate.note;
                 Intent intentShare = new Intent("android.intent.action.SEND");
                 intentShare.setType("text/plain");
                 intentShare.putExtra(Intent.EXTRA_TEXT,title +"\n"+ note);
                 startActivity(Intent.createChooser(intentShare,"Share with"));
                break;
             case R.id.action_delete:
                 AlertDialog.Builder builder = new AlertDialog.Builder(NewNoteActivity.this);
                 builder.setTitle("Confirm Delete");
                 builder.setMessage("Are you sure want to delete this ?");
                 builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                     dbHelper.deleteNote(mNoteInfoToUpdate.id);
                     Intent end = new Intent(NewNoteActivity.this, MainActivity.class);
                     startActivity(end);
                 });
                 builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                 builder.create().show();
                break;
             case R.id.action_next:
                break;
        }
        return true;
    }

    private void initValuesForUi() {
        //text
        if (mNoteInfoToUpdate == null) return;
        editTitle.setText(mNoteInfoToUpdate.title);
        editNote.setText(mNoteInfoToUpdate.note);
        setTitle(mNoteInfoToUpdate.title);

        //color
        if (mNoteInfoToUpdate.color != null) {
            rl.setBackgroundColor(Color.parseColor(mNoteInfoToUpdate.color));
            colorNote.setColor(mNoteInfoToUpdate.color);
        }

        //photo
        if (mNoteInfoToUpdate.path != null) {
            List<String> arrayPath = PathImageUtility.convertStringToList(mNoteInfoToUpdate.path);
            grGirdViewAdapter = new GirdViewAdapter(NewNoteActivity.this, R.layout.grid_item_layout, arrayPath);
            gridView.setAdapter(grGirdViewAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        imagePath.setImagePath(dataPath);
        deleteImage();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_picture:
                AlertDialog.Builder builder = new AlertDialog.Builder(NewNoteActivity.this);
                builder.setTitle("Insert Picture");
                final CharSequence[] items = {"Take Photo", "Choose Photo"};
                builder.setItems(items, (dialogInterface, i) -> {
                    switch (i) {
                        case 0:
                            cameraIntent();
                            break;
                        case 1:
                            galleryIntent();
                            break;
                    }
                });
                builder.create().show();
                return true;
            case R.id.action_choose_color:
                chooseColor();
                return true;
            case R.id.action_accept:
                onNoteResult();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onNoteResult() {
        //text
        NoteInfo noteInfo = new NoteInfo();
        if (!editTitle.getText().toString().isEmpty()) {
            noteInfo.title = editTitle.getText().toString();
            setTitle(editTitle.getText().toString());
        }
        if (!editNote.getText().toString().isEmpty()) {
            noteInfo.note = editNote.getText().toString();
        }
        //photo
        noteInfo.path = PathImageUtility.convertListToString(imagePath.getImagePath());
        //color
        noteInfo.color = colorNote.getColor();
        //insert,update
        Bundle data = getIntent().getExtras();
        if (data != null) {
            dbHelper.updateNote(noteInfo, mNoteInfoToUpdate.id);
        }
        if (data == null) {
            dbHelper.insertNote(noteInfo);
        }
        Intent i = new Intent(NewNoteActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    private void chooseColor() {

        final Dialog dialog = new Dialog(NewNoteActivity.this);
        dialog.setTitle("Choose Color");
        dialog.setContentView(R.layout.dialog_color_item);

        Button btnWhite = (Button) dialog.findViewById(R.id.btn_white);
        btnWhite.setOnClickListener(view -> {
            rl.setBackgroundColor(Color.parseColor("#FFFFFF"));
            colorNote.setColor("#FFFFFF");
            dialog.dismiss();
        });

        Button btnYellow = (Button) dialog.findViewById(R.id.btn_yellow);
        btnYellow.setOnClickListener(view -> {
            rl.setBackgroundColor(Color.parseColor("#EEFF41"));
            colorNote.setColor("#EEFF41");
            dialog.dismiss();
        });
        Button btnBlue = (Button) dialog.findViewById(R.id.btn_blue);
        btnBlue.setOnClickListener(view -> {
            rl.setBackgroundColor(Color.parseColor("#18FFFF"));
            colorNote.setColor("#18FFFF");
            dialog.dismiss();
        });

        Button btnBlue1 = (Button) dialog.findViewById(R.id.btn_blue1);
        btnBlue1.setOnClickListener(view -> {
            rl.setBackgroundColor(Color.parseColor("#81D4FA"));
            colorNote.setColor("#81D4FA");
            dialog.dismiss();
        });
        dialog.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Consts.REQUEST_CAMERA);

    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Consts.SELECT_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Consts.REQUEST_CAMERA) {
                onCaptureResult(data);

            } else if (requestCode == Consts.SELECT_FILE) {
                onSelectFileResult(data);
            }
        }
    }

    private void onCaptureResult(Intent data) {
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        //get pathImage from captureCamera
        Uri tempUri = getImageUri(getApplicationContext(), bitmap);
        String imagePath = getPath(tempUri);
        Log.d(">>>>>>>>>>>>>>>>>>>>>>>", imagePath);

        dataPath.add(imagePath);
        gridView = (GridView) findViewById(R.id.grid_image);
        grGirdViewAdapter = new GirdViewAdapter(this, R.layout.grid_item_layout, dataPath);
        gridView.setAdapter(grGirdViewAdapter);
    }

    private void onSelectFileResult(Intent data) {
        String imagePath = null;
        if (data != null) {
            //get pathImage from gallery
            imagePath = getPath(data.getData());
            Log.d(">>>>>>>>>>>>>>>>>>>>>>>", imagePath);
        }

        dataPath.add(imagePath);
        gridView = (GridView) findViewById(R.id.grid_image);
        grGirdViewAdapter = new GirdViewAdapter(this, R.layout.grid_item_layout, dataPath);
        gridView.setAdapter(grGirdViewAdapter);
    }

    private Uri getImageUri(Context context, Bitmap image) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), image, "Title", null);
        return Uri.parse(path);
    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private boolean deleteImage() {
        gridView.setOnItemLongClickListener((adapterView, view, pos, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(NewNoteActivity.this);
            builder.setTitle("Confirm Delete");
            builder.setMessage("Are you sure want to delete this ?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                for (int k = 0; k < dataPath.size(); k++) {
                     if (k == pos) {
                        dataPath.remove(k);
                     }
                }
                gridView = (GridView) findViewById(R.id.grid_image);
                grGirdViewAdapter = new GirdViewAdapter(this, R.layout.grid_item_layout, dataPath);
                gridView.setAdapter(grGirdViewAdapter);
             });

            builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
            builder.create().show();
            return true;
        });

        return false;
    }

}
