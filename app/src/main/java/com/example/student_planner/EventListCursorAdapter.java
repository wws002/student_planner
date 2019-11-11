package com.example.student_planner;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;

public class EventListCursorAdapter  extends CursorAdapter {
    public EventListCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listview_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        ImageView circle = view.findViewById(R.id.circle);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        // Extract properties from cursor
        String title = cursor.getString(cursor.getColumnIndexOrThrow(PlannerProvider.Planner_TABLE_COL_TITLE));
        String content = cursor.getString(cursor.getColumnIndexOrThrow(PlannerProvider.Planner_TABLE_COL_TYPE));
        // Populate fields with extracted properties
        circle.setColorFilter(getContext().getResources().getColor(R.color.blue));
        tvTitle.setText(title);
    }
}
