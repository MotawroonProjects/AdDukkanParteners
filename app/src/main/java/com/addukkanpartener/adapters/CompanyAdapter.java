package com.addukkanpartener.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.addukkanpartener.R;
import com.addukkanpartener.databinding.ChooseRowBinding;
import com.addukkanpartener.models.CompanyModel;
import com.addukkanpartener.uis.activity_company.ChooseCompanyActivity;

import java.util.List;


public class CompanyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<CompanyModel> list;
    private ChooseCompanyActivity activity;

    public CompanyAdapter(Context context,List<CompanyModel> list) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        activity = (ChooseCompanyActivity) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ChooseRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.choose_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setTitle(list.get(position).getProduct_company_trans_fk().getTitle());
        myHolder.itemView.setOnClickListener(v -> {
            activity.setItemData(list.get(myHolder.getAdapterPosition()));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private ChooseRowBinding binding;

        public MyHolder(ChooseRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }
}
