package com.addukkanpartener.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.addukkanpartener.R;
import com.addukkanpartener.databinding.UserSearchRowBinding;
import com.addukkanpartener.uis.activity_chat.ChatActivity;


public class RoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;

    public RoomAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        UserSearchRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.user_search_row, parent, false);
        return new UserHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(context, ChatActivity.class);
        context.startActivity(intent);
    }
});

    }

    @Override
    public int getItemCount() {
        return 18;
    }

    public static class UserHolder extends RecyclerView.ViewHolder {
        private UserSearchRowBinding binding;

        public UserHolder(UserSearchRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }
}
