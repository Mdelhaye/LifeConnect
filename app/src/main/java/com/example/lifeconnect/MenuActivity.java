package com.example.lifeconnect;

import android.os.Bundle;

import com.example.lifeconnect.JavaClass.JsonOperation;
import com.example.lifeconnect.model.User;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.lifeconnect.model.Users;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.TextView;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private TextView textView_top, textView_bottom;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_list_espaces, R.id.nav_calendar, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_settings, R.id.nav_upload)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Get header views
        View headerView = navigationView.getHeaderView(0);
        textView_top = headerView.findViewById(R.id.nav_header_textview_top);
        textView_bottom = headerView.findViewById(R.id.nav_header_textview_bottom);

        updateUserInformationOnHeaderView();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



    private void updateUserInformationOnHeaderView() {
        // Get User information
        user = (User) getIntent().getSerializableExtra("User");

        JsonOperation jsonOperation = new JsonOperation();
        Users users = (Users) jsonOperation.getObjectFromJson(getApplicationContext(), "Users.json", 2);
        for (User currentUser : users.getUsers()) {
            if (user.getFirstName().equals(currentUser.getFirstName())) {
                if (user.getLastName().equals(currentUser.getLastName())) {
                    user = currentUser;
                }
            }
        }

        // Set information to display
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(user.getFirstName() == null   ? "*Prénom*"    : user.getFirstName());
        arrayList.add(user.getLastName() == null    ? "*Nom*"       : user.getLastName());
        arrayList.add(user.getGender() == 0         ? "*Sexe*"      : user.getGender() == 1 ? "Homme" : user.getGender() == 2 ? "Femme" : "");
        arrayList.add(user.getAge() == 0            ? "*Age*"       : String.valueOf(user.getAge()));

        // Set the views
        textView_top.setText(getResources().getString(R.string.menu_activity_textview_top, arrayList.get(0), arrayList.get(1)));
        textView_bottom.setText(getResources().getString(R.string.menu_activity_textview_bottom, arrayList.get(2), arrayList.get(3)));
    }

    public User getUser() {
        return user;
    }
}
