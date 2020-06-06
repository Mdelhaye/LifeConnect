package com.example.lifeconnect.JavaClass;

import android.content.Context;
import android.util.Log;

import com.example.lifeconnect.model.LocalDatas;
import com.example.lifeconnect.model.LocalStructures;
import com.example.lifeconnect.model.Users;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileOutputStream;
import java.io.InputStream;

public class JsonOperation {

    public Object getObjectFromJson(Context context, String FILENAME, int type) {
        Object lclObject;
        final Gson gson = new GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create();

        String resultFromJson = "";


        try {
            InputStream inputStream = context.openFileInput(FILENAME);
            int content;
            while ((content = inputStream.read()) != -1) {
                resultFromJson += (char) content;
            }
            inputStream.close();
            if (type == 0) {
                lclObject = gson.fromJson(resultFromJson, LocalStructures.class);
                return lclObject;
            }
            else if (type == 1) {
                lclObject = gson.fromJson(resultFromJson, LocalDatas.class);
                return lclObject;
            }
            else if (type == 2) {
                lclObject = gson.fromJson(resultFromJson, Users.class);
                return lclObject;
            }
            return null;
        } catch (Exception e) {
            Log.e("Erreur", e.toString());
            return null;
        }
    }

    public void SaveJson(Context context, String FILENAME, Object object) {
        final Gson gson = new GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create();
        String fileContents = gson.toJson(object);
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
            Log.i("TODO", "Sauvegarde du fichier " + FILENAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String GetJsonString(Context context, String FILENAME) {
        final Gson gson = new GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create();

        String resultFromJson = "";


        try {
            InputStream inputStream = context.openFileInput(FILENAME);
            int content;
            while ((content = inputStream.read()) != -1) {
                resultFromJson += (char) content;
            }
            inputStream.close();
            return resultFromJson;
        } catch (Exception e) {
            Log.e("Erreur", e.toString());
            return null;
        }
    }
}
