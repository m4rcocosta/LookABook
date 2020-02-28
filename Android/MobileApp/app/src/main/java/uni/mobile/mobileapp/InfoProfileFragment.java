package uni.mobile.mobileapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class InfoProfileFragment extends Fragment implements View.OnClickListener {

    private DatabaseReference databaseReference;
    private TextView nameProfileTextView, surnameProfileTextView, phoneNumberProfileTextView, emailProfileTextView;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private MaterialButton editNameButton, editSurnameButton, editPhoneNumberButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        editNameButton = view.findViewById(R.id.editNameProfileButton);
        editSurnameButton = view.findViewById(R.id.editSurnameProfileButton);
        editPhoneNumberButton = view.findViewById(R.id.editPhoneNumberProfileButton);

        editNameButton.setOnClickListener(this);
        editSurnameButton.setOnClickListener(this);
        editPhoneNumberButton.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        nameProfileTextView = view.findViewById(R.id.nameProfileTextView);
        surnameProfileTextView = view.findViewById(R.id.surnameProfileTextView);
        phoneNumberProfileTextView = view.findViewById(R.id.phoneNumberProfileTextView);
        emailProfileTextView = view.findViewById(R.id.emailProfileTextView);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        if (firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(getActivity(), SignInActivity.class));
            getActivity().finish();
        }

        final FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                Userinformation userProfile = dataSnapshot.getValue(Userinformation.class);
                nameProfileTextView.setText(userProfile.getUserName());
                surnameProfileTextView.setText(userProfile.getUserSurname());
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
                        String name = setUserName.getText().toString();
                        String surname = surnameProfileTextView.getText().toString();
                        String phoneNumber =  phoneNumberProfileTextView.getText().toString();
                        Userinformation userinformation = new Userinformation(name,surname, phoneNumber);
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        databaseReference.child(user.getUid()).setValue(userinformation);
                        setUserName.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void editSurname() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_edit_surname, null);
        final EditText setUserSurname = alertLayout.findViewById(R.id.setUserSurname);
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Edit surname")
                .setMessage("Insert the new surname")
                .setView(alertLayout) // this is set the view from XML inside AlertDialog
                .setCancelable(false) // disallow cancel of AlertDialog on click of back button and outside touch
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = nameProfileTextView.getText().toString();
                        String surname = setUserSurname.getText().toString();
                        String phoneNumber =  phoneNumberProfileTextView.getText().toString();
                        Userinformation userinformation = new Userinformation(name,surname, phoneNumber);
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        databaseReference.child(user.getUid()).setValue(userinformation);
                        setUserSurname.onEditorAction(EditorInfo.IME_ACTION_DONE);
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
                        String name = nameProfileTextView.getText().toString();
                        String surname = surnameProfileTextView.getText().toString();
                        String phoneNumber =  setUserPhoneNumber.getText().toString();
                        Userinformation userinformation = new Userinformation(name,surname, phoneNumber);
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        databaseReference.child(user.getUid()).setValue(userinformation);
                        setUserPhoneNumber.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editNameProfileButton:
                editName();
                break;
            case R.id.editSurnameProfileButton:
                editSurname();
                break;
            case R.id.editPhoneNumberProfileButton:
                editPhoneNumber();
                break;
        }
    }
}
