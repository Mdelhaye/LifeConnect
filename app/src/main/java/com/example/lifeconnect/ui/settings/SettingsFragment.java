package com.example.lifeconnect.ui.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.lifeconnect.JavaClass.JsonOperation;
import com.example.lifeconnect.MenuActivity;
import com.example.lifeconnect.R;
import com.example.lifeconnect.model.User;
import com.example.lifeconnect.model.Users;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {

    private ArrayList<CardView> arrayList_cardViews;
    private User user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        // Get User settings
        MenuActivity menuActivity = (MenuActivity) getActivity();
        user = menuActivity.getUser();

        // Update User informations
        JsonOperation jsonOperation = new JsonOperation();
        Users users = (Users) jsonOperation.getObjectFromJson(getContext(), "Users.json", 2);
        for (User currentUser : users.getUsers()) {
            if (user.getFirstName().equals(currentUser.getFirstName())) {
                if (user.getLastName().equals(currentUser.getLastName())) {
                    user = currentUser;
                }
            }
        }

        // Get group from layout
        Group group = root.findViewById(R.id.fragment_settings_group);

        // Add group's Cardview in arraylist
        arrayList_cardViews = new ArrayList<>();
        int cardviews_id[] = group.getReferencedIds();
        for (int id : cardviews_id) {
            arrayList_cardViews.add((CardView) root.findViewById(id));
        }

        // Set User settings
        SetUserSettingsValue();

        return root;
    }

    private void SetUserSettingsValue() {
        LinearLayout linearLayout;

        for (CardView cardView : arrayList_cardViews) {
            // Get CardView's LinearLayout
            linearLayout = (LinearLayout) cardView.getChildAt(0);

            // Get cardView's TextViews
            final ArrayList<TextView> arrayList_currentCardView_textViews = new ArrayList<>();
            arrayList_currentCardView_textViews.add((TextView) linearLayout.getChildAt(0));
            arrayList_currentCardView_textViews.add((TextView) linearLayout.getChildAt(1));

            // Set setting value
            SetSettingValue(arrayList_currentCardView_textViews);

            // Create onClickListener who call a dialog to update setting value
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createSettingDialog(new OnSettingChangeListener() {
                                            @Override
                                            public void onSettingChange(String title, String value) {
                                                // Get the new setting value
                                                SetSettingValue(arrayList_currentCardView_textViews, title, value);
                                            }
                                        },
                            arrayList_currentCardView_textViews.get(0).getText().toString(),
                            arrayList_currentCardView_textViews.get(1).getText().toString());
                }
            });
        }
    }

    private void SetSettingValue(ArrayList<TextView> textViews, String... newSetting) {
        // Check whether to update User setting or initialize Activity setting
        if (newSetting.length == 0) {
            // initialize Activity setting with User value

            switch (textViews.get(0).getText().toString()) {
                case "Adresse e-mail":
                    textViews.get(1).setText(user.getEmail() == null        ? "" : user.getEmail());
                    break;
                case "Mot de passe":
                    textViews.get(1).setText(user.getPassword() == null     ? "" : user.getPassword());
                    break;
                case "Prénom":
                    textViews.get(1).setText(user.getFirstName() == null    ? "" : user.getFirstName());
                    break;
                case "Nom":
                    textViews.get(1).setText(user.getLastName() == null     ? "" : user.getLastName());
                    break;
                case "Homme / Femme":
                    textViews.get(1).setText(user.getGender() == 1 ? getResources().getString(R.string.settings_fragment_male) : user.getGender() == 2 ? getResources().getString(R.string.settings_fragment_female) : "");
                    break;
                case "Age":
                    textViews.get(1).setText(String.valueOf(user.getAge()));
                    break;
            }
        } else {
            // Update User setting with new value

            // Get the Setting attribute and the new value
            String title = newSetting.length > 0 ? newSetting[0] : "";
            String value = newSetting.length > 1 ? newSetting[1] : "";

            switch (title) {
                case "Prénom":
                    user.setFirstName(value);
                    break;
                case "Nom":
                    user.setLastName(value);
                    break;
                case "Homme / Femme":
                    user.setGender(Integer.valueOf(value));
                    break;
                case "Age":
                    user.setAge(Integer.valueOf(value));
                    break;
            }

            // Update Activity Setting
            SetSettingValue(textViews);
        }
    }

    private void createSettingDialog(final OnSettingChangeListener listener, final String title, String value) {
        // Create temporary View with setting value
        final View view_dialog = CreateTmpView(title, value);

        // Create alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage(getResources().getString(R.string.settings_fragment_dialog_title, title))
                .setCancelable(true)
                .setPositiveButton(
                        getResources().getText(R.string.settings_fragment_dialog_button_positive),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Update the setting value
                                if (view_dialog instanceof EditText) {
                                    listener.onSettingChange(title, ((EditText) view_dialog).getText().toString());
                                    dialog.cancel();
                                }
                                if (view_dialog instanceof RadioGroup) {
                                    RadioGroup radioGroup = (RadioGroup) view_dialog;
                                    if (radioGroup.getCheckedRadioButtonId() == radioGroup.getChildAt(0).getId()) {
                                        listener.onSettingChange(title, "1");
                                    }
                                    if (radioGroup.getCheckedRadioButtonId() == radioGroup.getChildAt(1).getId()) {
                                        listener.onSettingChange(title, "2");
                                    }
                                    dialog.cancel();
                                }
                            }
                        })
                .setNegativeButton(
                        getResources().getText(R.string.settings_fragment_dialog_button_negative),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setView(view_dialog);

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

    private View CreateTmpView(String title, String value) {
        // Check setting type
        switch (title) {
            case "Homme / Femme":
                RadioButton button_male = new RadioButton(getContext());
                button_male.setText(getResources().getString(R.string.settings_fragment_male));
                button_male.setId(View.generateViewId());

                RadioButton button_female = new RadioButton(getContext());
                button_female.setText(getResources().getString(R.string.settings_fragment_female));
                button_female.setId(View.generateViewId());

                RadioGroup view_gender = new RadioGroup(getContext());
                view_gender.setOrientation(RadioGroup.VERTICAL);
                view_gender.setId(View.generateViewId());
                view_gender.addView(button_male);
                view_gender.addView(button_female);
                return view_gender;
            case "Age":
                EditText view_age = new EditText(getContext());
                view_age.setText(value);
                view_age.setSingleLine(true);
                view_age.setInputType(InputType.TYPE_CLASS_NUMBER);
                return view_age;
            default:
                EditText view_default = new EditText(getContext());
                view_default.setText(value);
                view_default.setSingleLine(true);
                return view_default;
        }
    }

    private void saveJSONUserList() {
        String FILENAME = "Users.json";
        JsonOperation jsonOperation = new JsonOperation();

        // Get json User list
        Users user_list = new Users(new ArrayList<User>());
        user_list.setUsers(((Users) jsonOperation.getObjectFromJson(getContext(), FILENAME, 2)).getUsers());

        // Find User to update it in the User list
        for (User user_json : user_list.getUsers()) {
            if (user_json.getId() == user.getId()) {
                user_json.setFirstName(user.getFirstName());
                user_json.setLastName(user.getLastName());
                user_json.setGender(user.getGender());
                user_json.setAge(user.getAge());
            }
        }

        // Save User list in json
        jsonOperation.SaveJson(getContext(), FILENAME, user_list);
    }

    @Override
    public void onStop() {
        saveJSONUserList();
        super.onStop();
    }

    private void showToast(String text) {
        Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_LONG);
        toast.show();
    }

    private interface OnSettingChangeListener {
        public void onSettingChange(String title, String value);
    }
}