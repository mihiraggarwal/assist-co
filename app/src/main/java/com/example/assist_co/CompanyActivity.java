package com.example.assist_co;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assist_co.models.Company;
import com.google.android.material.textfield.TextInputEditText;

public class CompanyActivity extends AppCompatActivity {
    private TextInputEditText nameet, phoneet, websiteet, yearet, overviewet, servicesProvidedet, servicesRequiredet;
    private Company mInitialCompany;
    private String websiteUrl, test;
    private static final String TAG = "CompanyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        nameet = findViewById(R.id.name_company);
        phoneet = findViewById(R.id.phone_company);
        websiteet = findViewById(R.id.website_company);
        yearet = findViewById(R.id.year_company);
        overviewet = findViewById(R.id.overview_company);
        servicesProvidedet = findViewById(R.id.servicesProvided_company);
        servicesRequiredet = findViewById(R.id.servicesRequired_company);

        if (getIntent().hasExtra("selected_company")){
            mInitialCompany = getIntent().getParcelableExtra("selected_company");
            setCompanyProperties();
        }
    }

    private void setCompanyProperties() {
        nameet.setText(mInitialCompany.getName());
        phoneet.setText(mInitialCompany.getPhone());
        websiteet.setText(mInitialCompany.getWebsite());
        yearet.setText(mInitialCompany.getYear());
        overviewet.setText(mInitialCompany.getOverview());
        servicesProvidedet.setText(mInitialCompany.getServicesProvided().substring(21));
        servicesRequiredet.setText(mInitialCompany.getServicesRequired());
    }

    public void goToWebsite(View view) {
        websiteUrl = websiteet.getText().toString();
        if (websiteUrl.substring(0, 4) != "https"){
            websiteUrl = "https://" + websiteUrl;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(websiteUrl));
        try {
            startActivity(intent);
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
