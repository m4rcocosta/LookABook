package com.example.textrecognition;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class CheckBoxThread implements Runnable {

    private ListView listView;
    private CustomAdapter adapter;

    public CheckBoxThread(ListView ls,CustomAdapter ad ){
     listView=ls;
     adapter=ad;
    }

    final List<TitleModel> titles = new ArrayList<>();

    @Override
    public void run() {
        titles.add(new TitleModel(false, "Divina Commedia"));
        titles.add(new TitleModel(false, "Se questo è un uomo"));


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

    public void setTitles(){
        titles.add(new TitleModel(false, "mmm"));

    }

}
