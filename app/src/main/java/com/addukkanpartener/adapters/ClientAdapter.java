package com.addukkanpartener.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.addukkanpartener.R;
import com.addukkanpartener.databinding.ChooseRowBinding;
import com.addukkanpartener.databinding.ClientRowBinding;
import com.addukkanpartener.models.CompanyModel;
import com.addukkanpartener.models.UserModel;
import com.addukkanpartener.uis.activity_add_prescription.PrescriptionActivity;
import com.addukkanpartener.uis.activity_company.ChooseCompanyActivity;

import java.util.List;


public class ClientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<UserModel.User> list;
    private PrescriptionActivity activity;

    public ClientAdapter(Context context, List<UserModel.User> list) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        activity = (PrescriptionActivity) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ClientRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.client_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(v -> {
            activity.setClientItem(list.get(myHolder.getAdapterPosition()));
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private ClientRowBinding binding;

        public MyHolder(ClientRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }
}
