package com.example.lifeconnect.services;

import android.content.Context;

import com.example.lifeconnect.model.Message;
import com.example.lifeconnect.model.User;
import com.example.lifeconnect.model.Users;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class UserService {

    private AsyncHttpClient client;
    private Context context;

    private String BASE_URL = "http://10.0.2.2/Api/lifeconnect/user/";
    private String CONTENT_TYPE = "application/json";

    public UserService(Context context) {
        this.client = new AsyncHttpClient();
        this.context = context;
    }

    public void getUsers(final OnJSONResponseCallback callback) throws MalformedURLException {

        // Set url for read all user request
        String url = BASE_URL + "read.php";

        // Create GET request
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Called when response HTTP status is "200 OK"

                // Check if response is not null
                if (responseBody == null)  {
                    System.out.println("ERROR : getUsers(onSuccess) -> responseBody = null");
                    return;
                }

                // Convert byte[] in Users.class
                final Gson gsonBuilder = new GsonBuilder()
                        .serializeNulls()
                        .disableHtmlEscaping()
                        .setPrettyPrinting()
                        .create();

                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    Users users = gsonBuilder.fromJson(jsonObject.toString(), Users.class);

                    // Send users object
                    if (users != null) callback.onJSONResponse(true , users);
                    else callback.onJSONResponse(false, null);

                } catch (JSONException exception) {
                    System.out.println("ERROR : GetUsers(onSuccess) -> " + exception.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                System.out.println("ERROR : GetUsers(onFailure) -> " + error.getMessage());

                // Check if response is not null
                if (responseBody == null) {
                    System.out.println("ERROR : GetUsers(onFailure) -> responseBody = null");
                    return;
                }

                // Convert byte[] in Message.class
                final Gson gsonBuilder = new GsonBuilder()
                        .serializeNulls()
                        .disableHtmlEscaping()
                        .setPrettyPrinting()
                        .create();

                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    Message message = gsonBuilder.fromJson(jsonObject.toString(), Message.class);

                    // Send user object
                    if (message != null) callback.onJSONResponse(false, message);
                    else callback.onJSONResponse(false, null);

                } catch (JSONException exception) {
                    System.out.println("ERROR : GetUsers(onFailure) -> " + exception.getMessage());
                }
            }
        });
    }

    public void getOneUser(final OnJSONResponseCallback callback, String email) throws JSONException, UnsupportedEncodingException {

        // Set url for read one user request
        String url = BASE_URL + "read_one.php";

        // Create body params for request
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("email", email);

        // Convert jsonParams to string for the request
        StringEntity params = new StringEntity(jsonParams.toString());

        // Create GET request
        client.get(context, url, params, CONTENT_TYPE, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Called when response HTTP status is "200 OK"

                // Check if response is not null
                if (responseBody == null) {
                    System.out.println("ERROR : GetOneUser(onSuccess) -> responseBody = null");
                    return;
                }

                // Convert byte[] in User.class
                final Gson gsonBuilder = new GsonBuilder()
                        .serializeNulls()
                        .disableHtmlEscaping()
                        .setPrettyPrinting()
                        .create();

                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    User user = gsonBuilder.fromJson(jsonObject.toString(), User.class);

                    // Send user object
                    if (user != null) callback.onJSONResponse(true, user);
                    else callback.onJSONResponse(false, null);

                } catch (JSONException exception) {
                    System.out.println("ERROR : GetOneUser(onSuccess) -> " + exception.getMessage());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                System.out.println("ERROR : GetOneUser(onFailure) -> " + error.getMessage());

                // Check if response is not null
                if (responseBody == null) {
                    System.out.println("ERROR : GetOneUser(onFailure) -> responseBody = null");
                    return;
                }

                // Convert byte[] in Message.class
                final Gson gsonBuilder = new GsonBuilder()
                        .serializeNulls()
                        .disableHtmlEscaping()
                        .setPrettyPrinting()
                        .create();

                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    Message message = gsonBuilder.fromJson(jsonObject.toString(), Message.class);

                    // Send user object
                    if (message != null) callback.onJSONResponse(false, message);
                    else callback.onJSONResponse(false, null);

                } catch (JSONException exception) {
                    System.out.println("ERROR : GetOneUser(onFailure) -> " + exception.getMessage());
                }
            }
        });
    }

    public void createUser(final OnJSONResponseCallback callback, String email, String password) throws JSONException, UnsupportedEncodingException {

        // Set url for create user request
        String url = BASE_URL + "create.php";

        // Create body params for request
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("email", email);
        jsonParams.put("password", password);

        // Convert jsonParams to string for the request
        StringEntity params = new StringEntity(jsonParams.toString());

        // Create POST request
        client.post(context, url, params, CONTENT_TYPE, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Called when response HTTP status is "200 OK"

                // Check if response is not null
                if (responseBody == null) {
                    System.out.println("ERROR : CreateUser(onSuccess) -> responseBody = null");
                    return;
                }

                // Convert byte[] in Message.class
                final Gson gsonBuilder = new GsonBuilder()
                        .serializeNulls()
                        .disableHtmlEscaping()
                        .setPrettyPrinting()
                        .create();

                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    Message message = gsonBuilder.fromJson(jsonObject.toString(), Message.class);

                    // Send user object
                    if (message != null) callback.onJSONResponse(true, message);
                    else callback.onJSONResponse(false, null);

                } catch (JSONException exception) {
                    System.out.println("ERROR : CreateUser(onSuccess) -> " + exception.getMessage());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                System.out.println("ERROR : CreateUser(onFailure) -> " + error.getMessage());

                // Check if response is not null
                if (responseBody == null) {
                    System.out.println("ERROR : CreateUser(onFailure) -> responseBody = null");
                    return;
                }

                // Convert byte[] in Message.class
                final Gson gsonBuilder = new GsonBuilder()
                        .serializeNulls()
                        .disableHtmlEscaping()
                        .setPrettyPrinting()
                        .create();

                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    Message message = gsonBuilder.fromJson(jsonObject.toString(), Message.class);

                    // Send user object
                    if (message != null) callback.onJSONResponse(false, message);
                    else callback.onJSONResponse(false, null);

                } catch (JSONException exception) {
                    System.out.println("ERROR : CreateUser(onFailure) -> " + exception.getMessage());
                }
            }
        });
    }

    public void updateUser(final OnJSONResponseCallback callback, String email, String password, String firstName, String lastName, int gender, int age) throws JSONException, UnsupportedEncodingException {

        // Set url for update user request
        String url = BASE_URL + "update.php";

        // Create body params for request
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("email", email);
        jsonParams.put("password", password);
        jsonParams.put("firstName", firstName);
        jsonParams.put("lastName", lastName);
        jsonParams.put("gender", gender);
        jsonParams.put("age", age);

        // Convert jsonParams to string for the request
        StringEntity params = new StringEntity(jsonParams.toString());

        // Create POST request
        client.post(context, url, params, CONTENT_TYPE, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Called when response HTTP status is "200 OK"

                // Check if response is not null
                if (responseBody == null) {
                    System.out.println("ERROR : UpdateUser(onSuccess) -> responseBody = null");
                    return;
                }

                // Convert byte[] in Message.class
                final Gson gsonBuilder = new GsonBuilder()
                        .serializeNulls()
                        .disableHtmlEscaping()
                        .setPrettyPrinting()
                        .create();

                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    Message message = gsonBuilder.fromJson(jsonObject.toString(), Message.class);

                    // Send user object
                    if (message != null) callback.onJSONResponse(true, message);
                    else callback.onJSONResponse(false, null);

                } catch (JSONException exception) {
                    System.out.println("ERROR : UpdateUser(onSuccess) -> " + exception.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                System.out.println("ERROR : UpdateUser(onFailure) -> " + error.getMessage());

                // Check if response is not null
                if (responseBody == null) {
                    System.out.println("ERROR : UpdateUser(onFailure) -> responseBody = null");
                    return;
                }

                // Convert byte[] in Message.class
                final Gson gsonBuilder = new GsonBuilder()
                        .serializeNulls()
                        .disableHtmlEscaping()
                        .setPrettyPrinting()
                        .create();

                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    Message message = gsonBuilder.fromJson(jsonObject.toString(), Message.class);

                    // Send user object
                    if (message != null) callback.onJSONResponse(false, message);
                    else callback.onJSONResponse(false, null);

                } catch (JSONException exception) {
                    System.out.println("ERROR : UpdateUser(onFailure) -> " + exception.getMessage());
                }
            }
        });

    }

    public void deleteUser(final OnJSONResponseCallback callback, String email) throws JSONException, UnsupportedEncodingException {

        // Set url for delete user request
        String url = BASE_URL + "delete.php";

        // Create body params for request
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("email", email);

        // Convert jsonParams to string for the request
        StringEntity params = new StringEntity(jsonParams.toString());

        // Create POST request
        client.post(context, url, params, CONTENT_TYPE, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Called when response HTTP status is "200 OK"

                // Check if response is not null
                if (responseBody == null) {
                    System.out.println("ERROR : DeleteUser(onSuccess) -> responseBody = null");
                    return;
                }

                // Convert byte[] in Message.class
                final Gson gsonBuilder = new GsonBuilder()
                        .serializeNulls()
                        .disableHtmlEscaping()
                        .setPrettyPrinting()
                        .create();

                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    Message message = gsonBuilder.fromJson(jsonObject.toString(), Message.class);

                    // Send user object
                    if (message != null) callback.onJSONResponse(true, message);
                    else callback.onJSONResponse(false, null);

                } catch (JSONException exception) {
                    System.out.println("ERROR : DeleteUser(onSuccess) -> " + exception.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                System.out.println("ERROR : DeleteUser(onFailure) -> " + error.getMessage());

                // Check if response is not null
                if (responseBody == null) {
                    System.out.println("ERROR : DeleteUser(onFailure) -> responseBody = null");
                    return;
                }

                // Convert byte[] in Message.class
                final Gson gsonBuilder = new GsonBuilder()
                        .serializeNulls()
                        .disableHtmlEscaping()
                        .setPrettyPrinting()
                        .create();

                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    Message message = gsonBuilder.fromJson(jsonObject.toString(), Message.class);

                    // Send user object
                    if (message != null) callback.onJSONResponse(false, message);
                    else callback.onJSONResponse(false, null);

                } catch (JSONException exception) {
                    System.out.println("ERROR : DeleteUser(onFailure) -> " + exception.getMessage());
                }
            }
        });
    }

    public interface OnJSONResponseCallback {
        public void onJSONResponse(boolean success, Object object);
    }
}
