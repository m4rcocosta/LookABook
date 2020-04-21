package uni.mobile.mobileapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class InfoProfileFragment extends Fragment {

    private DatabaseReference databaseReference;
    private TextView nameProfileTextView, phoneNumberProfileTextView, emailProfileTextView;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private Button editNameButton, editPhoneNumberButton;
    private FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        editNameButton = view.findViewById(R.id.editNameProfileButton);
        editPhoneNumberButton = view.findViewById(R.id.editPhoneNumberProfileButton);

        editNameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editName();
            }
        });
        editPhoneNumberButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editPhoneNumber();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference();

        nameProfileTextView = view.findViewById(R.id.nameProfileTextView);
        phoneNumberProfileTextView = view.findViewById(R.id.phoneNumberProfileTextView);
        emailProfileTextView = view.findViewById(R.id.emailProfileTextView);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        user = firebaseAuth.getCurrentUser();

        if (user == null){
            startActivity(new Intent(getActivity(), SignInActivity.class));
            getActivity().finish();
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                Userinformation userProfile = dataSnapshot.getValue(Userinformation.class);
                nameProfileTextView.setText(user.getDisplayName());
                phoneNumberProfileTextView.setText(userProfile.getUserPhoneNumber());
                emailProfileTextView.setText(user.getEmail());
            }
            @Override
            public void onCancelled( DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void editName() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_edit_name, null);
        final EditText setUserName = alertLayout.findViewById(R.id.setUserName);
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Edit name")
                .setMessage("Insert the new name")
                .setView(alertLayout) // this is set the view from XML inside AlertDialog
                .setCancelable(false) // disallow cancel of AlertDialog on click of back button and outside touch
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = setUserName.getText().toString();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(newName).build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            nameProfileTextView.setText(user.getDisplayName());
                                            Toast.makeText(getContext(), "Name updated successfully!", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(getContext(), "Error while updating name.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        setUserName.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void editPhoneNumber() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_edit_phone_number, null);
        final EditText setUserPhoneNumber = alertLayout.findViewById(R.id.setUserPhoneNumber);
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Edit phone number")
                .setMessage("Insert the new phone number")
                .setView(alertLayout) // this is set the view from XML inside AlertDialog
                .setCancelable(false) // disallow cancel of AlertDialog on click of back button and outside touch
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String phoneNumber =  setUserPhoneNumber.getText().toString();
                        Userinformation userinformation = new Userinformation(phoneNumber);
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        databaseReference.child(user.getUid()).setValue(userinformation);
                        setUserPhoneNumber.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
