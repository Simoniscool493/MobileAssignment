package com.example.simon.mobileassignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by Simon on 06/11/2016.
 * Class for the new song screen, where users specify a name, key and bpm for a new song
 */

public class NewSongScreen extends AppCompatActivity
{
    Button doneButton,cancelButton;
    EditText songName,songBPM;
    Spinner songKey;

    String keySelection = "No key";
    //key selection for the spinner, changed every time the spinner is updated

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_newsong_screen);
        configureButtons();
    }

    public void configureButtons()
    {
        doneButton = (Button)findViewById(R.id.donebutton);
        cancelButton = (Button)findViewById(R.id.cancelbutton);
        songName = (EditText)findViewById(R.id.songnameedittext);
        songBPM = (EditText)findViewById(R.id.bpmedittext);
        songKey = (Spinner)findViewById(R.id.songkeychooser);

        songKey.setSelection(4);

        //adds all recorded data to the intent and uses it to make a new recording screen with that data
        doneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(v.getContext(),RecordingScreen.class);
                i.putExtra("songName", songName.getText().toString());
                i.putExtra("songBPM", songBPM.getText().toString());
                i.putExtra("songKey", keySelection);
                startActivity(i);

                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        songKey.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                keySelection = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}
