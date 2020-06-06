package com.example.lifeconnect;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lifeconnect.JavaClass.JsonOperation;
import com.example.lifeconnect.model.DataPerUser;
import com.example.lifeconnect.model.Espace;
import com.example.lifeconnect.model.LocalDatas;
import com.example.lifeconnect.model.LocalStructures;
import com.example.lifeconnect.model.Message;
import com.example.lifeconnect.model.Structure;
import com.example.lifeconnect.model.User;
import com.example.lifeconnect.model.Users;
import com.example.lifeconnect.services.JsonFilesService;
import com.example.lifeconnect.services.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class SignInActivity extends AppCompatActivity {

    private Button button_sign_in, button_sign_up;
    private EditText editText_email, editText_password;
    private User user;

    @Override
    protected void onCreate(Bundle state){
        super.onCreate(state);
        setContentView(R.layout.activity_sign_in);

        // Get User from Intent
        user = (User) getIntent().getSerializableExtra("User");

        // Update User informations
        JsonOperation jsonOperation = new JsonOperation();
    Users users = (Users) jsonOperation.getObjectFromJson(getApplicationContext(), "Users.json", 2);
        for (User currentUser : users.getUsers()) {
            if (user.getFirstName().equals(currentUser.getFirstName())) {
                if (user.getLastName().equals(currentUser.getLastName())) {
                    user = currentUser;
                }
            }
        }

        // Find layout view
        editText_email = findViewById(R.id.activity_sign_in_input_email);
        editText_password = findViewById(R.id.activity_sign_in_input_password);
        button_sign_up = findViewById(R.id.activity_sign_in_button_sign_up);
        button_sign_in = findViewById(R.id.activity_sign_in_button_sign_in);

        //  Turn off sign in button if user doesn't have email
        if (user.getPassword() == null) button_sign_in.setEnabled(false);

        // Set button to start SignUp activity
        button_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent to start SignUpActivity
                Intent signUpIntent = new Intent(SignInActivity.this, SignUpActivity.class);
                signUpIntent.putExtra("User", user);
                startActivityForResult(signUpIntent, 1);
            }
        });

        // Set the button to verify logins, if true start Menu activity
        button_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get login from user
                final String email = editText_email.getText().toString();
                final String password = editText_password.getText().toString();

                if (!user.getEmail().equals(email)) {
                    if (!user.getPassword().equals(password)) {
                        showToast(getResources().getString(R.string.sign_in_activity_login_fail));
                        return;
                    }
                }

                // Check if user exist
                UserService userService = new UserService(getApplicationContext());

                try {
                    userService.getOneUser(new UserService.OnJSONResponseCallback() {
                        @Override
                        public void onJSONResponse(boolean success, Object object) {
                            if (success) {
                                // Get request object
                                User user_request = (User) object;

                                // Check if password is correct
                                if (password.equals(user_request.getPassword())) {
                                    showToast(getResources().getString(R.string.sign_in_activity_login_success));

//                                    // Create intent to start MenuActivity
//                                    Intent menuIntent = new Intent( SignInActivity.this, MenuActivity.class);
//                                    menuIntent.putExtra("User", user);
//                                    startActivity(menuIntent);
                                    SaveJSONinDataBase(user_request);
                                } else {
                                    showToast(getResources().getString(R.string.sign_in_activity_login_fail));
                                }

                            } else {
                                showToast(getResources().getString(R.string.sign_in_activity_login_fail));
                            }
                        }
                    }, email);
                } catch (UnsupportedEncodingException | JSONException exception) {
                    System.out.println("ERROR : SignInButton.OnClick -> " + exception.getMessage());
                }
            }
        });
    }

    private void SaveJSONinDataBase(final User user_request) {
        final ArrayList<String> jsonFilesName = new ArrayList<>();
        jsonFilesName.add("StructureDesEspaces.json");
        jsonFilesName.add("DonneesLocales.json");

        final ArrayList<String> jsonFiles = FindUserDatas(jsonFilesName);

        final JsonFilesService jsonFilesService = new JsonFilesService(SignInActivity.this);

        for (int i = 0; i < jsonFiles.size(); i++) {
            try {
                final int finalI = i;
                jsonFilesService.getOneFile(new JsonFilesService.OnJSONResponseCallback() {
                    @Override
                    public void onJSONResponse(boolean success, Object object) {
                        if (success) {
                            try {
                                jsonFilesService.updateFile(new JsonFilesService.OnJSONResponseCallback() {
                                    @Override
                                    public void onJSONResponse(boolean success, Object object) {
                                        // Get request response
                                        Message message = (Message) object;

                                        // Show response
                                        System.out.println("SaveJSONinDataBase.updateFile -> message : " + message.getMessage());

                                        if (success) {
                                            showToast("Sauvegarde réussie!");
                                            finish();
                                        } else {
                                            showToast("Sauvegarde échouée, veuillez réessayer!");
                                        }
                                    }
                                }, jsonFilesName.get(finalI), jsonFiles.get(finalI), user_request.getId());
                            } catch (JSONException | UnsupportedEncodingException exception) {
                                System.out.println("ERROR : SaveJSONinDataBase.updateFile -> " + exception.getMessage());
                            }
                        } else {
                            try {
                                jsonFilesService.createFile(new JsonFilesService.OnJSONResponseCallback() {
                                    @Override
                                    public void onJSONResponse(boolean success, Object object) {
                                        // Get request response
                                        Message message = (Message) object;

                                        // Show response
                                        System.out.println("SaveJSONinDataBase.createFile -> message : " + message.getMessage());

                                        if (success) {
                                            showToast("Sauvegarde réussie!");
                                            finish();
                                        } else {
                                            showToast("Sauvegarde échouée, veuillez réessayer!");
                                        }
                                    }
                                }, jsonFilesName.get(finalI), jsonFiles.get(finalI), user_request.getId());
                            } catch (JSONException | UnsupportedEncodingException exception) {
                                System.out.println("ERROR : SaveJSONinDataBase.createFile -> " + exception.getMessage());
                            }
                        }
                    }
                }, jsonFilesName.get(finalI), user_request.getId());
            } catch (JSONException | UnsupportedEncodingException exception) {
                System.out.println("ERROR : SaveJSONinDataBase.getOneFile -> " + exception.getMessage());
            }
        }

    }

    private ArrayList<String> FindUserDatas(ArrayList<String> jsonFilesName) {
        JsonOperation jsonOperation = new JsonOperation();

        Structure structureToSave = null;
        DataPerUser dataPerUserToSave = null;

        LocalStructures localStructures = (LocalStructures) jsonOperation.getObjectFromJson(getApplicationContext(), jsonFilesName.get(0), 0);
        for (Structure currentStructure : localStructures.getStructures()) {
            if (currentStructure.getUser() == user.getId()) {
                structureToSave = currentStructure;
            }
        }

        LocalDatas localDatas = (LocalDatas) jsonOperation.getObjectFromJson(getApplicationContext(), jsonFilesName.get(1), 1);
        for (DataPerUser currentDataPerUser : localDatas.getDataPerUsers()) {
            if (currentDataPerUser.getUser() == user.getId()) {
                dataPerUserToSave = currentDataPerUser;
            }
        }

        final Gson gson = new GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create();
        String structureToSaveString = gson.toJson(structureToSave);
        String dataPerUserToSaveString = gson.toJson(dataPerUserToSave);

        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("'" + structureToSaveString + "'");
        stringArrayList.add("'" + dataPerUserToSaveString + "'");

        return stringArrayList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if(resultCode == RESULT_OK){
                Object returnData = data.getSerializableExtra("User");
                if (returnData instanceof User) {
                    this.user = (User) returnData;
                }

                //  Turn on sign in button if User have email
                if (user.getPassword() != null) button_sign_in.setEnabled(true);
            }
            if (resultCode == RESULT_CANCELED) {
                //Do nothing?
            }
        }
    }

    private void showToast(String text) {
        Toast toast = Toast.makeText(SignInActivity.this, text, Toast.LENGTH_LONG);
        toast.show();
    }
}
