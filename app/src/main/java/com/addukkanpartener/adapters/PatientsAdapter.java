package com.addukkanpartener.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.addukkanpartener.R;
import com.addukkanpartener.databinding.PatientRowBinding;
import com.addukkanpartener.models.UserModel;

import java.util.List;


public class PatientsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<UserModel.User> list;

    public PatientsAdapter(Context context, List<UserModel.User> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        PatientRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.patient_row, parent, false);
        return new PatientHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        PatientHolder eventHolder = (PatientHolder) holder;
eventHolder.binding.setModel(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class PatientHolder extends RecyclerView.ViewHolder {
        private PatientRowBinding binding;

        public PatientHolder(PatientRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }
}
