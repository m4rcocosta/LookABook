package uni.mobile.mobileapp;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uni.mobile.mobileapp.guiAdapters.ListAdapter;
import uni.mobile.mobileapp.rest.House;
import uni.mobile.mobileapp.rest.MyResponse;
import uni.mobile.mobileapp.rest.RestLocalMethods;
import uni.mobile.mobileapp.rest.Room;


public class RoomFragment extends Fragment {

    private FloatingActionButton addRoomButton;
    private Spinner userHouses;
    private List<House> houses = null;
    private Map<String, Integer> houseIds = null;
    private String currentHouse = "Select an house!";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        RestLocalMethods.initRetrofit(getContext());

        // Custom choices
        List<String> choices = new ArrayList<>();
        choices.add("Select an house!");

        Call<MyResponse<House>> call = RestLocalMethods.getJsonPlaceHolderApi().getHouses(RestLocalMethods.getMyUserId());
        call.enqueue(new Callback<MyResponse<House>>() {
            @Override
            public void onResponse(Call<MyResponse<House>> call, Response<MyResponse<House>> response) {
                if(!response.isSuccessful()) return;
                houses = response.body().getData();

                // User Can't add a Room if he has not an house
                if (houses.isEmpty()) {
                    new MaterialAlertDialogBuilder(getContext())
                            .setTitle("No house found")
                            .setMessage("Please create one first.")
                            .setCancelable(false) // disallow cancel of AlertDialog on click of back button and outside touch
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.fragmentContainer, new HouseFragment());
                                    transaction.commit();
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }

                houseIds = new HashMap<String, Integer>();
                for(House house: houses){
                    choices.add(house.getName());
                    houseIds.put(house.getName(), house.getId());
                }
            }

            @Override
            public void onFailure(Call<MyResponse<House>> call, Throwable t) {
                Log.d("RRRR","Request Error :: " + t.getMessage() );
            }

        });
        // Create an ArrayAdapter with custom choices
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.item_spinner, choices){
            @Override
            public boolean isEnabled(int position){
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                return view;
            }
        };

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.item_spinner);

        addRoomButton = view.findViewById(R.id.addRoomButton);

        addRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_add_room, null);

                final EditText roomNameEditText = alertLayout.findViewById(R.id.roomName);
                userHouses = alertLayout.findViewById(R.id.userHouses);

                // Set the adapter to th spinner
                userHouses.setAdapter(adapter);

                userHouses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getContext(), userHouses.getSelectedItem() + " selected", Toast.LENGTH_SHORT).show();
                        currentHouse = userHouses.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Create new room")
                        .setMessage("Insert the room name")
                        .setView(alertLayout) // this is set the view from XML inside AlertDialog
                        .setCancelable(false) // disallow cancel of AlertDialog on click of back button and outside touch
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String roomName = roomNameEditText.getText().toString();
                                if (currentHouse.equals("Select an house!")) Toast.makeText(getContext(), "Please select an house!", Toast.LENGTH_SHORT).show();
                                else RestLocalMethods.createRoom(RestLocalMethods.getMyUserId(), houseIds.get(currentHouse), new Room(RestLocalMethods.getMyUserId(), roomName, null, null));
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

       /* Wall w = RestLocalMethods.createWall(1,6,1,new Wall("stranaHouse") );
        //t.setText(h.getName());
        User back=RestLocalMethods.createUser(new User("mardzxcfaio","","","asdxzcsad@asasdd.com","zzxczxz<x<zx"));
        //t.setText( back.getEmail() );*/


        Call<MyResponse<Room>> callAsync = RestLocalMethods.getJsonPlaceHolderApi().getAllRooms(RestLocalMethods.getMyUserId());
        callAsync.enqueue(new Callback<MyResponse<Room>>() {

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
