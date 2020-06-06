package com.example.assist_co.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assist_co.R;
import com.example.assist_co.models.Company;

import java.util.ArrayList;

public class CompaniesRecyclerAdapter extends RecyclerView.Adapter<CompaniesRecyclerAdapter.ViewHolder> {

    private ArrayList<Company> mCompanies = new ArrayList<>();
    private OnCompanyListener mOnCompanyListener;

    public CompaniesRecyclerAdapter(ArrayList<Company> companies, OnCompanyListener onCompanyListener){
        this.mCompanies = companies;
        this.mOnCompanyListener = onCompanyListener;
    }

    @NonNull
    @Override
    public CompaniesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view, mOnCompanyListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CompaniesRecyclerAdapter.ViewHolder holder, int position) {
        holder.name.setText(mCompanies.get(position).getName());
        holder.overview.setText(mCompanies.get(position).getOverview());
        holder.servicesProvided.setText(mCompanies.get(position).getServicesProvided());
    }

    @Override
    public int getItemCount() {
        return mCompanies.size();
    }

    public interface OnCompanyListener {
        void onCompanyClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name, overview, servicesProvided;
        OnCompanyListener onCompanyListener;

        public ViewHolder(@NonNull View itemView, OnCompanyListener onCompanyListener) {
            super(itemView);
            name = itemView.findViewById(R.id.listitem_name);
            overview = itemView.findViewById(R.id.listitem_overview);
            servicesProvided = itemView.findViewById(R.id.listitem_servicesProvided);
            itemView.setOnClickListener(this);
            this.onCompanyListener = onCompanyListener;
        }

        @Override
        public void onClick(View view) {
            onCompanyListener.onCompanyClick(getAdapterPosition());
        }
    }
}
