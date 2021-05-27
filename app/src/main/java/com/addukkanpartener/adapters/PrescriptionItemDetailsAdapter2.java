package com.addukkanpartener.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.addukkanpartener.R;
import com.addukkanpartener.databinding.PrescriptionDetailsRowBinding;
import com.addukkanpartener.databinding.PrescriptionRow2Binding;
import com.addukkanpartener.databinding.PrescriptionRowBinding;
import com.addukkanpartener.models.OrderModel;
import com.addukkanpartener.uis.activity_prescription_details1.PrescriptionDetails1Activity;
import com.addukkanpartener.uis.activity_print.PrintActivity;

import java.util.List;


public class PrescriptionItemDetailsAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<OrderModel.PrescriptionDetailsFk> list;

    public PrescriptionItemDetailsAdapter2(Context context, List<OrderModel.PrescriptionDetailsFk> list) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        PrescriptionRow2Binding binding = DataBindingUtil.inflate(inflater, R.layout.prescription_row2, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        OrderModel.PrescriptionDetailsFk model = list.get(position);

        myHolder.binding.setModel(model);



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private PrescriptionRow2Binding binding;

        public MyHolder(PrescriptionRow2Binding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }
}
