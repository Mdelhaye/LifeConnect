package com.example.lifeconnect;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.lifeconnect.JavaClass.JsonOperation;
import com.example.lifeconnect.model.User;
import com.example.lifeconnect.model.Users;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String FILENAME = "Users.json";
    private JsonOperation jsonOperation;

    private LinearLayout linearLayout_userList;
    private Button toggle_sign_in_up;

    private Users user_list;
    private User user_selected;

    @Override
    protected void onCreate(Bundle state){
        super.onCreate(state);
        setContentView(R.layout.activity_main);

        final Users users = new Users(new ArrayList<User>());

        // Get Views from layout
        linearLayout_userList = findViewById(R.id.activity_main_layout_user_list);
        toggle_sign_in_up = findViewById(R.id.activity_main_toggle_sign_in_up);

        // Get Users from local data
        jsonOperation = new JsonOperation();
        Object returnData = jsonOperation.getObjectFromJson(getApplicationContext(), FILENAME, 2);
        if (returnData instanceof Users) {
            users.setUsers(((Users) returnData).getUsers());
        }
        user_list = users;

        // Set the different views
        setViews();

    }

    private void setViews() {
        // Show Users in ScrollView
        setScrollView(new OnLocalUserStelectedListener() {
            @Override
            public void onLocalUserSelected() {
                setViews();
            }
        });

        // Set toggle button
        setToggleButton();
    }

    private void setScrollView(final OnLocalUserStelectedListener listener) {

        linearLayout_userList.removeAllViews();

        for (final User user : user_list.getUsers()) {
            TextView textView_tmp = new TextView(getApplicationContext());
            textView_tmp.setText(getResources().getString(R.string.main_activity_textview_scrollview,user.getFirstName(), user.getLastName()));
            textView_tmp.setHeight((int)getResources().getDimension(R.dimen.textView_height) * 3 / 4);
            textView_tmp.setTextSize(15);
            textView_tmp.setTextColor(Color.BLACK);
            textView_tmp.setBackgroundColor(Color.TRANSPARENT);
            textView_tmp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView_tmp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (user_selected == user) user_selected = null;
                    else user_selected = user;
                    listener.onLocalUserSelected();
                }
            });

            if (user == user_selected) {
                textView_tmp.setBackgroundColor(Color.LTGRAY);
            }
            linearLayout_userList.addView(textView_tmp);
        }
    }

    private void setToggleButton() {
        // Check toggle button type
        if (user_selected == null) {
            // Set up button to create local user
            toggle_sign_in_up.setText(getResources().getString(R.string.main_activity_toggle_button_new));
            toggle_sign_in_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showNewLocalUserDialog();
                }
            });

        } else {
            // Set up button to connect the local user
            toggle_sign_in_up.setText(getResources().getString(R.string.main_activity_toggle_button_selected));
            toggle_sign_in_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveJSONUserList();
                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    intent.putExtra("User", user_selected);
                    startActivity(intent);
                }
            });
        }
    }

    private void showNewLocalUserDialog() {
        // Create temporary View with setting value
        final CardView view_tmp = CreateDialogView();

        // Create alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage(getResources().getString(R.string.main_activity_toggle_button_new))
                .setCancelable(true)
                .setPositiveButton(getResources().getString(R.string.main_activity_dialog_button_positive),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Create new local User and create listener to refresh views when new local User is created
                                 Boolean ret = CreateLocalUser(new OnNewLocalUserListener() {
                                    @Override
                                    public void onNewLocalUser() {
                                        setViews();
                                    }
                                }, view_tmp);
                                if (ret) dialog.cancel();
                                else showNewLocalUserDialog();
                            }
                        }
                )
                .setNegativeButton(getResources().getString(R.string.main_activity_dialog_button_negative),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setView(view_tmp);

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

    private CardView CreateDialogView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(64,0,64,0);

        ArrayList<EditText> editTexts = new ArrayList();
        for (int i = 0; i < 3; i++) {
            EditText editText = new EditText(getApplicationContext());
            editText.setHeight( (int) getResources().getDimension(R.dimen.editText_height));
            editText.setTextSize(15);
            editText.setSingleLine(true);
            editText.setLayoutParams(params);
            editText.setId(View.generateViewId());

            if (i == 0) editText.setHint(getResources().getString(R.string.main_activity_firstname));
            if (i == 1) editText.setHint(getResources().getString(R.string.main_activity_lastname));
            if (i == 2) {
                editText.setHint(getResources().getString(R.string.main_activity_age));
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            }

            editTexts.add(editText);
        }

        RadioButton radioButton_male = new RadioButton(getApplicationContext());
        radioButton_male.setText(getResources().getString(R.string.main_activity_gender_male));
        radioButton_male.setId(View.generateViewId());

        RadioButton radioButton_female = new RadioButton(getApplicationContext());
        radioButton_female.setText(getResources().getString(R.string.main_activity_gender_female));
        radioButton_female.setId(View.generateViewId());

        RadioGroup radioGroup = new RadioGroup(getApplicationContext());
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        radioGroup.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        radioGroup.setGravity(Gravity.CENTER_HORIZONTAL);
        radioGroup.setId(View.generateViewId());
        radioGroup.addView(radioButton_male);
        radioGroup.addView(radioButton_female);

        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < editTexts.size(); i++) {
            linearLayout.addView(editTexts.get(i));
        }
        linearLayout.addView(radioGroup);

        CardView cardView = new CardView(getApplicationContext());
        cardView.setId(View.generateViewId());
        cardView.addView(linearLayout);

        return cardView;
    }

    private Boolean CreateLocalUser(OnNewLocalUserListener listener, CardView view_dialog) {
        // Get cardview LinearLayout
        LinearLayout layout = (LinearLayout) view_dialog.getChildAt(0);

        // Get new User informations
        ArrayList<String> user_information = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            EditText editText = (EditText) layout.getChildAt(i);
            user_information.add(editText.getText().toString());
        }

        RadioGroup radioGroup = (RadioGroup) layout.getChildAt(3);
        RadioButton radioButton;

        if (radioGroup.getCheckedRadioButtonId() == radioGroup.getChildAt(0).getId()) {
            radioButton = (RadioButton) radioGroup.getChildAt(0);
        } else if (radioGroup.getCheckedRadioButtonId() == radioGroup.getChildAt(1).getId()){
            radioButton = (RadioButton) radioGroup.getChildAt(1);
        } else radioButton = new RadioButton(getApplicationContext());

        user_information.add(String.valueOf(radioButton.getText() == getResources().getString(R.string.main_activity_gender_male) ? 1 : (radioButton.getText() == getResources().getString(R.string.main_activity_gender_female) ? 2 : 0)));

        // Check if EditText are filled
        for (int i = 0; i < user_information.size(); i++) {
            if (!(user_information.get(i).length() > 0)) return false;
            else if (user_information.get(i).equals("0")) return false;
        }

        // Create id for new local User
        int id_newUser = user_list.getUsers().size();
        if (id_newUser == 0) id_newUser = 1;
        else id_newUser = user_list.getUsers().get(id_newUser-1).getId()+1;

        // Add new local User to Users list
        User user_new = new User(
                id_newUser,
                user_information.get(0),
                user_information.get(1),
                user_information.get(3).equals("") ? 0 : Integer.parseInt(user_information.get(3)),
                Integer.parseInt(user_information.get(2)));
        user_list.getUsers().add(user_new);

        // Tell to the listener that new local User is created
        listener.onNewLocalUser();

        return true;
    }

    private void saveJSONUserList() {
        jsonOperation.SaveJson(getApplicationContext(), FILENAME, user_list);
    }

    public interface OnNewLocalUserListener {
        public void onNewLocalUser();
    }

    public interface OnLocalUserStelectedListener {
        public void onLocalUserSelected();
    }


}
