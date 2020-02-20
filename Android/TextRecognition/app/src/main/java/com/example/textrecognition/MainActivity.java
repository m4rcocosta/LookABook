package com.example.textrecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {


    private ListView listView = null;
    private Boolean isStoppedCamera = false;
    private MyCamera c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //---------------------------------------------------------

        listView  = (ListView) findViewById(R.id.listview);





        CheckBoxThread checkBoxThread= new CheckBoxThread(listView,this);
        checkBoxThread.start();

        checkBoxThread.setTitles("New");

        c = new MyCamera(getApplicationContext(),this,checkBoxThread);
        c.startCameraSource();

    }



    public void onClickBtn(View v){
        Toast.makeText(this, "Camera "+ (isStoppedCamera?"ON":"OFF"), Toast.LENGTH_LONG).show();
        isStoppedCamera = !isStoppedCamera;
        c.stopCamera();
    }

    public void onClickSfc(View v){
        Toast.makeText(this, "Camera picture taken", Toast.LENGTH_LONG).show();
        c.snap();
    }



}