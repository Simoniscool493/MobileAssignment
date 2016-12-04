package com.example.simon.mobileassignment;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Simon on 26/11/2016.
 * Abstract file manager class, to store file saving and loading methods which are common throughout the app.
 * Files are named after their unique rowId in the database, preventing two files from having the same name
 */

public abstract class FileManager
{
    static void saveSong(Song s,String name,Activity currentActivity)
    {
        try
        {
            FileOutputStream fos = currentActivity.getApplicationContext().openFileOutput(name, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(s);
            os.close();
            fos.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    static Song loadSong(String songRowId,Activity currentActivity)
    {
        Song s = null;

        try
        {
            FileInputStream fis = currentActivity.getApplicationContext().openFileInput(songRowId);
            ObjectInputStream is = new ObjectInputStream(fis);
            s = (Song)is.readObject();
            is.close();
            fis.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(currentActivity, "Error: File not recognized", Toast.LENGTH_SHORT).show();
            currentActivity.finish();
        }

        return s;
    }
}
