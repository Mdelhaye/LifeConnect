package com.example.lifeconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lifeconnect.JavaClass.JsonOperation;
import com.example.lifeconnect.model.Message;
import com.example.lifeconnect.model.User;
import com.example.lifeconnect.model.Users;
import com.example.lifeconnect.services.UserService;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    private Button button_signUp;
    private EditText editText_email, editText_password, editText_passwordConfirm;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Get User from Intent
        user = (User) getIntent().getSerializableExtra("User");

        // Find layout view
        editText_email = findViewById(R.id.activity_sign_up_input_email);
        editText_password = findViewById(R.id.activity_sign_up_input_password);
        editText_passwordConfirm = findViewById(R.id.activity_sign_up_input_password_confirm);
        button_signUp = findViewById(R.id.activity_sign_up_button_sign_up);

        // Check login and add User to database, if success go back to Main activity
        button_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get login from user
                String email = editText_email.getText().toString();
                String password = editText_password.getText().toString();
                String confirmPassword = editText_passwordConfirm.getText().toString();

                // Check if password ok
                if (password.equals(confirmPassword))
                {
                    // Create UserService
                    final UserService userService = new UserService(getApplicationContext());

                    // Create User
                    user.setEmail(email);
                    user.setPassword(password);
                    try {
                        userService.createUser(new UserService.OnJSONResponseCallback() {
                            @Override
                            public void onJSONResponse(boolean success, Object object) {
                                // Get request response
                                Message message = (Message) object;

                                // Show response
                                System.out.println("SignUpButton.OnClick(OnJSONResponse) -> message : " + message.getMessage());

                                if (success) {
                                    showToast(getResources().getString(R.string.sign_up_activity_sign_up_success));

                                    // Update User information
                                    try {
                                        UpdateUser(userService, user);
                                    } catch (UnsupportedEncodingException | JSONException exception) {
                                        System.out.println("ERROR : SignUpButton.OnClick -> " + exception.getMessage());
                                    }
                                } else {
                                    showToast(getResources().getString(R.string.sign_up_activity_sign_up_fail));
                                }
                            }
                        }, user.getEmail(), user.getPassword());
                    } catch (UnsupportedEncodingException | JSONException exception) {
                        System.out.println("ERROR : SignUpButton.OnClick -> " + exception.getMessage());
                    }
                } else {
                    showToast(getResources().getString(R.string.sign_up_activity_password_different));
                }
            }
        });
    }

    private void UpdateUser(UserService userService, final User user) throws UnsupportedEncodingException, JSONException {
        userService.updateUser(new UserService.OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(boolean success, Object object) {
                // Get request response
                Message message = (Message) object;

                // Show response
                System.out.println("SignUpButton.OnClick(OnJSONResponse) -> message : " + message.getMessage());

                if (success) {
                    showToast(getResources().getString(R.string.sign_up_activity_sign_up_success));

                    // Return User
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("User", user);
                    setResult(RESULT_OK, returnIntent);

                    finish();
                } else {
                    showToast(getResources().getString(R.string.sign_up_activity_sign_up_fail));
                }

            }
        }, user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getGender(), user.getAge());
    }

    private void saveJSONUserList() {
        String FILENAME = "Users.json";
        JsonOperation jsonOperation = new JsonOperation();

        // Get json User list
        Users user_list = new Users(new ArrayList<User>());
        user_list.setUsers(((Users) jsonOperation.getObjectFromJson(getApplicationContext(), FILENAME, 2)).getUsers());

        // Find User to update it in the User list
        for (User user_json : user_list.getUsers()) {
            if (user_json.getId() == user.getId()) {
                user_json.setEmail(user.getEmail());
                user_json.setPassword(user.getPassword());
                user_json.setFirstName(user.getFirstName());
                user_json.setLastName(user.getLastName());
                user_json.setGender(user.getGender());
                user_json.setAge(user.getAge());
            }
        }

        // Save User list in json
        jsonOperation.SaveJson(getApplicationContext(), FILENAME, user_list);
    }

    @Override
    public void onStop() {
        saveJSONUserList();
        super.onStop();
    }

    private void showToast(String text) {
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
        toast.show();
    }
}
