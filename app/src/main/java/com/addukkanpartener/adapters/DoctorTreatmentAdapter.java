package com.addukkanpartener.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.addukkanpartener.R;
import com.addukkanpartener.databinding.ChooseRowBinding;
import com.addukkanpartener.databinding.TreatmentRowBinding;
import com.addukkanpartener.models.DoctorTreatmentModel;
import com.addukkanpartener.models.TreatmentModel;
import com.addukkanpartener.uis.activity_home.fragments.profile.fragmentchild.FragmentSelectedTreatments;
import com.addukkanpartener.uis.activity_treatments.ChooseTreatmentActivity;

import java.util.List;


public class DoctorTreatmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<DoctorTreatmentModel> list;
    private FragmentSelectedTreatments fragment;
    public DoctorTreatmentAdapter(Context context, List<DoctorTreatmentModel> list,FragmentSelectedTreatments fragment) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        TreatmentRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.treatment_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.imageClose.setOnClickListener(v -> {
            fragment.delete(list.get(myHolder.getAdapterPosition()),myHolder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private TreatmentRowBinding binding;

        public MyHolder(TreatmentRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }
}
