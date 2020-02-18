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

    private DatabaseReference databaseReference;
    private TextView profileNameTextView, profileSurnameTextView, profilePhonenoTextView;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private ImageView profilePicImageView;
    private FirebaseStorage firebaseStorage;
    private TextView textViewemailname;
    private EditText editTextName;
    private MaterialButton buttonEditName, buttonEditSurname, buttonEditPhoneNo, buttonLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        buttonEditName = view.findViewById(R.id.buttonEditName);
        buttonEditSurname = view.findViewById(R.id.buttonEditSurname);
        buttonEditPhoneNo = view.findViewById(R.id.buttonEditPhoneNo);
        buttonLogout = view.findViewById(R.id.buttonLogout);

        buttonEditName.setOnClickListener(this);
        buttonEditSurname.setOnClickListener(this);
        buttonEditPhoneNo.setOnClickListener(this);
        buttonLogout.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        editTextName = (EditText) view.findViewById(R.id.et_username);
        profilePicImageView = view.findViewById(R.id.profile_pic_imageView);
        profileNameTextView = view.findViewById(R.id.profile_name_textView);
        profileSurnameTextView = view.findViewById(R.id.profile_surname_textView);
        profilePhonenoTextView = view.findViewById(R.id.profile_phoneno_textView);
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
        final FirebaseUser user=firebaseAuth.getCurrentUser();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                Userinformation userProfile = dataSnapshot.getValue(Userinformation.class);
                profileNameTextView.setText(userProfile.getUserName());
                profileSurnameTextView.setText(userProfile.getUserSurname());
                profilePhonenoTextView.setText(userProfile.getUserPhoneno());
                textViewemailname=(TextView) getView().findViewById(R.id.textViewEmailAdress);
                textViewemailname.setText(user.getEmail());
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
        final EditText etUsername = alertLayout.findViewById(R.id.et_username);
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
                String name = etUsername.getText().toString();
                String surname = profileSurnameTextView.getText().toString();
                String phoneno =  profilePhonenoTextView.getText().toString();
                Userinformation userinformation = new Userinformation(name,surname, phoneno);
                FirebaseUser user = firebaseAuth.getCurrentUser();
                databaseReference.child(user.getUid()).setValue(userinformation);
                databaseReference.child(user.getUid()).setValue(userinformation);
                etUsername.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void buttonClickedEditSurname() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_edit_surname, null);
        final EditText etUserSurname = alertLayout.findViewById(R.id.et_userSurname);
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

                String name = profileNameTextView.getText().toString();
                String surname = etUserSurname.getText().toString();
                String phoneno =  profilePhonenoTextView.getText().toString();
                Userinformation userinformation = new Userinformation(name,surname, phoneno);
                FirebaseUser user = firebaseAuth.getCurrentUser();
                databaseReference.child(user.getUid()).setValue(userinformation);
                databaseReference.child(user.getUid()).setValue(userinformation);
                etUserSurname.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void buttonClickedEditPhoneNo() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_edit_phoneno, null);
        final EditText etUserPhoneno = alertLayout.findViewById(R.id.et_userPhoneno);
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
                String name = profileNameTextView.getText().toString();
                String surname = profileSurnameTextView.getText().toString();
                String phoneno =  etUserPhoneno.getText().toString();
                Userinformation userinformation = new Userinformation(name,surname, phoneno);
                FirebaseUser user = firebaseAuth.getCurrentUser();
                databaseReference.child(user.getUid()).setValue(userinformation);
                databaseReference.child(user.getUid()).setValue(userinformation);
                etUserPhoneno.onEditorAction(EditorInfo.IME_ACTION_DONE);
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
            case R.id.buttonEditName:
                buttonClickedEditName();
                break;
            case R.id.buttonEditSurname:
                buttonClickedEditSurname();
                break;
            case R.id.buttonEditPhoneNo:
                buttonClickedEditPhoneNo();
                break;
            case R.id.buttonLogout:
                buttonClickedLogout();
                break;
        }
    }
}
