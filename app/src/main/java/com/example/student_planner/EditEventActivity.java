package com.example.student_planner;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class EditEventActivity extends AppCompatActivity {

    String id;
    int position = 0;
    EditText chooseTime;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);
        id = getIntent().getStringExtra("Id");
        setFields(id);
        createNotificationChannel();
        //timePicker();

    }

    @Override
    protected void onStart(){
        super.onStart();
        populateSpinner();
    }

    public void timePicker()
    {
        chooseTime = findViewById(R.id.timeSelection);
        chooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(EditEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        chooseTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });
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
                String time;
                String address;

                String[] projection = {
                        PlannerProvider.PLANNER_TABLE_COL_ID,
                        PlannerProvider.PLANNER_TABLE_COL_TITLE,
                        PlannerProvider.PLANNER_TABLE_COL_TYPE,
                        PlannerProvider.PLANNER_TABLE_COL_DESCRIPTION,
                        PlannerProvider.PLANNER_TABLE_COL_DATE,
                        PlannerProvider.PLANNER_TABLE_COL_TIME};

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
                time = myCursor.getString(5);


                TextView titleTextView = (TextView) findViewById(R.id.title);
                titleTextView.setText(title);
                TextView descriptionTextView = (TextView) findViewById(R.id.content);
                descriptionTextView.setText(description);
                TextView dateTextView = (TextView)findViewById(R.id.date);
                dateTextView.setText(date);
                EditText timeSel = (EditText) findViewById(R.id.timeSelection);
                timeSel.setText(time);
                Spinner typeSpinner = (Spinner)findViewById(R.id.noteTypeSpinner);

                int typePos = 0;

                switch (type)
                {
                    case "School":
                        typePos = 1;
                        break;
                    case "Work":
                        typePos = 2;
                        break;
                    case "Social":
                        typePos = 3;
                        break;
                    case "Personal":
                        typePos = 4;
                        break;
                }

                typeSpinner.setSelection(typePos);

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
        String type = ((Spinner) findViewById(R.id.noteTypeSpinner)).getSelectedItem().toString();
        String description = ((EditText) findViewById(R.id.content)).getText().toString();
        String date = ((EditText) findViewById(R.id.date)).getText().toString();
        String timeSel = ((EditText)findViewById(R.id.timeSelection)).getText().toString();



        myCV.put(PlannerProvider.PLANNER_TABLE_COL_TITLE, title);
        myCV.put(PlannerProvider.PLANNER_TABLE_COL_TYPE, type);
        myCV.put(PlannerProvider.PLANNER_TABLE_COL_DESCRIPTION, description);
        myCV.put(PlannerProvider.PLANNER_TABLE_COL_DATE, date);
        myCV.put(PlannerProvider.PLANNER_TABLE_COL_TIME, timeSel);

        getContentResolver().insert(PlannerProvider.CONTENT_URI, myCV);

        String[] projection = {
                PlannerProvider.PLANNER_TABLE_COL_ID,
                PlannerProvider.PLANNER_TABLE_COL_TITLE,
                PlannerProvider.PLANNER_TABLE_COL_TYPE,
                PlannerProvider.PLANNER_TABLE_COL_DESCRIPTION,
                PlannerProvider.PLANNER_TABLE_COL_DATE,
                PlannerProvider.PLANNER_TABLE_COL_TIME};

        String dateTime = date + " " + timeSel;

        Random random = new Random();
        int randomInteger = random.nextInt();

        createNotification(dateTime, title, randomInteger);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    private void editItem() {
        ContentValues myCV = new ContentValues();

        String thisId = id;
        String title = ((EditText) findViewById(R.id.title)).getText().toString();
        String type = ((Spinner)findViewById(R.id.noteTypeSpinner)).getSelectedItem().toString();
        String description = ((EditText) findViewById(R.id.content)).getText().toString();
        String date = ((EditText) findViewById(R.id.date)).getText().toString();
        String timeSel = ((EditText)findViewById(R.id.timeSelection)).getText().toString();

        myCV.put(PlannerProvider.PLANNER_TABLE_COL_ID, thisId);
        myCV.put(PlannerProvider.PLANNER_TABLE_COL_TITLE, title);
        myCV.put(PlannerProvider.PLANNER_TABLE_COL_TYPE, type);
        myCV.put(PlannerProvider.PLANNER_TABLE_COL_DESCRIPTION, description);
        myCV.put(PlannerProvider.PLANNER_TABLE_COL_DATE, date);
        myCV.put(PlannerProvider.PLANNER_TABLE_COL_TIME, timeSel);

        String[] projection = {
                PlannerProvider.PLANNER_TABLE_COL_ID,
                PlannerProvider.PLANNER_TABLE_COL_TITLE,
                PlannerProvider.PLANNER_TABLE_COL_TYPE,
                PlannerProvider.PLANNER_TABLE_COL_DESCRIPTION,
                PlannerProvider.PLANNER_TABLE_COL_DATE,
                PlannerProvider.PLANNER_TABLE_COL_TIME};

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

            case R.id.timeSelection:
                timePicker();
                break;

            default:
                break;


           // case R.id.maps:
             //   openMap();
               // break;
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

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "dateReminder";
            String description = "reminds the user when a due date has come";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("101", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



    void createNotification(String date, String title, int id)
    {
        if(date == null)
        {
            return;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            Date date1 = sdf.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);
            NotificationScheduler.setReminder(this, AlarmReceiver.class, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_WEEK), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), title, id);
        }
        catch(java.text.ParseException e){
            e.printStackTrace();
        }

    }

}


