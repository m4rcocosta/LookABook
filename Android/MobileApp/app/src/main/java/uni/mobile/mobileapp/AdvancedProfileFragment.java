package uni.mobile.mobileapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.List;

import uni.mobile.mobileapp.rest.RestLocalMethods;


public class AdvancedProfileFragment extends Fragment {

    private Button changeEmailButton, changePasswordButton, deleteUserButton;
    private SharedPreferences.Editor editor;
    private FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_advanced_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();

        changeEmailButton = view.findViewById(R.id.changeEmailButton);
        changePasswordButton = view.findViewById(R.id.changePasswordButton);
        deleteUserButton = view.findViewById(R.id.deleteUserButton);

        changeEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEmail();
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDelete();
            }
        });

        String provider = getUserProvider(user);
        if (provider != "FIREBASE") {
            changeEmailButton.setVisibility(View.GONE);
            changePasswordButton.setVisibility(View.GONE);
        }

        editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
    }

    private void changeEmail() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_edit_email, null);
        final EditText setUserEmail = alertLayout.findViewById(R.id.setUserEmail);
        final EditText confirmUserEmail = alertLayout.findViewById(R.id.confirmUserEmail);
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Edit email")
                .setMessage("Insert the new email")
                .setView(alertLayout) // this is set the view from XML inside AlertDialog
                .setCancelable(false) // disallow cancel of AlertDialog on click of back button and outside touch
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newEmail = setUserEmail.getText().toString();
                        String confirmEmail = confirmUserEmail.getText().toString();
                        if(TextUtils.isEmpty(newEmail)){
                            Toast.makeText(getContext(),"Please enter your E-mail address",Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(TextUtils.isEmpty(confirmEmail)){
                            Toast.makeText(getContext(),"Please confirm your E-mail address",Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (!newEmail.equals(confirmEmail)) {
                            Toast.makeText(getContext(), "Emails must be equals!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            user.updateEmail(newEmail)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Email updated!", Toast.LENGTH_SHORT).show();
                                            }
                                            else Toast.makeText(getContext(), "Error while updating email", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        setUserEmail.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void changePassword() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_edit_password, null);
        final EditText setUserPassword = alertLayout.findViewById(R.id.setUserPassword);
        final EditText confirmUserPassword = alertLayout.findViewById(R.id.confirmUserPassword);
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Change password")
                .setMessage("Insert the new password")
                .setView(alertLayout) // this is set the view from XML inside AlertDialog
                .setCancelable(false) // disallow cancel of AlertDialog on click of back button and outside touch
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newPassword = setUserPassword.getText().toString();
                        String confirmPassword = confirmUserPassword.getText().toString();
                        if(TextUtils.isEmpty(newPassword)){
                            Toast.makeText(getContext(),"Please enter your Password",Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(TextUtils.isEmpty(confirmPassword)){
                            Toast.makeText(getContext(),"Please confirm your Password",Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (newPassword.length() == 0){
                            Toast.makeText(getContext(),"Please enter your Password",Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (confirmPassword.length() == 0){
                            Toast.makeText(getContext(),"Please confirm your Password",Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (newPassword.length()<8){
                            Toast.makeText(getContext(),"Password must be more than 8 digit",Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (!newPassword.equals(confirmPassword)) {
                            Toast.makeText(getContext(),"Passwords must be equals",Toast.LENGTH_LONG).show();
                            return;
                        }
                        else {
                            user.updatePassword(newPassword)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(),"Passwords updated!",Toast.LENGTH_LONG).show();
                                            }
                                            else Toast.makeText(getContext(), "Error while updating password! Try to logout and login again", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                        setUserPassword.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void userDelete() {
        AuthUI.getInstance().delete(requireContext()).addOnCompleteListener(new OnCompleteListener<Void>(){

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                RestLocalMethods.initRetrofit(getContext(), RestLocalMethods.getUserToken());
                RestLocalMethods.deleteUser(RestLocalMethods.getMyUserId());
                editor.putBoolean("UseBiometrics", false);
                editor.apply();
                Toast.makeText(getContext(), "Your profile is deleted!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finishAffinity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed to delete your account! Try to logout and login again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getUserProvider(FirebaseUser user) {
        List<? extends UserInfo> infos = user.getProviderData();
        String provider = "FIREBASE";
        for (UserInfo ui : infos) {
            if (ui.getProviderId().equals(GoogleAuthProvider.PROVIDER_ID)) provider = "GOOGLE";
            else if (ui.getProviderId().equals(FacebookAuthProvider.PROVIDER_ID)) provider = "FACEBOOK";
        }
        return provider;
    }
}
