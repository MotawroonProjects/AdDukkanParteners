package com.addukkanpartener.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.addukkanpartener.R;
import com.addukkanpartener.databinding.ClientPrescriptionRowBinding;
import com.addukkanpartener.databinding.DoctorPrescriptionRowBinding;
import com.addukkanpartener.models.ClientPrescriptionDetailsModel;
import com.addukkanpartener.uis.activity_client_prescription.ClientPrescriptionActivity;
import com.addukkanpartener.uis.activity_home.fragments.profile.fragmentchild.FragmentMyPrescriptions;

import java.util.List;

public class DoctorPrescriptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ClientPrescriptionDetailsModel> list;
    private Context context;
    private LayoutInflater inflater;
    private FragmentMyPrescriptions  fragment;
    public DoctorPrescriptionAdapter(List<ClientPrescriptionDetailsModel> list, Context context,FragmentMyPrescriptions  fragment) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list =list;
        this.fragment = fragment;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DoctorPrescriptionRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.doctor_prescription_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
            ClientPrescriptionDetailsModel model = list.get(position);
            myHolder.binding.setModel(model);

            if (model.getStatus().equals("new")){
                myHolder.binding.imageStatus.setImageResource(R.color.color6);
                myHolder.binding.tvStatus.setText(R.string.new1);
            }else if (model.getStatus().equals("part")){
                myHolder.binding.imageStatus.setImageResource(R.color.color2);
                myHolder.binding.tvStatus.setText(R.string.partially_purchased);

            }else if (model.getStatus().equals("pending")){
                myHolder.binding.imageStatus.setImageResource(R.color.rate_color);
                myHolder.binding.tvStatus.setText(R.string.pending);

            }
            else if (model.getStatus().equals("old")){
                myHolder.binding.imageStatus.setImageResource(R.color.colorPrimary);
                myHolder.binding.tvStatus.setText(R.string.done2);

            }else {
                myHolder.binding.imageStatus.setImageResource(R.color.color5);
                myHolder.binding.tvStatus.setText(R.string.in_cart);

            }

            myHolder.binding.btnDetails.setOnClickListener(v -> {
                fragment.setItemData(list.get(myHolder.getAdapterPosition()));
            });

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public DoctorPrescriptionRowBinding binding;

        public MyHolder(@NonNull DoctorPrescriptionRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
