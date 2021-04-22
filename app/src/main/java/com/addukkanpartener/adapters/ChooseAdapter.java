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


public class ChooseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;

    public ChooseAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ChooseRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.choose_row, parent, false);
        return new TreatmentsHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 18;
    }

    public static class TreatmentsHolder extends RecyclerView.ViewHolder {
        private ChooseRowBinding binding;

        public TreatmentsHolder(ChooseRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }
}
