package com.example.lifeconnect.JavaClass;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lifeconnect.R;
import com.example.lifeconnect.model.Espace;
import com.example.lifeconnect.ui.calendar.CalendarFragment;

import java.util.ArrayList;

public class EspaceHolder extends RecyclerView.ViewHolder {

    private CardView cardView;
    private FrameLayout frameLayoutBack;
    private TextView textView;

    public EspaceHolder(View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.cardViewCalendarRecyclerView);
        frameLayoutBack = itemView.findViewById(R.id.frameLayoutCardViewCalenderBackward);
        textView = itemView.findViewById(R.id.titleCardViewCalendarRecyclerView);
    }

    public void setDetailsForCalendar(final ArrayList<FrameLayout> frameLayouts, final Espace currentEspace, final CalendarFragment calendarFragment) {
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayouts.add(frameLayoutBack);
                for (int i = 0; i < frameLayouts.size(); i++) {
                    frameLayouts.get(i).setBackgroundColor(Color.TRANSPARENT);
                }

                frameLayoutBack.setBackgroundColor(Color.rgb(0, 133, 119));
                calendarFragment.setSelectedEspace(currentEspace);

            }
        });

        textView.setText(currentEspace.getName());
    }

}
