package uni.mobile.mobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText inputEmail;
    private MaterialButton btnReset;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        inputEmail = findViewById(R.id.EditTextEmailRP);
        btnReset = findViewById(R.id.btn_reset_password);
        auth = FirebaseAuth.getInstance();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Enter your mail address", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetPasswordActivity.this, "We send you an e-mail", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(ResetPasswordActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    public void NavigateSignUp(View v) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();;
    }
}
