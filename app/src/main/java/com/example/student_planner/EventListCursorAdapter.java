package com.example.student_planner;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        String title = cursor.getString(cursor.getColumnIndexOrThrow(PlannerProvider.PLANNER_TABLE_COL_TITLE));
        String type = cursor.getString(cursor.getColumnIndexOrThrow(PlannerProvider.PLANNER_TABLE_COL_TYPE));

        // Populate fields with extracted properties
        tvTitle.setText(title);

        //set circle to appropriate color
        switch (type)
        {
            case "school" :
                circle.setColorFilter(Color.parseColor("red"), PorterDuff.Mode.SRC_ATOP);
                break;
            case "work" :
                circle.setColorFilter(Color.parseColor("blue"), PorterDuff.Mode.SRC_ATOP);
                break;
            case "personal":
                circle.setColorFilter(Color.parseColor("green"), PorterDuff.Mode.SRC_ATOP);
                break;
            case "social":
                circle.setColorFilter(Color.parseColor("yellow"), PorterDuff.Mode.SRC_ATOP);
                break;
            default :
                circle.setColorFilter(Color.parseColor("black"), PorterDuff.Mode.SRC_ATOP);
        }
    }
}
