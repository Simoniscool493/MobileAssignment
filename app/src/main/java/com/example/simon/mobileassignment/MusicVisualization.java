package com.example.simon.mobileassignment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Simon on 12/11/2016.
 * Class for the MusicVisualisation View, which plays animations in time with the music
 */

public class MusicVisualization extends View
{
    //Start is the rectangle that covers the screen
    Rect start;
    //vis is the rectangle that visualises a note played
    Rect vis;

    Paint black;

    //Integer that stores the most recently pressed note, so the visualisation can animate it
    int lastPressed = -1;
    //boolean that whether the most recently pressed note was a chord, so the visualisation knows whether to animate three notes or one
    boolean chordPressed = false;

    //Paint array that stores the colors of the notes to visualize
    Paint[] colors = new Paint[12];

    //Boolean that stores whether the animation has started yet, its activated as soon as the initial rectangle is drawn to cover the screen
    boolean started;

    public MusicVisualization(Context context)
    {
        super(context);
        init(context);
    }

    public MusicVisualization(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public MusicVisualization(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context);
        setWillNotDraw(false);
    }

    private void init(Context context)
    {
        start = new Rect();
        vis = new Rect();

        black = new Paint();
        black.setColor(Color.BLACK);
        black.setStyle(Paint.Style.FILL);

        initializeColors();
    }

    protected void drawBackground(Canvas canvas)
    {
        start.set(0,0,canvas.getWidth(),canvas.getHeight());
        canvas.drawRect(start,black);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        drawBackground(canvas);

        //draws a note's visualisation if the key pressed is within the bounds of the screen, and the animation has started
        if(started&&lastPressed<12&&lastPressed>-1)
        {
            //get 1/12 the width of the screen, to decide how far across the screen to draw the animation
            int incWidth = canvas.getWidth()/12;

            //Draw the animation
            vis.set(incWidth*lastPressed,0,(incWidth*lastPressed)+incWidth,canvas.getHeight());
            canvas.drawRect(vis,colors[lastPressed]);

            //If it's a chord, draw one or two more amimations, depending on whether they're within the bounds of the screen
            if(chordPressed)
            {
                if(lastPressed<10)//draw second key of chord
                {
                    vis.set(incWidth * (lastPressed + 2), 0, (incWidth * (lastPressed + 2)) + incWidth, canvas.getHeight());
                    canvas.drawRect(vis, colors[(lastPressed + 2) % 12]);

                    if(lastPressed<8)//draw third key of chord
                    {
                        vis.set(incWidth * (lastPressed + 4), 0, (incWidth * (lastPressed + 4)) + incWidth, canvas.getHeight());
                        canvas.drawRect(vis, colors[(lastPressed + 4) % 12]);
                    }
                }
            }
        }

        chordPressed = false;
    }

    //create 12 colors on the spectrum of red to yellow, to animate the notes
    void initializeColors()
    {
        for(int i=0;i<colors.length;i++)
        {
            colors[i] = new Paint();
            int c = Color.rgb(255, i*20, 0);

            colors[i].setColor(c);
            colors[i].setStyle(Paint.Style.FILL);
        }
    }

    //There are two methods for the screen that's playing the music to visualize it, because one screen requires the use of runOnUiThread
    //to prevent an exception being raised
    //to prevent an exception being raised
    void setLastPressed(int i,boolean c)
    {
        chordPressed = c;
        lastPressed = i;
        invalidate();
    }

    void setLastPressed(int i,boolean c,Activity parent)
    {
        chordPressed = c;
        lastPressed = i;
        parent.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                invalidate();
            }
        });
    }
}
