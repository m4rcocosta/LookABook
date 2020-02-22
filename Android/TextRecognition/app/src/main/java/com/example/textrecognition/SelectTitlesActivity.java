package com.example.textrecognition;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SelectTitlesActivity extends AppCompatActivity {
    private final List<TitleModel> titles = new ArrayList<>();
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_titles);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        ListView listView = findViewById(R.id.listview);
        //lv.setText(message);




        adapter = new CustomAdapter(this, titles);
        listView.setAdapter(adapter);


        String[] potentialTitles=message.split("\n");
        for(String t:potentialTitles){
            if(t.length() > 5)
            titles.add(new TitleModel( t ) );
        }
 /*       titles.add(new TitleModel(false, "Divina Commedia"));
        titles.add(new TitleModel(false, "Se questo è un uomo"));
*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TitleModel model = titles.get(i);

                if (model.isSelected())
                    model.setSelected(false);

                else
                    model.setSelected(true);

                titles.set(i, model);

                //now update adapter
                adapter.updateRecords(titles);
            }
        });

    }

    public void setTitles(String newTitle) {
        if (!titles.contains(newTitle))
            titles.add(new TitleModel(false, newTitle));
        adapter.updateRecords(titles);

    }

}
