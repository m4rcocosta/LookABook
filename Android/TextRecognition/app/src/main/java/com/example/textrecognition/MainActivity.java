package com.example.textrecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


public class MainActivity extends Activity {


    public static final String EXTRA_MESSAGE = "lookABook";
    private ListView listView = null;
    private Boolean isStoppedCamera = false;
    private MyCamera c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //---------------------------------------------------------

        listView  = (ListView) findViewById(R.id.listview); //view of list of titles with checkboxes

        //CheckBoxThread checkBoxThread= new CheckBoxThread(listView,this);
        //checkBoxThread.start();
        //checkBoxThread.setTitles("New");
        //c = new MyCamera(getApplicationContext(),this,checkBoxThread);

        c = new MyCamera(getApplicationContext(),this,null);    //class to handle camera recognition stuff
        c.startCameraSource();

    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Toast.makeText(this, "onRestore", Toast.LENGTH_LONG).show();

    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Toast.makeText(this, "onSave", Toast.LENGTH_LONG).show();
    //Todo
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }



    public void onClickBtn(View v){ //Todo toglle on/off the camera (not working)
        Toast.makeText(this, "Camera "+ (isStoppedCamera?"ON":"OFF"), Toast.LENGTH_LONG).show();
        isStoppedCamera = !isStoppedCamera;
        if(isStoppedCamera) {
            c.stopCamera();
        }
        else{
            c.startCameraSource();
        }

        }

    public void onClickSfc(View v){ //Tap on the camera happened
        if(c.getReady()) {
            Toast.makeText(this, "Camera picture taken", Toast.LENGTH_LONG).show();
            c.snap();
        }
    }

    // onClickSfg() -> c.snap() -> ... -> onSnapped()
    public void onSnapped(String msgToPass, String filename){   //Camera took text from the tapped image
        //Intent intent = new Intent(this, SelectTitlesActivity.class);
        Intent intent = new Intent(this, PhotoDecisionActivity.class);



        intent.putExtra(EXTRA_MESSAGE, msgToPass);
        intent.putExtra(EXTRA_MESSAGE+"bitmapFilename", filename);
        startActivity(intent);

    }







}