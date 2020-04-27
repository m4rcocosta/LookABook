package uni.mobile.mobileapp;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uni.mobile.mobileapp.guiAdapters.ListAdapter;
import uni.mobile.mobileapp.rest.Book;
import uni.mobile.mobileapp.rest.House;
import uni.mobile.mobileapp.rest.MyResponse;
import uni.mobile.mobileapp.rest.RestLocalMethods;
import uni.mobile.mobileapp.rest.Room;


public class RoomFragment extends Fragment {

    private FloatingActionButton addRoomButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        addRoomButton = view.findViewById(R.id.addRoomButton);
        addRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_add_room, null);
                final EditText roomNameEditText = alertLayout.findViewById(R.id.roomName);
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Create new room")
                        .setMessage("Insert the room name")
                        .setView(alertLayout) // this is set the view from XML inside AlertDialog
                        .setCancelable(false) // disallow cancel of AlertDialog on click of back button and outside touch
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String roomName = roomNameEditText.getText().toString();
                                //RestLocalMethods.createHouse(view,RestLocalMethods.getMyUserId(),new Room(roomName));
                                //Toast.makeText(getContext(), "Room " + roomName + " created!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        RestLocalMethods.initRetrofit(this.getContext());

       /* Wall w = RestLocalMethods.createWall(1,6,1,new Wall("stranaHouse") );
        //t.setText(h.getName());
        User back=RestLocalMethods.createUser(new User("mardzxcfaio","","","asdxzcsad@asasdd.com","zzxczxz<x<zx"));
        //t.setText( back.getEmail() );*/


        Call<MyResponse<Room>> callAsync = RestLocalMethods.getJsonPlaceHolderApi().getAllRooms(RestLocalMethods.getMyUserId());
        callAsync.enqueue(new Callback<MyResponse<Room>>()
        {

            @Override
            public void onResponse(Call<MyResponse<Room>> call, Response<MyResponse<Room>> response)
            {
                if (response.isSuccessful())
                {

                    List<Room> rooms = response.body().getData();

                    ArrayList<String> names = new ArrayList<String>();
                    ArrayList<String> subNames = new ArrayList<String>();
                    for(Room b: rooms){
                        names.add( b.getName() ) ;
                        subNames.add("None");
                    }

                    ListView lView = view.findViewById(R.id.roomList);

                    ListAdapter lAdapter = new ListAdapter(getContext(), names, subNames, null,R.drawable.ic_roomicon);

                    lView.setAdapter(lAdapter);
                    if(getContext()==null)  //too late now to print
                        return;
                    Toast.makeText(getContext(), "Found " + rooms.size() +" rooms", Toast.LENGTH_SHORT).show();
                    Log.d("RRRR",names.toString());
                    lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Toast.makeText(getContext(), names.get(i), Toast.LENGTH_SHORT).show();

                        }
                    });


                }
                else
                {
                    Log.d("RRRR","Request Error :: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<MyResponse<Room>> call, Throwable t)
            {
                Log.d("RRRR","Request Error :: " + t.getMessage() );
            }
        });
    }
}
