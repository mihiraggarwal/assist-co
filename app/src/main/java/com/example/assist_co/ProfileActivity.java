package com.example.assist_co;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuAdapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputEditText nameet, emailet, phoneet, passwordet, yearet, overviewet, websiteet;
    private TextInputEditText servicesProvidedet, servicesRequiredet;
    private String name, email, password, phone, year, overview, website, servicesProvided, servicesRequired;
    private String oldEmail, oldPassword;
    private String doc;
    private MaterialButton delete_btn;
    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        nameet = findViewById(R.id.name_profile);
        phoneet = findViewById(R.id.phone_profile);
        emailet = findViewById(R.id.email_profile);
        passwordet = findViewById(R.id.password_profile);
        overviewet = findViewById(R.id.overview_profile);
        yearet = findViewById(R.id.year_profile);
        websiteet = findViewById(R.id.website_profile);
        servicesProvidedet = findViewById(R.id.servicesProvided_profile);
        servicesRequiredet = findViewById(R.id.servicesRequired_profile);

        setUserProperties();

        final FirebaseUser user = mAuth.getCurrentUser();

        delete_btn = findViewById(R.id.delete_btn);
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProfileActivity.this);
                alertDialogBuilder.setTitle("Are you sure you want to delete your profile?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AuthCredential credential = EmailAuthProvider.getCredential(oldEmail, oldPassword);
                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Log.d(TAG, "onComplete early: reauth success");
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection("Users")
                                            .document(doc)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    user.delete();
                                                    startActivity(new Intent(ProfileActivity.this, SignUpActivity.class));
                                                    if (user!=null){
                                                        Log.d(TAG, "onSuccess:  user present");
                                                    }
                                                    else {
                                                        Log.d(TAG, "onSuccess: user absent");
                                                    }
                                                    Toast.makeText(ProfileActivity.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else {
                                    Log.d(TAG, "onComplete: " + task.getException().getMessage());
                                }
                            }
                        });
                    }
                });
                alertDialogBuilder.setNegativeButton("No", null);
                alertDialogBuilder.show();
            }
        });
    }

    private void setUserProperties() {
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .whereEqualTo("uid", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                nameet.setText(documentSnapshot.getString("name"));
                                phoneet.setText(documentSnapshot.getString("phone"));
                                emailet.setText(documentSnapshot.getString("email"));
                                passwordet.setText(documentSnapshot.getString("password"));
                                websiteet.setText(documentSnapshot.getString("website"));
                                yearet.setText(documentSnapshot.getString("year_of_creation"));
                                overviewet.setText(documentSnapshot.getString("overview"));
                                servicesProvidedet.setText(documentSnapshot.getString("services_provided"));
                                servicesRequiredet.setText(documentSnapshot.getString("services_required"));
                                doc = documentSnapshot.getString("docId");

                                oldEmail = emailet.getText().toString();
                                oldPassword = passwordet.getText().toString();
                            }
                        }
                        else {
                            Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void update(View view) {
        name = nameet.getText().toString();
        email = emailet.getText().toString();
        //password = passwordet.getText().toString();
        phone = phoneet.getText().toString();
        year = yearet.getText().toString();
        website = websiteet.getText().toString();
        overview = overviewet.getText().toString();
        servicesProvided = servicesProvidedet.getText().toString();
        servicesRequired = servicesRequiredet.getText().toString();
        final FirebaseUser user = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user_details = new HashMap<>();
        user_details.put("name", name);
        user_details.put("email", email);
        //user_details.put("password", password);
        user_details.put("phone", phone);
        user_details.put("year_of_creation", year);
        user_details.put("website", website);
        user_details.put("overview", overview);
        user_details.put("services_provided", servicesProvided);
        user_details.put("services_required", servicesRequired);

        if (TextUtils.isEmpty(year) || TextUtils.isEmpty(overview) || TextUtils.isEmpty(website)
                || TextUtils.isEmpty(servicesProvided) || TextUtils.isEmpty(servicesRequired) || TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(name) || TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
        }
        else {
            db.collection("Users")
                    .document(doc)
                    .set(user_details, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            AuthCredential credential = EmailAuthProvider.getCredential(oldEmail, oldPassword);
                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Log.d(TAG, "onComplete: reauth success");
                                    }
                                    else {
                                        Log.d(TAG, "onComplete: " + task.getException().getMessage());
                                    }
                                }
                            });
                            user.updateEmail(email);
                            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                            user.updateProfile(request);
                            mAuth.signOut();
                            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                            Toast.makeText(ProfileActivity.this, "Profile updated successfully. Login again to continue", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
