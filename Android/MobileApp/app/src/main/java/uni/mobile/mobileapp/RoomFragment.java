package uni.mobile.mobileapp;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;
import java.util.List;

import uni.mobile.mobileapp.rest.House;
import uni.mobile.mobileapp.rest.RestLocalMethods;
import uni.mobile.mobileapp.rest.Room;


public class RoomFragment extends Fragment {

    private FloatingActionButton addRoomButton;
    private Spinner userHouses;

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
                userHouses = alertLayout.findViewById(R.id.userHouses);
                // Custom choices
                List<String> choices = new ArrayList<>();
                choices.add("Select an house!");
                choices.add("A");
                choices.add("B");
                choices.add("C");
                choices.add("D");
                choices.add("E");

                // Create an ArrayAdapter with custom choices
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.item_spinner, choices){
                    @Override
                    public boolean isEnabled(int position){
                        if(position == 0) {
                            return false;
                        }
                        else {
                            return true;
                        }
                    }
                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
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

                // Set the adapter to th spinner
                userHouses.setAdapter(adapter);

                userHouses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getContext(), userHouses.getSelectedItem() + " selected", Toast.LENGTH_SHORT).show();
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
                                //RestLocalMethods.createHouse(view,RestLocalMethods.getMyUserId(),new Room(roomName));
                                //Toast.makeText(getContext(), "Room " + roomName + " created!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
    }
}
