package uni.mobile.mobileapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uni.mobile.mobileapp.guiAdapters.ListAdapter;
import uni.mobile.mobileapp.rest.House;
import uni.mobile.mobileapp.rest.JsonPlaceHolderApi;
import uni.mobile.mobileapp.rest.MyResponse;
import uni.mobile.mobileapp.rest.RestLocalMethods;
import uni.mobile.mobileapp.rest.callbacks.HouseCallback;


public class HouseFragment extends Fragment {

    private FloatingActionButton addHouseButton;
    private Context context;
    private FragmentActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_house, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        context = this.getContext();
        activity = this.getActivity();

        RestLocalMethods.initRetrofit(context, RestLocalMethods.getUserToken());
        getUserHouses(view, context);

        addHouseButton = view.findViewById(R.id.addHouseButton);
        addHouseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_add_house, null);
                final EditText houseNameEditText = alertLayout.findViewById(R.id.houseName);
                new MaterialAlertDialogBuilder(context)
                        .setTitle("Create new house")
                        .setMessage("Insert the house name")
                        .setView(alertLayout) // this is set the view from XML inside AlertDialog
                        .setCancelable(false) // disallow cancel of AlertDialog on click of back button and outside touch
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String houseName = houseNameEditText.getText().toString();
                                RestLocalMethods.createHouse(RestLocalMethods.getMyUserId(), new House(houseName,null,null,false), new HouseCallback() {
                                    @Override
                                    public void onSuccess(@NonNull List<House> booksRes) {
                                        getUserHouses(view, context);
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });







    }

    private void getUserHouses(View view, Context context){

        Call<MyResponse<House>> callAsync = RestLocalMethods.getJsonPlaceHolderApi().getHouses(RestLocalMethods.getMyUserId());
        callAsync.enqueue(new Callback<MyResponse<House>>() {

            @Override
            public void onResponse(Call<MyResponse<House>> call, Response<MyResponse<House>> response) {
                if (response.isSuccessful()) {

                    List<House> houses = response.body().getData();

                    ArrayList<String> names = new ArrayList<String>();
                    //ArrayList<String> authors = new ArrayList<String>();
                    for(House h: houses){
                        names.add(h.getName());
                        //authors.add(b.getAuthors());
                    }

                    ListView lView = view.findViewById(R.id.houseList);

                    ListAdapter lAdapter = new ListAdapter(context, names, null, null,R.drawable.ic_houseicon);

                    lView.setAdapter(lAdapter);
                    if(context == null) return;
                    Toast.makeText(context, "Found " + houses.size() +" Houses", Toast.LENGTH_SHORT).show();
                    Log.d("HOUSE", names.toString());
                    lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                            if(context == null) return;
                            LayoutInflater inflater = getLayoutInflater();
                            View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_edit_house, null);
                            final EditText houseNameEditText = alertLayout.findViewById(R.id.houseNameEdit);
                            final String oldName = names.get(i);
                            houseNameEditText.setText(oldName);
                            final Button deleteHouseButton = alertLayout.findViewById(R.id.deleteHouseButton);
                            deleteHouseButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    RestLocalMethods.deleteHouse(RestLocalMethods.getMyUserId(), houses.get(i).getId(), new HouseCallback() {
                                        @Override
                                        public void onSuccess(@NonNull List<House> booksRes) {
                                            getUserHouses(view, context);
                                        }
                                    });
                                }
                            });
                            new MaterialAlertDialogBuilder(context)
                                    .setTitle("Edit house")
                                    .setMessage("Change house name or delete it")
                                    .setView(alertLayout) // this is set the view from XML inside AlertDialog
                                    .setCancelable(false) // disallow cancel of AlertDialog on click of back button and outside touch
                                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            RestLocalMethods.patchHouse(RestLocalMethods.getMyUserId(), houses.get(i).getId(), new House(houseNameEditText.getText().toString(),null,null,false), new HouseCallback() {
                                                @Override
                                                public void onSuccess(@NonNull List<House> booksRes) {
                                                    getUserHouses(view, context);
                                                }
                                            });
                                        }
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .show();
                        }
                    });


                }
                else {
                    Log.d("HOUSE","Request Error :: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<MyResponse<House>> call, Throwable t) {
                Log.d("HOUSE","Request Error :: " + t.getMessage() );
            }
        });
    }
}
