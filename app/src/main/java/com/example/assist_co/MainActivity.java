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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import static com.google.common.collect.Iterables.size;

public class MainActivity extends AppCompatActivity implements CompaniesRecyclerAdapter.OnCompanyListener {
    private FirebaseAuth mAuth;
    private RecyclerView mRecyclerView;
    private ArrayList<Company> mCompanies = new ArrayList<>();
    private CompaniesRecyclerAdapter mCompaniesRecyclerAdapter;
    private static final String TAG = "MainActivity";

    private List<String> mCompare = new ArrayList<>();
    private ArrayList<String> mDuplicate = new ArrayList<>();
    private List<String> mToCompare = new ArrayList<>();

    //private Dictionary <Integer, String> similarities = new Hashtable <Integer, String>();
    private Dictionary <String, Integer> similarities = new Hashtable <String, Integer>();

    private ArrayList lengths = new ArrayList();
    private ArrayList docIds = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.companies_recyclerView);
        mAuth = FirebaseAuth.getInstance();

        getUser();

    }

    private void getUser() {
        Log.d(TAG, "getUser: running");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        db.collection("Users")
                .whereEqualTo("uid", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String [] splitter = documentSnapshot.getString("services_required").split(", ");
                                mCompare = Arrays.asList(splitter);
                                Log.d(TAG, "onComplet: " + mCompare);

                                initRecyclerView();
                                findCompanies();
                            }
                        }
                    }
                });
    }

    private void findCompanies() {
        Log.d(TAG, "insertCompanies: running");
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Log.d(TAG, "onComplete: task successful");
                                if (documentSnapshot.getString("uid").equals(user.getUid())) {
                                    // continue
                                } else {
                                    String[] splitter_other = documentSnapshot.getString("services_provided").split(", ");
                                    mToCompare = Arrays.asList(splitter_other);
                                    Log.d(TAG, "onComplete: " + mToCompare);
                                    List<String> checker = new ArrayList<>();
                                    for (String i : mToCompare) {
                                        if (mCompare.contains(i)) {
                                            checker.add(i);
                                        }
                                    }
                                    //similarities.put(size(checker), documentSnapshot.getString("docId"));
                                    similarities.put(documentSnapshot.getString("docId"), size(checker));
                                    Log.d(TAG, "onComplete: " + similarities);
                                }
                            }
                            insertCompanies();

                        } else {
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void insertCompanies() {
        for (Enumeration i = similarities.elements(); i.hasMoreElements();){
            lengths.add(i.nextElement());
        }
        Collections.sort(lengths, Collections.reverseOrder());
        Log.d(TAG, "onComplete: " + lengths);
        for (Object i : lengths){
            //docIds.add(similarities.get(i));
            for (Enumeration k = similarities.keys(); k.hasMoreElements();){
                Object n = k.nextElement();
                Object t = similarities.get(n);
                if (t == i){
                    docIds.add(n);
                    similarities.remove(n);
                    break;
                }
            }
        }
        Log.d(TAG, "onComplete: " + similarities);
        Log.d(TAG, "onComplete: " + docIds);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for (Object i : docIds){
            Log.d(TAG, "insertCompanies: " + i.toString());
            db.collection("Users")
                    .document(i.toString())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                Log.d(TAG, "onComplete: success");
                                DocumentSnapshot documentSnapshot = task.getResult();
                                Company company = new Company();
                                company.setName(documentSnapshot.getString("name"));
                                company.setEmail(documentSnapshot.getString("email"));
                                company.setPhone(documentSnapshot.getString("phone"));
                                company.setWebsite(documentSnapshot.getString("website"));
                                company.setYear(documentSnapshot.getString("year_of_creation"));
                                company.setOverview(documentSnapshot.getString("overview"));
                                company.setServicesProvided("Services provided -> " + documentSnapshot.getString("services_provided"));
                                company.setServicesRequired(documentSnapshot.getString("services_required"));
                                Log.d(TAG, "onComplete: " + documentSnapshot.getString("docId"));
                                mCompanies.add(company);

                                mCompaniesRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    });

        }
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
