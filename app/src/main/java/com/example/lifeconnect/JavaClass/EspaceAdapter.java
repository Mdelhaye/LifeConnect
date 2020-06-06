package com.example.lifeconnect.JavaClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lifeconnect.R;
import com.example.lifeconnect.model.Espace;
import com.example.lifeconnect.ui.calendar.CalendarFragment;

import java.util.ArrayList;

public class EspaceAdapter extends RecyclerView.Adapter<EspaceHolder>  {

    private Context context;
    private CalendarFragment calendarFragment;

    private ArrayList<Espace> espaces;
    private ArrayList<FrameLayout> frameLayouts = new ArrayList<>();

    public EspaceAdapter(Context context, ArrayList<Espace> espaces, CalendarFragment calendarFragment) {
        this.context = context;
        this.espaces = espaces;
        this.calendarFragment = calendarFragment;
    }

    @NonNull
    @Override
    public EspaceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_row, parent, false);
        return new EspaceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EspaceHolder holder, int position) {
        Espace currentEspace = espaces.get(position);
        holder.setDetailsForCalendar(frameLayouts, currentEspace, calendarFragment);
    }

    @Override
    public int getItemCount() {
        return espaces.size();
    }
}
