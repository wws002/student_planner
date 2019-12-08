package com.example.student_planner;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //current date when app is launched
    //long date = getDate();
    long date = 0;
    //id
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
        // populateListDailyEvents(date);
        setContentView(R.layout.activity_main);
        //date = getDa;
        setCalListener();
        //populateListDailyEvents(c);

    }

    //our methods//

 /*   long getDate()
    {
        CalendarView calendar = findViewById(R.id.calendar);
        return calendar.getDate();
    }
*/
    void setCalListener()
    {

        final CalendarView calendar = findViewById(R.id.calendar);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                month = month + 1;
                populateListDailyEvents(year, month, dayOfMonth);

                //Toast.makeText(getApplicationContext(), toString()calendar.getDate(), 0).show();// TODO Auto-generated method stub
            }
        });


    }

    //this method populates the listview with what is in the PlannerProvider
    void populateListDailyEvents(int year, int month, int dayOfMonth)
    {
        //get date
        //SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        String selectedDate = month + "/" + dayOfMonth + "/" + year; //dateFormat.format(new Date(date));

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
                PlannerProvider.PLANNER_TABLE_COL_ADDRESS
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
