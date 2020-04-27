package uni.mobile.mobileapp;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uni.mobile.mobileapp.guiAdapters.ListAdapter;
import uni.mobile.mobileapp.rest.MyResponse;
import uni.mobile.mobileapp.rest.RestLocalMethods;
import uni.mobile.mobileapp.rest.Shelf;

public class ShelfFragment extends Fragment {

    private FloatingActionButton addShelfButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shelf, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        addShelfButton = view.findViewById(R.id.addShelfButton);
        addShelfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_add_shelf, null);
                final EditText shelfNameEditText = alertLayout.findViewById(R.id.shelfName);
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Create new shelf")
                        .setMessage("Insert the shelf name")
                        .setView(alertLayout) // this is set the view from XML inside AlertDialog
                        .setCancelable(false) // disallow cancel of AlertDialog on click of back button and outside touch
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String shelfName = shelfNameEditText.getText().toString();
                                Toast.makeText(getContext(), "Shelf " + shelfName + " created!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        RestLocalMethods.initRetrofit(this.getContext());
        Call<MyResponse<Shelf>> callAsync = RestLocalMethods.getJsonPlaceHolderApi().getAllShelves(RestLocalMethods.getMyUserId());
        callAsync.enqueue(new Callback<MyResponse<Shelf>>()
        {

            @Override
            public void onResponse(Call<MyResponse<Shelf>> call, Response<MyResponse<Shelf>> response)
            {
                if (response.isSuccessful())
                {

                    List<Shelf> shelves = response.body().getData();

                    ArrayList<String> names = new ArrayList<String>();
                    ArrayList<String> subNames = new ArrayList<String>();
                    for(Shelf b: shelves){
                        names.add( b.getName() ) ;
                        subNames.add("None");
                    }

                    ListView lView = view.findViewById(R.id.shelfList);

                    ListAdapter lAdapter = new ListAdapter(getContext(), names, subNames, null,R.drawable.ic_shelficon);

                    lView.setAdapter(lAdapter);
                    if(getContext()==null)  //too late now to print
                        return;
                    Toast.makeText(getContext(), "Found " + shelves.size() +" shelves", Toast.LENGTH_SHORT).show();
                    Log.d("SSSS",names.toString());
                    lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Toast.makeText(getContext(), names.get(i), Toast.LENGTH_SHORT).show();

                        }
                    });


                }
                else
                {
                    Log.d("SSSS","Request Error :: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<MyResponse<Shelf>> call, Throwable t)
            {
                Log.d("SSSS","Request Error :: " + t.getMessage() );
            }
        });
    }
}
