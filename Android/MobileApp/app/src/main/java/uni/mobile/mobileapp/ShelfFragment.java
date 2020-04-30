package uni.mobile.mobileapp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import uni.mobile.mobileapp.rest.Shelf;
import uni.mobile.mobileapp.rest.Wall;
import uni.mobile.mobileapp.rest.callbacks.ShelfCallback;

public class ShelfFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton addShelfButton;
    private Spinner userWalls;
    private List<Wall> walls = null;
    private Map<String, Wall> wallDic = null;
    private String currentWall = "Select a wall!";
    private Context context;
    private FragmentActivity activity;

    public ShelfFragment(BottomNavigationView bottomNavigationView) {
        this.bottomNavigationView = bottomNavigationView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shelf, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        context = this.getContext();
        activity = this.getActivity();

        RestLocalMethods.initRetrofit(context);

        // Load user shelves
        getUserShelves(view);

        // Custom choices
        List<String> choices = new ArrayList<>();
        choices.add("Select a wall!");

        Call<MyResponse<Wall>> call = RestLocalMethods.getJsonPlaceHolderApi().getAllWalls(RestLocalMethods.getMyUserId());
        call.enqueue(new Callback<MyResponse<Wall>>() {
            @Override
            public void onResponse(Call<MyResponse<Wall>> call, Response<MyResponse<Wall>> response) {
                if(!response.isSuccessful()) return;
                walls = response.body().getData();

                wallDic = new HashMap<String, Wall>();
                for(Wall wall: walls){
                    choices.add(wall.getName());
                    wallDic.put(wall.getName(), wall);
                }
            }

            @Override
            public void onFailure(Call<MyResponse<Wall>> call, Throwable t) {
                Log.d("WALL","Request Error :: " + t.getMessage() );
            }

        });

        // Create an ArrayAdapter with custom choices
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.item_spinner, choices){
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

        addShelfButton = view.findViewById(R.id.addShelfButton);
        addShelfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // User Can't add a Shelf if he has not a Wall
                if (walls.isEmpty()) {
                    new MaterialAlertDialogBuilder(context)
                            .setTitle("No wall found")
                            .setMessage("Please create one first.")
                            .setCancelable(false) // disallow cancel of AlertDialog on click of back button and outside touch
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    bottomNavigationView.setSelectedItemId(R.id.navigation_wall);
                                    FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.fragmentContainer, new WallFragment(bottomNavigationView));
                                    transaction.commit();
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
                else {
                    LayoutInflater inflater = getLayoutInflater();
                    View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_add_shelf, null);
                    final EditText shelfNameEditText = alertLayout.findViewById(R.id.shelfName);
                    userWalls = alertLayout.findViewById(R.id.userWalls);

                    // Set the adapter to th spinner
                    userWalls.setAdapter(adapter);

                    userWalls.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            currentWall = userWalls.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    new MaterialAlertDialogBuilder(context)
                            .setTitle("Create new shelf")
                            .setMessage("Insert the shelf name")
                            .setView(alertLayout) // this is set the view from XML inside AlertDialog
                            .setCancelable(false) // disallow cancel of AlertDialog on click of back button and outside touch
                            .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String shelfName = shelfNameEditText.getText().toString();
                                    if (currentWall.equals("Select a wall!"))
                                        Toast.makeText(getContext(), "Please select a wall!", Toast.LENGTH_SHORT).show();
                                    else {
                                        Wall selectedWall = wallDic.get(currentWall);
                                        RestLocalMethods.createShelf(RestLocalMethods.getMyUserId(), selectedWall.getHouseId(), selectedWall.getRoomId(), selectedWall.getId(), new Shelf(shelfName, selectedWall.getId(), selectedWall.getRoomId(), selectedWall.getHouseId()), new ShelfCallback() {
                                            @Override
                                            public void onSuccess(@NonNull List<Shelf> booksRes) {
                                                getUserShelves(view);
                                            }
                                        });
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
            }
        });
    }

    private void getUserShelves(View view) {
        Call<MyResponse<Shelf>> callAsync = RestLocalMethods.getJsonPlaceHolderApi().getAllShelves(RestLocalMethods.getMyUserId());
        callAsync.enqueue(new Callback<MyResponse<Shelf>>() {

            @Override
            public void onResponse(Call<MyResponse<Shelf>> call, Response<MyResponse<Shelf>> response) {
                if (response.isSuccessful()) {

                    List<Shelf> shelves = response.body().getData();

                    ArrayList<String> names = new ArrayList<String>();
                    ArrayList<String> subNames = new ArrayList<String>();
                    for(Shelf s: shelves){
                        names.add(s.getName()) ;
                        subNames.add("None");
                    }

                    ListView lView = view.findViewById(R.id.shelfList);

                    ListAdapter lAdapter = new ListAdapter(context, names, subNames, null,R.drawable.ic_shelficon);

                    lView.setAdapter(lAdapter);
                    if(getContext()==null)  //too late now to print
                        return;
                    Toast.makeText(getContext(), "Found " + shelves.size() +" shelves", Toast.LENGTH_SHORT).show();
                    Log.d("SHELF",names.toString());
                    lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                            LayoutInflater inflater = getLayoutInflater();
                            View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_edit_shelf, null);
                            final EditText shelfNameEditText = alertLayout.findViewById(R.id.shelfNameEdit);
                            final String oldName = names.get(i);
                            shelfNameEditText.setText(oldName);
                            final Button deleteShelfButton = alertLayout.findViewById(R.id.deleteShelfButton);
                            deleteShelfButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    RestLocalMethods.deleteShelf(RestLocalMethods.getMyUserId(), shelves.get(i).getHouseId(), shelves.get(i).getRoomId(), shelves.get(i).getWallId(), shelves.get(i).getId(), new ShelfCallback() {
                                        @Override
                                        public void onSuccess(@NonNull List<Shelf> booksRes) {
                                            getUserShelves(view);
                                        }
                                    });
                                }
                            });
                            new MaterialAlertDialogBuilder(context)
                                    .setTitle("Edit shelf")
                                    .setMessage("Change shelf name or delete it")
                                    .setView(alertLayout) // this is set the view from XML inside AlertDialog
                                    .setCancelable(false) // disallow cancel of AlertDialog on click of back button and outside touch
                                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            RestLocalMethods.patchShelf(RestLocalMethods.getMyUserId(), shelves.get(i).getHouseId(), shelves.get(i).getRoomId(), shelves.get(i).getWallId(), shelves.get(i).getId(), new Shelf(shelfNameEditText.getText().toString(), shelves.get(i).getWallId(), shelves.get(i).getRoomId(), shelves.get(i).getHouseId()), new ShelfCallback() {
                                                @Override
                                                public void onSuccess(@NonNull List<Shelf> booksRes) {
                                                    getUserShelves(view);
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
                    Log.d("SHELF","Request Error :: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<MyResponse<Shelf>> call, Throwable t) {
                Log.d("SHELF","Request Error :: " + t.getMessage() );
            }
        });
    }
}
