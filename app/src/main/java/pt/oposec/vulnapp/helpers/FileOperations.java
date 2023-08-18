package pt.oposec.vulnapp.helpers;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileOperations {
    private final Context context;
    private final String APP_TAG;
    public FileOperations(Context context, String class_tag){
        this.context=context;
        this.APP_TAG =class_tag;
    }

    public boolean isFileNotFound(String filePath) {
        return !(new File(context.getApplicationInfo().dataDir + "/files/" + filePath)).exists();
    }

    public void writeToFile(String data, String filePath) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filePath, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e(APP_TAG, "File write failed: " + e.toString());
        }
    }

    public String readFromFile(String filePath) {

        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(filePath);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }
                bufferedReader.close();
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e(APP_TAG, "File not found: " + e);
        } catch (IOException e) {
            Log.e(APP_TAG, "Can not read file: " + e);
        }
        return ret;
    }


}
