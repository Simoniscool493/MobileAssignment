package com.example.simon.mobileassignment;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Simon on 15/11/2016.
 * Class holding all the information about a song file
 */

public class Song implements Serializable
{
    int numKeys = 12;

    String rowId;
    int bpm;
    int timerInc;
    String key;
    String name;

    ArrayList<NoteBlock> data;

    //The inner class NoteBlock stores all the data about one beat of a song
    public class NoteBlock implements Serializable
    {
        boolean isChord;
        int note;

        NoteBlock(int n1,Boolean c)
        {
            isChord = c;
            note = n1;
        }

        @Override
        public String toString()
        {
            if(!isChord)
            {
                return String.valueOf(note);
            }

            return String.valueOf(note) + " " + String.valueOf(note+2) + " " + String.valueOf(note+4);
        }
    }

    Song(String n,String k,int b)
    {
        bpm = b;
        timerInc = (int)(6000.0/(float)bpm);
        name = n;
        key = k;
        data = new ArrayList<NoteBlock>();
    }

    //Write one noteblock
    void write(int p,Boolean c)
    {
        Log.d("Note",String.valueOf(p));
        data.add(new NoteBlock(p,c));
    }

    //read one noteblock
    NoteBlock read(int p)
    {
        NoteBlock n;

        try
        {
            n = data.get(p);
        }
        catch(IndexOutOfBoundsException e)
        {
            return null;
        }

        return n;
    }

    @Override
    public String toString()
    {
        String s = "";

        for(NoteBlock n:data)
        {
            s = s + n.toString();
        }

        return s;
    }
}
