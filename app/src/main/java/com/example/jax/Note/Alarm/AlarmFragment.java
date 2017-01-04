package com.example.jax.Note.Alarm;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jax.assignment_note.R;

import java.text.DateFormat;
import java.util.Calendar;

public class AlarmFragment extends Fragment {
    Spinner spDate, spTime;
    ImageButton btRemove;
    RelativeLayout rl;
    TextView tvAlarm;
    String textDate[] = {"Today","Tomorrow","Other"};
    String textTime[] ={"06:30","12:00","Other"};
    ArrayAdapter adapterDate;
    ArrayAdapter<String> adapterTime;
    DateFormat dateFormat = DateFormat.getDateTimeInstance();
    Calendar date = Calendar.getInstance();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_alarm,container,false);

        tvAlarm =(TextView)view.findViewById(R.id.textAlarm);
        rl = (RelativeLayout)view.findViewById(R.id.rl);
        spDate = (Spinner)view.findViewById(R.id.date_spinner);
        spTime = (Spinner)view.findViewById(R.id.time_spinner);
        btRemove = (ImageButton)view.findViewById(R.id.remove);

        adapterDate = new ArrayAdapter<>(this.getActivity(),android.R.layout.simple_spinner_item,textDate);
        adapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterTime = new ArrayAdapter<>(this.getActivity(),android.R.layout.simple_spinner_item,textTime);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDate.setAdapter(adapterDate);
        spTime.setAdapter(adapterTime);

        rl.setOnClickListener(v -> {
            spDate.setVisibility(View.VISIBLE);
            spTime.setVisibility(View.VISIBLE);
            tvAlarm.setVisibility(View.INVISIBLE);
            btRemove.setVisibility(View.VISIBLE);
            spDate.setOnItemSelectedListener(new EventDate());
            spTime.setOnItemSelectedListener(new EventTime());
        });

        btRemove.setOnClickListener(v -> {
            tvAlarm.setVisibility(View.VISIBLE);
            spDate.setVisibility(View.INVISIBLE);
            spTime.setVisibility(View.INVISIBLE);
            btRemove.setVisibility(View.INVISIBLE);
        });
        return view;
    }
    private class EventDate implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 0:
                    Toast.makeText(parent.getContext(),"Today",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(parent.getContext(),"Tomorrow",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    updateDate();
                    break;
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private void updateDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date.set(Calendar.YEAR,year);
                date.set(Calendar.MONTH,month);
                date.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                //    alarm.setAlarmDate(date);

            }
        }, date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private class EventTime implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            switch (position){
                case 0:
                    Toast.makeText(parent.getContext(),"6:30",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(parent.getContext(),"12:00",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    updateTime();
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private void updateTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                date.set(Calendar.HOUR_OF_DAY,hourOfDay);
                date.set(Calendar.MINUTE, minute);
            }
        }, date.get(Calendar.HOUR_OF_DAY),date.get(Calendar.MINUTE),true);
        timePickerDialog.show();

    }
}
