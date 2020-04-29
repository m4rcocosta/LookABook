package uni.mobile.mobileapp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import uni.mobile.mobileapp.rest.MyResponse;
import uni.mobile.mobileapp.rest.RestLocalMethods;
import uni.mobile.mobileapp.rest.Room;
import uni.mobile.mobileapp.rest.Wall;

public class WallFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton addWallButton;
    private Spinner userRooms;
    private List<Room> rooms = null;
    private Map<String, Room> roomDic = null;
    private String currentRoom = "Select a room!";
    private Context context;
    private FragmentActivity activity;

    public WallFragment(BottomNavigationView bottomNavigationView) {
        this.bottomNavigationView = bottomNavigationView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wall, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        context = this.getContext();
        activity = this.getActivity();

        RestLocalMethods.initRetrofit(context);

        // Load user walls
        getUserWalls(view);

        // Custom choices
        List<String> choices = new ArrayList<>();
        choices.add("Select a room!");

        Call<MyResponse<Room>> call = RestLocalMethods.getJsonPlaceHolderApi().getAllRooms(RestLocalMethods.getMyUserId());
        call.enqueue(new Callback<MyResponse<Room>>() {
            @Override
            public void onResponse(Call<MyResponse<Room>> call, Response<MyResponse<Room>> response) {
                if(!response.isSuccessful()) return;
                rooms = response.body().getData();

                roomDic = new HashMap<String, Room>();
                for(Room room: rooms){
                    choices.add(room.getName());
                    roomDic.put(room.getName(), room);
                }
            }

            @Override
            public void onFailure(Call<MyResponse<Room>> call, Throwable t) {
                Log.d("ROOM","Request Error :: " + t.getMessage() );
            }

        });

        // Create an ArrayAdapter with custom choices
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.item_spinner, choices){
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

        addWallButton = view.findViewById(R.id.addWallButton);
        addWallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // User Can't add a Wall if he has not a Room
                if (rooms.isEmpty()) {
                    new MaterialAlertDialogBuilder(context)
                            .setTitle("No room found")
                            .setMessage("Please create one first.")
                            .setCancelable(false) // disallow cancel of AlertDialog on click of back button and outside touch
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    bottomNavigationView.setSelectedItemId(R.id.navigation_room);
                                    FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.fragmentContainer, new RoomFragment(bottomNavigationView));
                                    transaction.commit();
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
                else{
                    LayoutInflater inflater = getLayoutInflater();
                    View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_add_wall, null);
                    final EditText wallNameEditText = alertLayout.findViewById(R.id.wallName);
                    userRooms = alertLayout.findViewById(R.id.userRooms);

                    // Set the adapter to th spinner
                    userRooms.setAdapter(adapter);

                    userRooms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            currentRoom = userRooms.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    new MaterialAlertDialogBuilder(context)
                            .setTitle("Create new wall")
                            .setMessage("Insert the wall name")
                            .setView(alertLayout) // this is set the view from XML inside AlertDialog
                            .setCancelable(false) // disallow cancel of AlertDialog on click of back button and outside touch
                            .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String wallName = wallNameEditText.getText().toString();
                                    if (currentRoom.equals("Select a room!"))
                                        Toast.makeText(getContext(), "Please select a room!", Toast.LENGTH_SHORT).show();
                                    else {
                                        Room selectedRoom = roomDic.get(currentRoom);
                                        RestLocalMethods.createWall(RestLocalMethods.getMyUserId(), selectedRoom.getHouseId(), selectedRoom.getId(), new Wall(wallName, selectedRoom.getId(), selectedRoom.getHouseId()));
                                        // Reload fragment in order to see new added wall
                                        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.fragmentContainer, new WallFragment(bottomNavigationView));
                                        transaction.commit();
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
            }
        });
    }

    private void getUserWalls(View view) {
        Call<MyResponse<Wall>> callAsync = RestLocalMethods.getJsonPlaceHolderApi().getAllWalls(RestLocalMethods.getMyUserId());
        callAsync.enqueue(new Callback<MyResponse<Wall>>() {

            @Override
            public void onResponse(Call<MyResponse<Wall>> call, Response<MyResponse<Wall>> response) {
                if (response.isSuccessful()) {

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
                    Log.d("WALL",names.toString());
                    lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Toast.makeText(getContext(), names.get(i), Toast.LENGTH_SHORT).show();

                        }
                    });


                }
                else {
                    Log.d("WALL","Request Error :: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<MyResponse<Wall>> call, Throwable t) {
                Log.d("WALL","Request Error :: " + t.getMessage() );
            }
        });
    }
}
