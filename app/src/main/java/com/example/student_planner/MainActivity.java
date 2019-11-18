package com.example.student_planner;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //current date when app is launched
    long date = 0;
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
        setContentView(R.layout.activity_main);
        date = getDate();
        setCalListener();
        populateListDailyEvents(date);
    }

    //our methods//

    long getDate()
    {
        CalendarView calendar = findViewById(R.id.calendar);
        return calendar.getDate();
    }

    void setCalListener()
    {
        final CalendarView calendar = findViewById(R.id.calendar);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth)
            {
                if(calendar.getDate() != date)
                {
                    populateListDailyEvents(calendar.getDate());
                    date = calendar.getDate();
                }
            }
        });
    }

    //this method populates the listview with what is in the PlannerProvider
    void populateListDailyEvents(Long date)
    {
        //get date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        String selectedDate = dateFormat.format(new Date(date));

        // Find ListView to populate
        final ListView events = findViewById(R.id.listOfEvents);

        //all columns
        String[] projection = {
                "_ID AS " + PlannerProvider.PLANNER_TABLE_COL_ID,
                PlannerProvider.PLANNER_TABLE_COL_TITLE,
                PlannerProvider.PLANNER_TABLE_COL_TYPE,
                PlannerProvider.PLANNER_TABLE_COL_DESCRIPTION,
                PlannerProvider.PLANNER_TABLE_COL_DATE
        };

        //Perform a query to get the rows that correspond that the date that is clicked on the calendar
        final Cursor eventCursor = getContentResolver().query(PlannerProvider.CONTENT_URI, projection,
                "DATE = " + selectedDate , null, null);

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
                eventCursor.moveToPosition(position);
                //starts the edit activity
                id = eventCursor.getInt(0);

                //editEvent();
            }
        });
    }

    //should start a new activity to edit the note on the note screen
    public void editEvent(View v)
    {
        Intent intent = new Intent(this, EditEventActivity.class);
        intent.putExtra("Id", id);
        startActivity(intent);

        startActivity(new Intent(MainActivity.this, EditEventActivity.class));
    }

  /*/  public void onEditButtonClicked(View v){
       // editEvent(id);
    }

   // public void onDoneButtonClicked(View v){
       // setContentView(R.layout.activity_main);
   // }

   */
}
