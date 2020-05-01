package uni.mobile.mobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailSignUp, confirmEmailSignUp, passwordSignUp, confirmPasswordSignUp;
    private Button signUpButton;
    private TextView alreadyRegisteredButton;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private EditText nameEditText, phoneNumberEditText;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;

    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Permission
        if (ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)  == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(SignUpActivity.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, STORAGE_PERMISSION_CODE);
        }

        emailSignUp = findViewById(R.id.emailSignUpEditText);
        confirmEmailSignUp = findViewById(R.id.confirmEmailSignUpEditText);
        passwordSignUp = findViewById(R.id.passwordSignUpEditText);
        confirmPasswordSignUp = findViewById(R.id.confirmPasswordSignUpEditText);
        auth = FirebaseAuth.getInstance();
        signUpButton = findViewById(R.id.signUpButton);
        alreadyRegisteredButton = findViewById(R.id.alreadyRegisteredButton);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        nameEditText = findViewById(R.id.nameSignUpEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberSignUpEditText);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        signUpButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String phoneNumber = phoneNumberEditText.getText().toString().trim();
                String email = emailSignUp.getText().toString();
                String confirmEmail = confirmEmailSignUp.getText().toString();
                String pass = passwordSignUp.getText().toString();
                String confirmPass = confirmPasswordSignUp.getText().toString();


                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "Enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(getApplicationContext(), "Enter your phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Please enter your E-mail address",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(confirmEmail)){
                    Toast.makeText(getApplicationContext(),"Please confirm your E-mail address",Toast.LENGTH_LONG).show();
                    return;
                }
                if (!email.equals(confirmEmail)) {
                    Toast.makeText(getApplicationContext(),"Emails must be equals",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    Toast.makeText(getApplicationContext(),"Please enter your Password",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(confirmPass)){
                    Toast.makeText(getApplicationContext(),"Please confirm your Password",Toast.LENGTH_LONG).show();
                    return;
                }
                if (pass.length() == 0){
                    Toast.makeText(getApplicationContext(),"Please enter your Password",Toast.LENGTH_LONG).show();
                    return;
                }
                if (confirmPass.length() == 0){
                    Toast.makeText(getApplicationContext(),"Please confirm your Password",Toast.LENGTH_LONG).show();
                    return;
                }
                if (pass.length()<8){
                    Toast.makeText(getApplicationContext(),"Password must be more than 8 digit",Toast.LENGTH_LONG).show();
                    return;
                }
                if (!pass.equals(confirmPass)) {
                    Toast.makeText(getApplicationContext(),"Passwords must be equals",Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        try {
                                            throw task.getException();
                                        }
                                        // if user enters wrong password.
                                        catch (FirebaseAuthWeakPasswordException weakPassword) {
                                            Toast.makeText(SignUpActivity.this, "Wrong password.",Toast.LENGTH_LONG).show();
                                        }
                                        // if user enters wrong email.
                                        catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                            Toast.makeText(SignUpActivity.this, "Wrong email.",Toast.LENGTH_LONG).show();
                                        }
                                        catch (FirebaseAuthUserCollisionException existEmail) {
                                            Toast.makeText(SignUpActivity.this, "An account with this email already exists! If you don't remember your password, reset it.",Toast.LENGTH_LONG).show();
                                        }
                                        catch (Exception e) {
                                            Log.d("SIGN UP", "onComplete: " + e.getMessage());
                                        }
                                    }
                                    else {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        Userinformation userinformation = new Userinformation(phoneNumber);
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            //Success
                                                        }
                                                    }
                                                });
                                        databaseReference.child(user.getUid()).setValue(userinformation);
                                        Toast.makeText(getApplicationContext(),"User information updated",Toast.LENGTH_LONG).show();
                                        user.sendEmailVerification();
                                        Toast.makeText(getApplicationContext(),"Activate your account by clicking on the link sent at the email " + user.getEmail(),Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                        finish();
                                    }
                                }
                            });}
            }
        });

        alreadyRegisteredButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                navigateSignIn();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(SignUpActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(SignUpActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void navigateSignIn(){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
}
