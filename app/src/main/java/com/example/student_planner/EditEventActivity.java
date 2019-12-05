package com.example.student_planner;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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


    public void setFields(String id) {
        int found = 0;
        String title;
        String date;
        String description;
        String type;

        String[] projection = {
                PlannerProvider.PLANNER_TABLE_COL_ID,
                PlannerProvider.PLANNER_TABLE_COL_TITLE,
                PlannerProvider.PLANNER_TABLE_COL_TYPE,
                PlannerProvider.PLANNER_TABLE_COL_DESCRIPTION,
                PlannerProvider.PLANNER_TABLE_COL_DATE};

        Cursor myCursor = getContentResolver().query(PlannerProvider.CONTENT_URI, projection, null, null, "_ID DESC");
        if (id.equals("null"))
        {

        }
        else
            {
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
                description = myCursor.getString(4);
                date = myCursor.getString(3);

                TextView titleTextView = (TextView) findViewById(R.id.title);
                titleTextView.setText(title);
                TextView descriptionTextView = (TextView) findViewById(R.id.content);
                descriptionTextView.setText(description);
                TextView dateTextView = (TextView)findViewById(R.id.date);
                //Edit dateTextView = (TextView) findViewById(R.id.date);
                //dateTextView.setText(date);
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


        myCV.put(PlannerProvider.PLANNER_TABLE_COL_TITLE, title);
        myCV.put(PlannerProvider.PLANNER_TABLE_COL_TYPE, type);
        myCV.put(PlannerProvider.PLANNER_TABLE_COL_DESCRIPTION, description);
        myCV.put(PlannerProvider.PLANNER_TABLE_COL_DATE, date);

        getContentResolver().insert(PlannerProvider.CONTENT_URI, myCV);

        String[] projection = {
                PlannerProvider.PLANNER_TABLE_COL_ID,
                PlannerProvider.PLANNER_TABLE_COL_TITLE,
                PlannerProvider.PLANNER_TABLE_COL_TYPE,
                PlannerProvider.PLANNER_TABLE_COL_DESCRIPTION,
                PlannerProvider.PLANNER_TABLE_COL_DATE,};

        Cursor myCursor = getContentResolver().query(PlannerProvider.CONTENT_URI, projection, null, null, null);
        if (myCursor != null && myCursor.getCount() > 0) {
            String getTitle = myCursor.getString(1);
            Toast.makeText(getApplicationContext(), "Created Event: " + getTitle, Toast.LENGTH_LONG).show();

        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void editItem() {
        ContentValues myCV = new ContentValues();

        String title = ((EditText) findViewById(R.id.title)).getText().toString();
        String type = "Example";
        String description = ((EditText) findViewById(R.id.content)).getText().toString();
        String date = "01/01/2020";
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
}


