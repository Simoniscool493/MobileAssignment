package com.example.simon.mobileassignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Simon on 06/11/2016.
 * Class for the main app menu
 */

public class MainMenu extends AppCompatActivity implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_menu);

        //Initializes the abstract class MusicPlayer by assigning all its static variables, which are used by
        //many different screens in the app
        MusicPlayer.init();

        Button newSongButton = (Button)findViewById(R.id.newsongbutton);
        Button playSongButton = (Button)findViewById(R.id.playsongbutton);
        Button editSongButton = (Button)findViewById(R.id.editsongbutton);
        Button quitButton = (Button)findViewById(R.id.quitbutton);

        newSongButton.setOnClickListener(this);
        playSongButton.setOnClickListener(this);
        editSongButton.setOnClickListener(this);
        quitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.newsongbutton)
        {
            Intent i = new Intent(v.getContext(),NewSongScreen.class);
            startActivity(i);
        }
        else if(v.getId()==R.id.playsongbutton)
        {
            Intent i = new Intent(v.getContext(),SongDatabaseScreen.class);
            //Opens the song database screen, telling it that when a song is selected, to load it into the PlayBack screen
            i.putExtra("OpenSongFor","Playing");
            startActivity(i);
        }
        else if(v.getId()==R.id.editsongbutton)
        {
            Intent i = new Intent(v.getContext(),SongDatabaseScreen.class);
            //Opens the song database screen, telling it that when a song is selected, to load it into the editing screen
            i.putExtra("OpenSongFor","Editing");
            startActivity(i);
        }
        else if(v.getId()==R.id.quitbutton)
        {
            finish();
            System.exit(0);
        }
    }
}
