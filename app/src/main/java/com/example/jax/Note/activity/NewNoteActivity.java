package com.example.jax.Note.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jax.Note.Consts;
import com.example.jax.Note.custom.view.GridView.GirdViewAdapter;
import com.example.jax.Note.db.DBHelper;
import com.example.jax.Note.model.ColorNote;
import com.example.jax.Note.model.ImageItem;
import com.example.jax.Note.model.NoteInfo;
import com.example.jax.Note.utils.DbBitmapUtility;
import com.example.jax.assignment_note.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class NewNoteActivity extends Activity {

    //photo
    private GridView gridView;
    private GirdViewAdapter grGirdViewAdapter;
    private ArrayList<Bitmap> dataImage = new ArrayList<>();
    private DbBitmapUtility dbBitmapUtility = new DbBitmapUtility();

    //add Item
    private EditText editTitle, editNote;
    private TextView tvTime;
    DBHelper dbHelper;
    NoteInfo mNoteInfoToUpdate ;
    RelativeLayout rl;
    ColorNote color = new ColorNote();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        dbHelper = DBHelper.getInstance(getApplicationContext());

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        editTitle = (EditText)findViewById(R.id.title_textview);
        editNote = (EditText)findViewById(R.id.note_textview);
        tvTime =(TextView)findViewById(R.id.time_textview);
        rl =(RelativeLayout)findViewById(R.id.rl);

        String currentDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
        tvTime.setText(currentDateTime);

        gridView = (GridView)findViewById(R.id.grid_image);
        grGirdViewAdapter = new GirdViewAdapter(this,R.layout.grid_item_layout, dataImage);
        gridView.setAdapter(grGirdViewAdapter);


        Bundle data = getIntent().getExtras();
        if (data != null && data.containsKey(Consts.NOTE)) {
            mNoteInfoToUpdate = (NoteInfo) data.getSerializable(Consts.NOTE);
        }
        try {
            initValuesForUi();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initValuesForUi() throws IOException, ClassNotFoundException {
        //text
        if (mNoteInfoToUpdate == null) return;
        editTitle.setText(mNoteInfoToUpdate.title);
        editNote.setText(mNoteInfoToUpdate.note);
        //color
        if(mNoteInfoToUpdate.color != null) {
            rl.setBackgroundColor(Color.parseColor(mNoteInfoToUpdate.color));
        }
        //photo
        ArrayList<Bitmap> showImage = new ArrayList<>();
            showImage.add(dbBitmapUtility.getImage(dbHelper.getSingleNote(mNoteInfoToUpdate.id).image));
            grGirdViewAdapter = new GirdViewAdapter(this, R.layout.grid_item_layout, showImage);
            gridView.setAdapter(grGirdViewAdapter);

    }


    @Override
    protected void onResume() {
        super.onResume();
        deleteImage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_insert_picture:
                AlertDialog.Builder builder = new AlertDialog.Builder(NewNoteActivity.this);
                builder.setTitle("Insert Picture");
                final CharSequence[] items ={"Take Photo","Choose Photo"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                cameraIntent();
                                break;
                            case 1:
                                galleryIntent();
                                break;
                        }
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
        if(!editTitle.getText().toString().isEmpty()){
            noteInfo.title = editTitle.getText().toString();
        }
        if(!editNote.getText().toString().isEmpty()){
            noteInfo.note = editNote.getText().toString();
        }
        //photo
        for(Bitmap image : dataImage){
            noteInfo.image = dbBitmapUtility.getBytes(image);
        }
        //color
        noteInfo.color = color.getColor();
        //insert,update
        Bundle data = getIntent().getExtras();
        if (data != null) {
            dbHelper.updateNote(noteInfo, mNoteInfoToUpdate.id);
        }
        if(data == null ){
            dbHelper.insertNote(noteInfo);
        }

        Intent i = new Intent(NewNoteActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);



    }

    private void chooseColor(){

        final Dialog dialog = new Dialog(NewNoteActivity.this);
        dialog.setTitle("Choose Color");
        dialog.setContentView(R.layout.dialog_color_item);

        Button btnWhite = (Button)dialog.findViewById(R.id.btn_white);
        btnWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl.setBackgroundColor(Color.parseColor("#FFFFFF"));
                color.setColor("#FFFFFF");
                dialog.dismiss();
            }
        });

        Button btnYellow = (Button)dialog.findViewById(R.id.btn_yellow);
        btnYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    rl.setBackgroundColor(Color.parseColor("#EEFF41"));
                    color.setColor("#EEFF41");
                    dialog.dismiss();
            }
        });
        Button btnBlue = (Button)dialog.findViewById(R.id.btn_blue);
        btnBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl.setBackgroundColor(Color.parseColor("#18FFFF"));
                color.setColor("#18FFFF");
                dialog.dismiss();
            }
        });

        Button btnBlue1 = (Button)dialog.findViewById(R.id.btn_blue1);
        btnBlue1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl.setBackgroundColor(Color.parseColor("#81D4FA"));
                color.setColor("#81D4FA");
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void cameraIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,Consts.REQUEST_CAMERA);

    }

    private void galleryIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select File"), Consts.SELECT_FILE);
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
            Bundle data1 = getIntent().getExtras();
            if(data1!= null) {
                Bitmap imageUpdate = dbBitmapUtility.getImage(dbHelper.getSingleNote(mNoteInfoToUpdate.id).image);
                Bitmap imageUpdate1 = Bitmap.createScaledBitmap(imageUpdate, 150, 150, true);
                grGirdViewAdapter.addItem(imageUpdate1);
            }
        }
    }

    private void onCaptureResult(Intent data){
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);

        grGirdViewAdapter.addItem(resizeBitmap);
        grGirdViewAdapter.notifyDataSetChanged();
    }

    private void onSelectFileResult(Intent data){
        Bitmap bitmap = null;
        if (data != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(
                        getApplicationContext().getContentResolver(), data.getData()
                );
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
        grGirdViewAdapter.addItem(resizeBitmap);
        grGirdViewAdapter.notifyDataSetChanged();

    }

    private void deleteImage(){
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewNoteActivity.this);
                builder.setTitle("Confirm Delete");
                builder.setMessage("Are you sure want to delete this ?");
                builder.setPositiveButton("Yes",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        grGirdViewAdapter.removeItem(pos);
                        grGirdViewAdapter.notifyDataSetChanged();

                    }
                });

                builder.setNegativeButton("No",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();
                return true;
            }
        });

    }

}
