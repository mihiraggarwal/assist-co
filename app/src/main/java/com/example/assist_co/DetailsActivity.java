package com.example.assist_co;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String name, email, phone, year, overview, website, servicesProvided, servicesRequired, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (getIntent().hasExtra("name") && getIntent().hasExtra("email") && getIntent().hasExtra("password")){
            name = getIntent().getStringExtra("name");
            email = getIntent().getStringExtra("email");
            password = getIntent().getStringExtra("password");

            mAuth = FirebaseAuth.getInstance();
        }
    }


    public void addDetails(View view) {
        TextInputEditText phoneet = findViewById(R.id.phone_details);
        TextInputEditText yearet = findViewById(R.id.year_details);
        TextInputEditText websiteet = findViewById(R.id.website_details);
        TextInputEditText overviewet = findViewById(R.id.overview_details);
        TextInputEditText servicesProvidedet = findViewById(R.id.servicesProvided_details);
        TextInputEditText servicesRequiredet = findViewById(R.id.servicesRequired_details);
        phone = phoneet.getText().toString();
        year = yearet.getText().toString();
        website = websiteet.getText().toString();
        overview = overviewet.getText().toString();
        servicesProvided = servicesProvidedet.getText().toString();
        servicesRequired = servicesRequiredet.getText().toString();
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user_details = new HashMap<>();
        user_details.put("name", name);
        user_details.put("email", email);
        user_details.put("password", password);
        user_details.put("phone", phone);
        user_details.put("year_of_creation", year);
        user_details.put("website", website);
        user_details.put("overview", overview);
        user_details.put("services_provided", servicesProvided);
        user_details.put("services_required", servicesRequired);
        user_details.put("uid", user.getUid());

        if (TextUtils.isEmpty(year) || TextUtils.isEmpty(overview) || TextUtils.isEmpty(website)
        || TextUtils.isEmpty(servicesProvided) || TextUtils.isEmpty(servicesRequired) || TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
        }
        else {
            DocumentReference doc = db.collection("Users").document();
            user_details.put("docId", doc.getId());
            doc.set(user_details)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            startActivity(new Intent(DetailsActivity.this, MainActivity.class));
                            Toast.makeText(DetailsActivity.this, "Details added successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
