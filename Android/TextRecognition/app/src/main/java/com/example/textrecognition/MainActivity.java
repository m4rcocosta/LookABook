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


    ListView listView = null;
    Boolean isStoppedCamera = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //---------------------------------------------------------

        listView  = (ListView) findViewById(R.id.listview);



        final CustomAdapter adapter = new CustomAdapter(this, titles);
        listView.setAdapter(adapter);


        CheckBoxThread checkBoxThread= new CheckBoxThread(listView,adapter);

        checkBoxThread.run();

        MyCamera c = new MyCamera(getApplicationContext(),this);
        c.startCameraSource();

    }



    public void onClickBtn(View v){
        Toast.makeText(this, "Camera "+ (isStoppedCamera?"ON":"OFF"), Toast.LENGTH_LONG).show();
        isStoppedCamera = !isStoppedCamera;
    }




}