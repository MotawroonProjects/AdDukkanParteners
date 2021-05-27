package com.addukkanpartener.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.addukkanpartener.R;
import com.addukkanpartener.databinding.ClientPrescriptionRowBinding;
import com.addukkanpartener.databinding.NotificationRowBinding;
import com.addukkanpartener.models.ClientPrescriptionDetailsModel;
import com.addukkanpartener.models.NotificationModel;
import com.addukkanpartener.uis.activity_client_prescription.ClientPrescriptionActivity;
import com.addukkanpartener.uis.activity_home.fragments.profile.fragmentchild.FragmentPatients;
import com.addukkanpartener.uis.activity_notification.NotificationActivity;

import java.util.List;

public class ClientPrescriptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ClientPrescriptionDetailsModel> list;
    private Context context;
    private LayoutInflater inflater;
    private ClientPrescriptionActivity activity;
    public ClientPrescriptionAdapter(List<ClientPrescriptionDetailsModel> list, Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list =list;
        activity = (ClientPrescriptionActivity) context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ClientPrescriptionRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.client_prescription_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
            ClientPrescriptionDetailsModel model = list.get(position);
            myHolder.binding.setModel(model);

            if (model.getStatus().equals("old")){
                myHolder.binding.imageStatus.setImageResource(R.color.colorPrimary);
            }else {
                myHolder.binding.imageStatus.setImageResource(R.color.color5);
            }

            myHolder.binding.btnDetails.setOnClickListener(v -> {
                activity.setItemData(list.get(myHolder.getAdapterPosition()));
            });

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public ClientPrescriptionRowBinding binding;

        public MyHolder(@NonNull ClientPrescriptionRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
