package uni.mobile.mobileapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import uni.mobile.mobileapp.guiAdapters.ListAdapter;


public class BookFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book, container, false);


    }
/*
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {



        ListView lView = (ListView) view.findViewById(R.id.androidList);

        ListAdapter lAdapter = new ListAdapter(this, titles, authors, null,R.drawable.ic_book_red);

        lView.setAdapter(lAdapter);

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(this, titles[i], Toast.LENGTH_SHORT).show();

            }
        });

    }*/
}
