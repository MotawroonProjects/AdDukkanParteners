package com.addukkanpartener.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.addukkanpartener.R;
import com.addukkanpartener.databinding.PrescriptionRowBinding;
import com.addukkanpartener.models.AddPrescriptionModel;
import com.addukkanpartener.models.DoctorTreatmentModel;
import com.addukkanpartener.uis.activity_add_prescription.PrescriptionActivity;
import com.addukkanpartener.uis.activity_print.PrintActivity;

import java.util.List;


public class PrescriptionItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<AddPrescriptionModel.ItemModel> list;
    private AppCompatActivity activity;

    public PrescriptionItemAdapter(Context context, List<AddPrescriptionModel.ItemModel> list) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        activity = (AppCompatActivity) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        PrescriptionRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.prescription_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));

        myHolder.binding.imageIncrease.setOnClickListener(v -> {
            if (activity instanceof PrescriptionActivity){
                PrescriptionActivity prescriptionActivity = (PrescriptionActivity) activity;
                AddPrescriptionModel.ItemModel model = list.get(myHolder.getAdapterPosition());
                int amount =model.getAmount();
                amount +=1;
                model.setAmount(amount);
                list.set(myHolder.getAdapterPosition(),model);
                notifyItemChanged(myHolder.getAdapterPosition());
                prescriptionActivity.updateItem(myHolder.getAdapterPosition(),model);
            }
        });

        myHolder.binding.imageDecrease.setOnClickListener(v -> {
            if (activity instanceof PrescriptionActivity){
                PrescriptionActivity prescriptionActivity = (PrescriptionActivity) activity;
                AddPrescriptionModel.ItemModel model = list.get(myHolder.getAdapterPosition());
                int amount =model.getAmount();
                if (amount>1){
                    amount -=1;
                    model.setAmount(amount);
                    list.set(myHolder.getAdapterPosition(),model);
                    notifyItemChanged(myHolder.getAdapterPosition());

                    prescriptionActivity.updateItem(myHolder.getAdapterPosition(),model);
                }else {
                    list.remove(myHolder.getAdapterPosition());
                    notifyItemChanged(myHolder.getAdapterPosition());
                    prescriptionActivity.deleteItem(model);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private PrescriptionRowBinding binding;

        public MyHolder(PrescriptionRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }
}
