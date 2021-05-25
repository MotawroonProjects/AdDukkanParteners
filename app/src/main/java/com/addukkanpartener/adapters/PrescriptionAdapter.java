package com.addukkanpartener.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.addukkanpartener.databinding.SpinnerRowBinding;
import com.addukkanpartener.models.TreatmentModel;

import java.util.List;

public class PrescriptionAdapter extends ArrayAdapter<TreatmentModel> {
    private List<TreatmentModel> list;
    private Context context;
    private int layout_resource;
    private LayoutInflater inflater;

    public PrescriptionAdapter(@NonNull Context context, int resource, @NonNull List<TreatmentModel> list) {
        super(context, resource, list);
        this.list = list;
        this.context = context;
        this.layout_resource = resource;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public TreatmentModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") SpinnerRowBinding binding = DataBindingUtil.inflate(inflater,layout_resource,parent,false);
        binding.setTitle(list.get(position).getProduct_trans_fk().getTitle());
        return binding.getRoot();
    }
}
