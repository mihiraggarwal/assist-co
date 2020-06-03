package com.example.assist_co;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String name, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
    }

    public void signUp(View view) {
        final TextInputEditText nameet = findViewById(R.id.name_signup);
        TextInputEditText emailet = findViewById(R.id.email_signup);
        TextInputEditText passwordet = findViewById(R.id.password_signup);
        name = nameet.getText().toString();
        email = emailet.getText().toString();
        password = passwordet.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();
                                UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                                user.updateProfile(request);
                                Intent intent = new Intent(SignUpActivity.this, DetailsActivity.class);
                                intent.putExtra("name", name);
                                intent.putExtra("email", email);
                                startActivity(intent);
                                Toast.makeText(SignUpActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void goToLogin(View view) {
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
    }
}
