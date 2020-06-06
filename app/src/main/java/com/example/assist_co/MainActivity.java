package com.example.assist_co;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.assist_co.adapters.CompaniesRecyclerAdapter;
import com.example.assist_co.models.Company;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CompaniesRecyclerAdapter.OnCompanyListener {
    private FirebaseAuth mAuth;
    private RecyclerView mRecyclerView;
    private ArrayList<Company> mCompanies = new ArrayList<>();
    private CompaniesRecyclerAdapter mCompaniesRecyclerAdapter;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.companies_recyclerView);
        mAuth = FirebaseAuth.getInstance();

        initRecyclerView();
        insertCompanies();
    }

    private void insertCompanies() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Log.d(TAG, "onComplete: task successful");
                                Company company = new Company();
                                company.setName(documentSnapshot.getString("name"));
                                company.setPhone(documentSnapshot.getString("phone"));
                                company.setWebsite(documentSnapshot.getString("website"));
                                company.setYear(documentSnapshot.getString("year_of_creation"));
                                company.setOverview(documentSnapshot.getString("overview"));
                                company.setServicesProvided("Services provided -> " + documentSnapshot.getString("services_provided"));
                                company.setServicesRequired(documentSnapshot.getString("services_required"));
                                mCompanies.add(company);
                            }
                            mCompaniesRecyclerAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mCompaniesRecyclerAdapter = new CompaniesRecyclerAdapter(mCompanies, this);
        mRecyclerView.setAdapter(mCompaniesRecyclerAdapter);
    }


    public void logout(View view) {
        mAuth.signOut();
        Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    public void goToProfile(View view) {
        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
    }

    @Override
    public void onCompanyClick(int position) {
        Intent intent = new Intent(this, CompanyActivity.class);
        intent.putExtra("selected_company", mCompanies.get(position));
        startActivity(intent);
    }
}
