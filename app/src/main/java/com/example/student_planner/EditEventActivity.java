package com.example.student_planner;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditEventActivity extends AppCompatActivity {

    String id;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);

        id = getIntent().getStringExtra("Id");

        setFields(id);

    }

    @Override
    protected void onStart(){
        super.onStart();
        populateSpinner();
    }


    public void setFields(String id) {
        int found = 0;



        if (id.equals("null"))
        {
        }
        else
            {
                String title;
                String date;
                String description;
                String type;
                String[] projection = {
                        PlannerProvider.PLANNER_TABLE_COL_ID,
                        PlannerProvider.PLANNER_TABLE_COL_TITLE,
                        PlannerProvider.PLANNER_TABLE_COL_TYPE,
                        PlannerProvider.PLANNER_TABLE_COL_DESCRIPTION,
                        PlannerProvider.PLANNER_TABLE_COL_DATE,
                        PlannerProvider.PLANNER_TABLE_COL_TIME,
                        PlannerProvider.PLANNER_TABLE_COL_ADDRESS};

                Cursor myCursor = getContentResolver().query(PlannerProvider.CONTENT_URI, projection, null, null, "_ID DESC");

            int i = 0;
            if (myCursor != null && myCursor.getCount() > 0) {
                String titles[] = new String[myCursor.getCount()];

                while (i < myCursor.getCount() && found == 0) {

                    myCursor.moveToPosition(i);

                    String currentId = myCursor.getString(0);
                    if (currentId.equals(id)) {
                        found = 1;
                        position = i;
                    }

                    i++;

                }
                myCursor.moveToPosition(position);
                title = myCursor.getString(1);
                type = myCursor.getString(2);
                description = myCursor.getString(3);
                date = myCursor.getString(4);

                TextView titleTextView = (TextView) findViewById(R.id.title);
                titleTextView.setText(title);
                TextView descriptionTextView = (TextView) findViewById(R.id.content);
                descriptionTextView.setText(description);
                TextView dateTextView = (TextView)findViewById(R.id.date);
                //Edit dateTextView = (TextView) findViewById(R.id.date);
                dateTextView.setText(date);
               // Spinner typeSpinner = (Spinner)findViewById(R.id.noteTypeSpinner);
                //Set spinner
            }
        }

        initializeComponents();
    }

    void initializeComponents() {
        // findViewById(R.id.btnSave).setOnClickListener(this);
    }


    private void newItem() {

        ContentValues myCV = new ContentValues();
        String title = ((EditText) findViewById(R.id.title)).getText().toString();
        String type = "school";
        String description = ((EditText) findViewById(R.id.content)).getText().toString();
        String date = ((EditText) findViewById(R.id.date)).getText().toString();
        String address = ((EditText)findViewById(R.id.address)).getText().toString();
        String time = ((EditText)findViewById(R.id.time)).getText().toString();

        myCV.put(PlannerProvider.PLANNER_TABLE_COL_TITLE, title);
        myCV.put(PlannerProvider.PLANNER_TABLE_COL_TYPE, type);
        myCV.put(PlannerProvider.PLANNER_TABLE_COL_DESCRIPTION, description);
        myCV.put(PlannerProvider.PLANNER_TABLE_COL_DATE, date);
        myCV.put(PlannerProvider.PLANNER_TABLE_COL_TIME, time);
        myCV.put(PlannerProvider.PLANNER_TABLE_COL_ADDRESS, address);

        getContentResolver().insert(PlannerProvider.CONTENT_URI, myCV);

        String[] projection = {
                PlannerProvider.PLANNER_TABLE_COL_ID,
                PlannerProvider.PLANNER_TABLE_COL_TITLE,
                PlannerProvider.PLANNER_TABLE_COL_TYPE,
                PlannerProvider.PLANNER_TABLE_COL_DESCRIPTION,
                PlannerProvider.PLANNER_TABLE_COL_DATE,
                PlannerProvider.PLANNER_TABLE_COL_TIME,
                PlannerProvider.PLANNER_TABLE_COL_ADDRESS};

        Cursor myCursor = getContentResolver().query(PlannerProvider.CONTENT_URI, projection, null, null, null);
        if (myCursor != null && myCursor.getCount() > 0) {
            //String getTitle = myCursor.getString(1);
           // Toast.makeText(getApplicationContext(), "Created Event: " + getTitle, Toast.LENGTH_LONG).show();

        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void editItem() {
        ContentValues myCV = new ContentValues();

        String title = ((EditText) findViewById(R.id.title)).getText().toString();
        String type = "Example";
        String description = ((EditText) findViewById(R.id.content)).getText().toString();
        String date = ((EditText) findViewById(R.id.date)).getText().toString();
        String time = "8:00";
        String thisId = id;

        myCV.put(PlannerProvider.PLANNER_TABLE_COL_ID, thisId);
        myCV.put(PlannerProvider.PLANNER_TABLE_COL_TITLE, title);
        myCV.put(PlannerProvider.PLANNER_TABLE_COL_TYPE, type);
        myCV.put(PlannerProvider.PLANNER_TABLE_COL_DESCRIPTION, description);
        myCV.put(PlannerProvider.PLANNER_TABLE_COL_DATE, date);
        myCV.put(PlannerProvider.PLANNER_TABLE_COL_TIME, time);

        String[] projection = {
                PlannerProvider.PLANNER_TABLE_COL_ID,
                PlannerProvider.PLANNER_TABLE_COL_TITLE,
                PlannerProvider.PLANNER_TABLE_COL_TYPE,
                PlannerProvider.PLANNER_TABLE_COL_DESCRIPTION,
                PlannerProvider.PLANNER_TABLE_COL_DATE};

        Cursor myCursor = getContentResolver().query(PlannerProvider.CONTENT_URI, projection, null, null, null);

        if (myCursor != null && myCursor.getCount() > 0) {
            getContentResolver().update(Uri.parse(PlannerProvider.CONTENT_URI + "/" + thisId), myCV, null, null);
            myCursor.moveToPosition(position);
            title = myCursor.getString(1);

            Toast.makeText(getApplicationContext(), "Updated Event " + title, Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }



    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.done:

                //If new todo is being created
                if(id.equals("null"))
                {
                    newItem();
                }

                // If a todo is being updated
                else
                {
                    editItem();
                }
                break;


            // Opens up the calendar view for the user.
            case R.id.date:
                calendarPicker();
                break;
            default:
                break;

            case R.id.maps:
                openMap();
                break;
        }
    }

    void openMap()
    {
        Intent intent = new Intent(this, MapsActivity.class);
       // intent.putExtra("Id", id);
        startActivity(intent);
    }

    void calendarPicker()
    {
        final Calendar myCalendar = Calendar.getInstance();
        final EditText editText = (EditText)findViewById(R.id.date);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(editText, myCalendar);
            }
        };


        new DatePickerDialog(EditEventActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    void updateLabel(EditText editText, Calendar myCalendar)
    {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(myCalendar.getTime()));


    }
    /*
    public void onDoneButtonClicked(View v)
    {
        if(id.equals("null"))
        {
            newItem();
        }
        else
        {
            editItem();
        }
    }

*/
    public void populateSpinner(){
        String[] event_type = {"School", "Work", "Social", "Personal"};

        Spinner spinner = findViewById(R.id.noteTypeSpinner);
        //spinner.setOnItemClickListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_item, event_type);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


}


