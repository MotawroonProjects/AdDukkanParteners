package com.addukkanpartener.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.addukkanpartener.R;
import com.addukkanpartener.databinding.PrescriptionDetailsRowBinding;
import com.addukkanpartener.databinding.PrescriptionRowBinding;
import com.addukkanpartener.models.AddPrescriptionModel;
import com.addukkanpartener.models.OrderModel;
import com.addukkanpartener.uis.activity_add_prescription.PrescriptionActivity;
import com.addukkanpartener.uis.activity_prescription_details1.PrescriptionDetails1Activity;

import java.util.List;


public class PrescriptionItemDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<OrderModel.PrescriptionDetailsFk> list;
    private PrescriptionDetails1Activity activity;

    public PrescriptionItemDetailsAdapter(Context context, List<OrderModel.PrescriptionDetailsFk> list) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        activity = (PrescriptionDetails1Activity) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        PrescriptionDetailsRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.prescription_details_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        OrderModel.PrescriptionDetailsFk model = list.get(position);

        if (model.getStatus().equals("old")){
            myHolder.binding.status.setImageResource(R.color.colorPrimary);
        }else if (model.getStatus().equals("in_cart")){
            myHolder.binding.status.setImageResource(R.color.color5);
        }else if (model.getStatus().equals("part")){
            myHolder.binding.status.setImageResource(R.color.gray12);

        }
        myHolder.binding.setModel(model);


        myHolder.itemView.setOnClickListener(v -> {
            activity.setItemData(list.get(myHolder.getAdapterPosition()));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private PrescriptionDetailsRowBinding binding;

        public MyHolder(PrescriptionDetailsRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }
}
