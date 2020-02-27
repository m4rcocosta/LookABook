package uni.mobile.mobileapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private DatabaseReference databaseReference;
    private TextView nameProfileTextView, surnameProfileTextView, phoneNumberProfileTextView, emailProfileTextView;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private ImageView profilePicImageView;
    private FirebaseStorage firebaseStorage;
    private MaterialButton buttonEditName, buttonEditSurname, buttonEditPhoneNo, buttonLogout;

    public static Fragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        buttonEditName = view.findViewById(R.id.editNameProfileButton);
        buttonEditSurname = view.findViewById(R.id.editSurnameProfileButton);
        buttonEditPhoneNo = view.findViewById(R.id.editPhoneNumberProfileButton);
        buttonLogout = view.findViewById(R.id.logoutButton);

        buttonEditName.setOnClickListener(this);
        buttonEditSurname.setOnClickListener(this);
        buttonEditPhoneNo.setOnClickListener(this);
        buttonLogout.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        profilePicImageView = view.findViewById(R.id.imageProfileImageView);
        nameProfileTextView = view.findViewById(R.id.nameProfileTextView);
        surnameProfileTextView = view.findViewById(R.id.surnameProfileTextView);
        phoneNumberProfileTextView = view.findViewById(R.id.phoneNumberProfileTextView);
        emailProfileTextView = view.findViewById(R.id.emailProfileTextView);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        StorageReference storageReference = firebaseStorage.getReference();
        // Get the image stored on Firebase via "User id/Images/Profile Pic.jpg".
        storageReference.child(firebaseAuth.getUid()).child("Images").child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Using "Picasso" (http://square.github.io/picasso/) after adding the dependency in the Gradle.
                // ".fit().centerInside()" fits the entire image into the specified area.
                // Finally, add "READ" and "WRITE" external storage permissions in the Manifest.
                Picasso.get().load(uri).fit().centerInside().into(profilePicImageView);
            }
        });
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

    private void buttonClickedEditName() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_edit_name, null);
        final EditText setUserName = alertLayout.findViewById(R.id.setUserName);
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Name Edit");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void buttonClickedEditSurname() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_edit_surname, null);
        final EditText setUserSurname = alertLayout.findViewById(R.id.setUserSurname);
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Surname Edit");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void buttonClickedEditPhoneNumber() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_edit_phone_number, null);
        final EditText setUserPhoneNumber = alertLayout.findViewById(R.id.setUserPhoneNumber);
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Phone No Edit");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void buttonClickedLogout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.editNameProfileButton:
                buttonClickedEditName();
                break;
            case R.id.editSurnameProfileButton:
                buttonClickedEditSurname();
                break;
            case R.id.editPhoneNumberProfileButton:
                buttonClickedEditPhoneNumber();
                break;
            case R.id.logoutButton:
                buttonClickedLogout();
                break;
        }
    }
}
