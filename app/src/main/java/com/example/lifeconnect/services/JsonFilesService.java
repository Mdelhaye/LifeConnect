package com.example.lifeconnect.services;

import android.content.Context;

import com.example.lifeconnect.model.FilesOnline;
import com.example.lifeconnect.model.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class JsonFilesService {

    private AsyncHttpClient client;
    private Context context;

    private String BASE_URL = "http://10.0.2.2/Api/lifeconnect/jsonfiles/";
    private String CONTENT_TYPE = "application/json";

    public JsonFilesService(Context context) {
        this.client = new AsyncHttpClient();
        this.context = context;
    }

    public void getOneFile(final OnJSONResponseCallback callback, String name, int idUser) throws JSONException, UnsupportedEncodingException {

        // Set url for read one jsonFile request
        String url = BASE_URL + "read_one.php";

        // Create body params for request
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("name", name);
        jsonParams.put("idUser", idUser);

        // Convert jsonParams to string for the request
        StringEntity params = new StringEntity(jsonParams.toString());

        // Create GET request
        client.get(context, url, params, CONTENT_TYPE, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Called when response HTTP status is "200 OK"

                // Check if response is not null
                if (responseBody == null) {
                    System.out.println("ERROR : getOneFile(onSuccess) -> responseBody = null");
                    return;
                }

                // Convert byte[] in FilesOnline.class
                final Gson gsonBuilder = new GsonBuilder()
                        .serializeNulls()
                        .disableHtmlEscaping()
                        .setPrettyPrinting()
                        .create();

                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    FilesOnline filesOnline = gsonBuilder.fromJson(jsonObject.toString(), FilesOnline.class);

                    // Send FilesOnline object
                    if (filesOnline != null) callback.onJSONResponse(true, filesOnline);
                    else callback.onJSONResponse(false, null);

                } catch (JSONException exception) {
                    System.out.println("ERROR : getOneFile(onSuccess) -> " + exception.getMessage());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                System.out.println("ERROR : getOneFile(onFailure) -> " + error.getMessage());

                // Check if response is not null
                if (responseBody == null) {
                    System.out.println("ERROR : getOneFile(onFailure) -> responseBody = null");
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
                    System.out.println("ERROR : getOneFile(onFailure) -> " + exception.getMessage());
                }
            }
        });
    }

    public void createFile(final OnJSONResponseCallback callback, String name, String fileToString, int idUser) throws JSONException, UnsupportedEncodingException {

        // Set url for create jsonFile request
        String url = BASE_URL + "create.php";

        // Create body params for request
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("name", name);
        jsonParams.put("fileToString", fileToString);
        jsonParams.put("idUser", idUser);

        // Convert jsonParams to string for the request
        StringEntity params = new StringEntity(jsonParams.toString());

        // Create POST request
        client.post(context, url, params, CONTENT_TYPE, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Called when response HTTP status is "200 OK"

                // Check if response is not null
                if (responseBody == null) {
                    System.out.println("ERROR : CreateFile(onSuccess) -> responseBody = null");
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
                    System.out.println("ERROR : CreateFile(onSuccess) -> " + exception.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                System.out.println("ERROR : CreateFile(onFailure) -> " + error.getMessage());

                // Check if response is not null
                if (responseBody == null) {
                    System.out.println("ERROR : CreateFile(onFailure) -> responseBody = null");
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
                    System.out.println("ERROR : CreateFile(onFailure) -> " + exception.getMessage());
                }
            }
        });
    }

    public void updateFile(final OnJSONResponseCallback callback, String name, String fileToString, int idUser) throws JSONException, UnsupportedEncodingException {

        // Set url for update jsonFile request
        String url = BASE_URL + "update.php";

        // Create body params for request
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("name", name);
        jsonParams.put("fileToString", fileToString);
        jsonParams.put("idUser", idUser);

        // Convert jsonParams to string for the request
        StringEntity params = new StringEntity(jsonParams.toString());

        // Create POST request
        client.post(context, url, params, CONTENT_TYPE, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Called when response HTTP status is "200 OK"

                // Check if response is not null
                if (responseBody == null) {
                    System.out.println("ERROR : updateFile(onSuccess) -> responseBody = null");
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
                    System.out.println("ERROR : updateFile(onSuccess) -> " + exception.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                System.out.println("ERROR : updateFile(onFailure) -> " + error.getMessage());

                // Check if response is not null
                if (responseBody == null) {
                    System.out.println("ERROR : updateFile(onFailure) -> responseBody = null");
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
                    System.out.println("ERROR : updateFile(onFailure) -> " + exception.getMessage());
                }
            }
        });
    }

    public interface OnJSONResponseCallback {
        public void onJSONResponse(boolean success, Object object);
    }

}
