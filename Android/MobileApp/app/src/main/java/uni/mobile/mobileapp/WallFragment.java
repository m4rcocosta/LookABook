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
import uni.mobile.mobileapp.rest.Wall;

public class WallFragment extends Fragment {

    private FloatingActionButton addWallButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wall, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        addWallButton = view.findViewById(R.id.addWallButton);
        addWallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_add_wall, null);
                final EditText wallNameEditText = alertLayout.findViewById(R.id.wallName);
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Create new wall")
                        .setMessage("Insert the wall name")
                        .setView(alertLayout) // this is set the view from XML inside AlertDialog
                        .setCancelable(false) // disallow cancel of AlertDialog on click of back button and outside touch
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String wallName = wallNameEditText.getText().toString();
                                Toast.makeText(getContext(), "Wall " + wallName + " created!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        RestLocalMethods.initRetrofit(this.getContext());
        
        Call<MyResponse<Wall>> callAsync = RestLocalMethods.getJsonPlaceHolderApi().getAllWalls(RestLocalMethods.getMyUserId());
        callAsync.enqueue(new Callback<MyResponse<Wall>>()
        {

            @Override
            public void onResponse(Call<MyResponse<Wall>> call, Response<MyResponse<Wall>> response)
            {
                if (response.isSuccessful())
                {

                    List<Wall> walls = response.body().getData();

                    ArrayList<String> names = new ArrayList<String>();
                    ArrayList<String> subNames = new ArrayList<String>();
                    for(Wall b: walls){
                        names.add( b.getName() ) ;
                        subNames.add("None");
                    }

                    ListView lView = view.findViewById(R.id.wallList);

                    ListAdapter lAdapter = new ListAdapter(getContext(), names, subNames, null,R.drawable.ic_wallicon2);

                    lView.setAdapter(lAdapter);
                    if(getContext()==null)  //too late now to print
                        return;
                    Toast.makeText(getContext(), "Found " + walls.size() +" walls", Toast.LENGTH_SHORT).show();
                    Log.d("WWWW",names.toString());
                    lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Toast.makeText(getContext(), names.get(i), Toast.LENGTH_SHORT).show();

                        }
                    });


                }
                else
                {
                    Log.d("WWWW","Request Error :: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<MyResponse<Wall>> call, Throwable t)
            {
                Log.d("WWWW","Request Error :: " + t.getMessage() );
            }
        });
    }
}
