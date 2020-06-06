package com.example.lifeconnect.ui.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lifeconnect.EspaceDetailsActivity;
import com.example.lifeconnect.JavaClass.EspaceAdapter;
import com.example.lifeconnect.JavaClass.JsonOperation;
import com.example.lifeconnect.MenuActivity;
import com.example.lifeconnect.R;
import com.example.lifeconnect.model.Espace;
import com.example.lifeconnect.model.LocalStructures;
import com.example.lifeconnect.model.Structure;
import com.example.lifeconnect.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarFragment extends Fragment {

    private CalendarViewModel calendarViewModel;

    private static final String FILENAME_STRUCTURE = "StructureDesEspaces.json";

    private LocalStructures localStructures;
    private Structure structure;
    private JsonOperation jsonOperation;
    private View root;

    private RecyclerView recyclerView;
    private ArrayList<Espace> espaces;
    private EspaceAdapter adapter;
    private Espace selectedEspace;

    private int dayOfWeek;
    private Date date;

    private User user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        calendarViewModel =
                ViewModelProviders.of(this).get(CalendarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        this.root = root;

        // Get User settings
        MenuActivity menuActivity = (MenuActivity) getActivity();
        user = menuActivity.getUser();

        FloatingActionButton floatingActionButton = root.findViewById(R.id.floatingButtonCalendar);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedEspace == null) {
                    return;
                }
                Intent intent = new Intent( getActivity(), EspaceDetailsActivity.class);
                intent.putExtra("Structure", structure);
                intent.putExtra("Espace", selectedEspace);
                intent.putExtra("Date", date);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });

        CalendarView calendarView = root.findViewById(R.id.calendarEspace);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                date = new Date(calendar.getTimeInMillis());
                dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; //  1:L, 2:Ma, 3:Me, 4:J, 5:V, 6:S, 0:D
                if (dayOfWeek == 0) dayOfWeek = 7;
                createListData();

            }
        });

        date = new Date(calendarView.getDate());
        dayOfWeek = date.getDay(); //  1:L, 2:Ma, 3:Me, 4:J, 5:V, 6:S, 7:D

        jsonOperation = new JsonOperation();
        localStructures = (LocalStructures) jsonOperation.getObjectFromJson(getContext(), FILENAME_STRUCTURE, 0);
        if (localStructures.getStructures() != null) {
            for (Structure structure_json : localStructures.getStructures()) {
                if (structure_json.getUser() == user.getId()) {
                    structure = structure_json;
                }
            }
        }
        if (structure == null) {
            structure = new Structure(user.getId());
        }

        recyclerView = root.findViewById(R.id.calendarRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        espaces = new ArrayList<>();
        createListData();

        return root;
    }

    private void createListData() {

        espaces = new ArrayList<>();
        for ( int i = 0; i < structure.getEspaces().size(); i++ ) {
            Espace currentEspace = structure.getEspaces().get(i);
            if (currentEspace.getListDayOn().contains(dayOfWeek)) {
                espaces.add(currentEspace);
            }
        }

        adapter = new EspaceAdapter(getContext(), espaces, this);
        recyclerView.setAdapter(adapter);
    }

    public void setSelectedEspace(Espace espace) {
        this.selectedEspace = espace;
    }
}