package com.example.student_planner;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static java.util.Calendar.DAY_OF_MONTH;

public class MainActivity extends AppCompatActivity {

    String id = "null";

    //override methods//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);
        CalendarView calendar = findViewById(R.id.calendar);
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        c.setTimeInMillis(calendar.getDate());
        int day = c.get(DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);
        populateListDailyEvents(year, month, day);
        setCalListener();

    }

    //our methods//

    void setCalListener()
    {
        final CalendarView calendar = findViewById(R.id.calendar);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                month = month + 1;
                populateListDailyEvents(year, month, dayOfMonth);
            }
        });
    }

    //this method populates the listview with what is in the PlannerProvider
    void populateListDailyEvents(int year, int month, int dayOfMonth)
    {
        String selectedDate;

        //make the day of the month a double digit if necessary
        if(dayOfMonth < 10) {
            selectedDate = month + "/0" + dayOfMonth + "/" + year;

        }
        else
        {
            selectedDate = month + "/" + dayOfMonth + "/" + year;
        }

        // Find ListView to populate
        final ListView events = findViewById(R.id.listOfEvents);

        //all columns
        String[] projection = {
                PlannerProvider.PLANNER_TABLE_COL_ID,
                PlannerProvider.PLANNER_TABLE_COL_TITLE,
                PlannerProvider.PLANNER_TABLE_COL_TYPE,
                PlannerProvider.PLANNER_TABLE_COL_DESCRIPTION,
                PlannerProvider.PLANNER_TABLE_COL_DATE,
                PlannerProvider.PLANNER_TABLE_COL_TIME,
        };

        String sel = PlannerProvider.PLANNER_TABLE_COL_DATE + " = ?";
        String[] selectionArgs = new String[1];
        selectionArgs[0] = selectedDate;

        //Perform a query to get the rows that correspond that the date that is clicked on the calendar
        final Cursor eventCursor = getContentResolver().query(PlannerProvider.CONTENT_URI, projection,
                sel, selectionArgs, null);

        // Setup cursor adapter using cursor from last step
        final EventListCursorAdapter eventAdapter = new EventListCursorAdapter(this, eventCursor);

        //Attach cursor adapter to the ListView
        events.setAdapter(eventAdapter);
        eventAdapter.changeCursor(eventCursor);

        //on click listener so that the user can click on the event in the list
        events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //moves the cursor to event that was clicked
                //eventCursor.moveToPosition(position);
                String value = String.valueOf(eventAdapter.getItemId(position));

                //starts the edit activity
                updateItem(value);
            }
        });
    }

    //should start a new activity to edit the note on the note screen
    public void onEditButtonClicked(View v)
    {
        Intent intent = new Intent(this, EditEventActivity.class);
        intent.putExtra("Id", id);
        startActivity(intent);
    }

    public void updateItem(String id)
    {
        Intent intent = new Intent(this, EditEventActivity.class);
        intent.putExtra("Id", id);
        startActivity(intent);
    }
}
