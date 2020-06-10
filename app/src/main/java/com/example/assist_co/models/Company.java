package com.example.assist_co.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Company implements Parcelable {

    private String name;
    private String email;
    private String phone;
    private String website;
    private String year;
    private String overview;
    private String servicesProvided;
    private String servicesRequired;

    public Company(String name, String email, String phone, String website, String year, String overview, String servicesProvided, String servicesRequired) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.website = website;
        this.year = year;
        this.overview = overview;
        this.servicesProvided = servicesProvided;
        this.servicesRequired = servicesRequired;
    }

    public Company() {
    }

    protected Company(Parcel in) {
        name = in.readString();
        email = in.readString();
        phone = in.readString();
        website = in.readString();
        year = in.readString();
        overview = in.readString();
        servicesProvided = in.readString();
        servicesRequired = in.readString();
    }

    public static final Creator<Company> CREATOR = new Creator<Company>() {
        @Override
        public Company createFromParcel(Parcel in) {
            return new Company(in);
        }

        @Override
        public Company[] newArray(int size) {
            return new Company[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getServicesProvided() {
        return servicesProvided;
    }

    public void setServicesProvided(String servicesProvided) {
        this.servicesProvided = servicesProvided;
    }

    public String getServicesRequired() {
        return servicesRequired;
    }

    public void setServicesRequired(String servicesRequired) {
        this.servicesRequired = servicesRequired;
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", website='" + website + '\'' +
                ", year='" + year + '\'' +
                ", overview='" + overview + '\'' +
                ", servicesProvided='" + servicesProvided + '\'' +
                ", servicesRequired='" + servicesRequired + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(phone);
        parcel.writeString(website);
        parcel.writeString(year);
        parcel.writeString(overview);
        parcel.writeString(servicesProvided);
        parcel.writeString(servicesRequired);
    }
}
