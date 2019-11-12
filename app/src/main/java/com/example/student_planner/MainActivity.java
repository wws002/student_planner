package com.example.student_planner;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    //override methods//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //our methods//

    //this method populates the listview with what is in the PlannerProvider
    void populateListDailyEvents(String date)
    {
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
                null, null, null);

        // Setup cursor adapter using cursor from last step
        final EventListCursorAdapter eventAdapter = new EventListCursorAdapter(this, eventCursor);

        // Attach cursor adapter to the ListView
        events.setAdapter(eventAdapter);
        eventAdapter.changeCursor(eventCursor);

        //on click listener so that the user can click on the event in the list
        events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //moves the cursor to event that was clicked
                eventCursor.moveToPosition(position);
                //starts the edit activity
                editEvent();
            }
        });
    }

    //should start a new activity to edit the note on the note screen
    void editEvent()
    {

    }

    public void onEditButtonClicked(View v){

    }
}
