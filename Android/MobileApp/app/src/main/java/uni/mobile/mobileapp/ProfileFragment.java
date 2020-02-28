package uni.mobile.mobileapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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
    private TextView nameProfileTextView, surnameProfileTextView, phoneNumberProfileTextView, emailProfileTextView;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private ImageView profilePicImageView;
    private FirebaseStorage firebaseStorage;
    private MaterialButton editNameButton, editSurnameButton, editPhoneNumberButton, logoutButton, deleteUserButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        editNameButton = view.findViewById(R.id.editNameProfileButton);
        editSurnameButton = view.findViewById(R.id.editSurnameProfileButton);
        editPhoneNumberButton = view.findViewById(R.id.editPhoneNumberProfileButton);
        logoutButton = view.findViewById(R.id.logoutButton);
        deleteUserButton = view.findViewById(R.id.deleteUserButton);

        editNameButton.setOnClickListener(this);
        editSurnameButton.setOnClickListener(this);
        editPhoneNumberButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
        deleteUserButton.setOnClickListener(this);

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

        if (firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(getActivity(), SignInActivity.class));
            getActivity().finish();
        }

        final FirebaseUser user = firebaseAuth.getCurrentUser();
        Uri imageUrl = user.getPhotoUrl();

        if (imageUrl != null) Picasso.get().load(imageUrl).fit().centerInside().into(profilePicImageView);
        else {
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
        }

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

    private void userLogout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void userDelete() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Your profile is deleted!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                Toast.makeText(getContext(), "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
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
                userLogout();
                break;
            case R.id.deleteUserButton:
                userDelete();
                break;
        }
    }
}
