package com.example.simon.mobileassignment;

import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;

/**
 * Created by Simon on 25/11/2016.
 * Class to store all sound files and mappings of keys, sounds and starting notes for keys
 */

public abstract class MusicPlayer
{
    //soundReferences map, which maps the id numbers of specifc keys to their file reference in R.raw. used to load
    //the sounds into a given song when it's played
    static HashMap soundReferences;

    //keyMapping map, which maps the names of keys (A Major, C Minor, etc) to a boolean array of notes in that key.
    //used to configure the sounds loaded into a given song when it is played
    static HashMap keyMapping;

    //startingKeys map, which maps the names of keys to the note they start with. used to configure the sounds loaded
    //into a given song when it is played
    static HashMap startingNotes;

    //keyNamesNumber map, which maps the names of keys to their index numbers in strings.xml. this is for displaying
    //the right key name in the key spinner on the editing screen.
    static HashMap keyNamesNumbers;

    //String array containing the names of all notes
    static String[] noteNames = {"A","A#","B","C","C#","D","D#","E","F","F#","G","G#"};

    //String array containing the names of the notes in the key most recently loaded. accessed by the
    //PianoKeys class to name its keys
    static String[] lastMadeNoteNames = {"A","A#","B","C","C#","D","D#","E","F","F#","G","G#"};

    MusicPlayer() {}

    static void init()
    {
        soundReferences = getSoundReferences();
        keyMapping = getKeyMapping();
        startingNotes = getStartingNotes();
        keyNamesNumbers = getKeyNamesNumbers();
    }

    //Method that takes in a key, and returns a SoundPool map object with the first X notes in that key, plus
    //the number of extra keys needed to make chords from every note in that SoundPool
    static HashMap makeSoundPoolMapFromKey(String key, AppCompatActivity parent,SoundPool sounds,int numKeys)
    {
        int numKeysPlusExtra = numKeys + 4;

        HashMap soundPoolMap = new HashMap<>();

        //Gets the keyMapping for this key, which describes the notes in the key
        boolean[] notes = (boolean[])keyMapping.get(key);

        int keysAssiged = 0;
        int counter = 0;
        int startingNote = (int)startingNotes.get(key);

        //Loop that runs until the soundpool is filled, starting with the starting note of the key
        while(keysAssiged<numKeysPlusExtra)
        {
            //Int that stores which note is currently being checked
            int noteIndexNumber = ((startingNote+counter-1)%12);

            //If the current note is in the key, add the current note to the soundpool and move to the next note and the next key.
            if(notes[noteIndexNumber%12])
            {
                soundPoolMap.put(keysAssiged, sounds.load(parent,(int)(soundReferences.get(startingNote+counter)), 1));

                if(keysAssiged<12)
                {
                    lastMadeNoteNames[keysAssiged] = noteNames[noteIndexNumber];
                }

                keysAssiged++;

            }
            counter++;
        }

        return soundPoolMap;

    }

    private static HashMap getStartingNotes()
    {
        HashMap m = new HashMap();

        m.put("No key",28);
        m.put("A Major",25);
        m.put("A# Major",26);
        m.put("B Major",27);
        m.put("C Major",28);
        m.put("C# Major",29);
        m.put("D Major",30);
        m.put("D# Major",31);
        m.put("E Major",32);
        m.put("F Major",33);
        m.put("F# Major",34);
        m.put("G Major",35);
        m.put("G# Major",36);

        m.put("A Minor",25);
        m.put("A# Minor",26);
        m.put("B Minor",27);
        m.put("C Minor",28);
        m.put("C# Minor",29);
        m.put("D Minor",30);
        m.put("D# Minor",31);
        m.put("E Minor",32);
        m.put("F Minor",33);
        m.put("F# Minor",34);
        m.put("G Minor",35);
        m.put("G# Minor",36);

        return m;
    }

    private static HashMap getKeyMapping()
    {
        HashMap m = new HashMap();
        boolean t = true;
        boolean f = false;

        boolean[] noKey =  {t,t,t,t,t,t,t,t,t,t,t,t};

        boolean[] a =  {t,f,t,f,t,t,f,t,f,t,f,t};
        boolean[] as = {t,t,f,t,f,t,t,f,t,f,t,f};
        boolean[] b =  {f,t,t,f,t,f,t,t,f,t,f,t};
        boolean[] c =  {t,f,t,t,f,t,f,t,t,f,t,f};
        boolean[] cs = {f,t,f,t,t,f,t,f,t,t,f,t};
        boolean[] d =  {t,f,t,f,t,t,f,t,f,t,t,f};
        boolean[] ds = {f,t,f,t,f,t,t,f,t,f,t,t};
        boolean[] e =  {t,f,t,f,t,f,t,t,f,t,f,t};
        boolean[] F =  {t,t,f,t,f,t,f,t,t,f,t,f};
        boolean[] fs = {f,t,t,f,t,f,t,f,t,t,f,t};
        boolean[] g =  {t,f,t,t,f,t,f,t,f,t,t,f};
        boolean[] gs = {f,t,f,t,t,f,t,f,t,f,t,t};

        boolean[] am =  {t,f,t,t,f,t,f,t,t,f,t,f};
        boolean[] asm = {f,t,f,t,t,f,t,f,t,t,f,t};
        boolean[] bm =  {t,f,t,f,t,t,f,t,f,t,t,f};
        boolean[] cm =  {f,t,f,t,f,t,t,f,t,f,t,t};
        boolean[] csm = {t,f,t,f,t,f,t,t,f,t,f,t};
        boolean[] dm =  {t,t,f,t,f,t,f,t,t,f,t,f};
        boolean[] dsm = {f,t,t,f,t,f,t,f,t,t,f,t};
        boolean[] em =  {t,f,t,t,f,t,f,t,f,t,t,f};
        boolean[] fm =  {f,t,f,t,t,f,t,f,t,f,t,t};
        boolean[] fsm = {t,f,t,f,t,t,f,t,f,t,f,t};
        boolean[] gm =  {t,t,f,t,f,t,t,f,t,f,t,f};
        boolean[] gsm = {f,t,t,f,t,f,t,t,f,t,f,t};

        m.put("No key",noKey);
        m.put("A Major",a);
        m.put("A# Major",as);
        m.put("B Major",b);
        m.put("C Major",c);
        m.put("C# Major",cs);
        m.put("D Major",d);
        m.put("D# Major",ds);
        m.put("E Major",e);
        m.put("F Major",F);
        m.put("F# Major",fs);
        m.put("G Major",g);
        m.put("G# Major",gs);

        m.put("A Minor",am);
        m.put("A# Minor",asm);
        m.put("B Minor",bm);
        m.put("C Minor",cm);
        m.put("C# Minor",csm);
        m.put("D Minor",dm);
        m.put("D# Minor",dsm);
        m.put("E Minor",em);
        m.put("F Minor",fm);
        m.put("F# Minor",fsm);
        m.put("G Minor",gm);
        m.put("G# Minor",gsm);

        return m;
    }

    private static HashMap getSoundReferences()
    {
        HashMap m = new HashMap();

        m.put(1,R.raw.k001);
        m.put(2,R.raw.k002);
        m.put(3,R.raw.k003);
        m.put(4,R.raw.k004);
        m.put(5,R.raw.k005);
        m.put(6,R.raw.k006);
        m.put(7,R.raw.k007);
        m.put(8,R.raw.k008);
        m.put(9,R.raw.k009);
        m.put(10,R.raw.k010);
        m.put(11,R.raw.k011);
        m.put(12,R.raw.k012);
        m.put(13,R.raw.k013);
        m.put(14,R.raw.k014);
        m.put(15,R.raw.k015);
        m.put(16,R.raw.k016);
        m.put(17,R.raw.k017);
        m.put(18,R.raw.k018);
        m.put(19,R.raw.k019);
        m.put(20,R.raw.k020);
        m.put(21,R.raw.k021);
        m.put(22,R.raw.k022);
        m.put(23,R.raw.k023);
        m.put(24,R.raw.k024);
        m.put(25,R.raw.k025);
        m.put(26,R.raw.k026);
        m.put(27,R.raw.k027);
        m.put(28,R.raw.k028c);
        m.put(29,R.raw.k029);
        m.put(30,R.raw.k030);
        m.put(31,R.raw.k031);
        m.put(32,R.raw.k032);
        m.put(33,R.raw.k033);
        m.put(34,R.raw.k034);
        m.put(35,R.raw.k035);
        m.put(36,R.raw.k036);
        m.put(37,R.raw.k037);
        m.put(38,R.raw.k038);
        m.put(39,R.raw.k039);
        m.put(40,R.raw.k040);
        m.put(41,R.raw.k041);
        m.put(42,R.raw.k042);
        m.put(43,R.raw.k043);
        m.put(44,R.raw.k044);
        m.put(45,R.raw.k045);
        m.put(46,R.raw.k046);
        m.put(47,R.raw.k047);
        m.put(48,R.raw.k048);
        m.put(49,R.raw.k049);
        m.put(50,R.raw.k050);
        m.put(51,R.raw.k051);
        m.put(52,R.raw.k052);
        m.put(53,R.raw.k053);
        m.put(54,R.raw.k054);
        m.put(55,R.raw.k055);
        m.put(56,R.raw.k056);
        m.put(57,R.raw.k057);
        m.put(58,R.raw.k058);
        m.put(59,R.raw.k059);
        m.put(60,R.raw.k060);
        m.put(61,R.raw.k061);
        m.put(62,R.raw.k062);
        m.put(63,R.raw.k063);
        m.put(64,R.raw.k064);

        return m;
    }

    private static HashMap getKeyNamesNumbers()
    {
        HashMap m = new HashMap();

        m.put("No key",0);
        m.put("A Major",1);
        m.put("A# Major",2);
        m.put("B Major",3);
        m.put("C Major",4);
        m.put("C# Major",5);
        m.put("D Major",6);
        m.put("D# Major",7);
        m.put("E Major",8);
        m.put("F Major",9);
        m.put("F# Major",10);
        m.put("G Major",11);
        m.put("G# Major",12);

        m.put("A Minor",13);
        m.put("A# Minor",14);
        m.put("B Minor",15);
        m.put("C Minor",16);
        m.put("C# Minor",17);
        m.put("D Minor",18);
        m.put("D# Minor",19);
        m.put("E Minor",20);
        m.put("F Minor",21);
        m.put("F# Minor",22);
        m.put("G Minor",23);
        m.put("G# Minor",24);

        return m;
    }

}
