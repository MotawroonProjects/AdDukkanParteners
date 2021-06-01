package com.addukkanpartener.uis.activity_home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.addukkanpartener.R;
import com.addukkanpartener.adapters.RoomAdapter;
import com.addukkanpartener.databinding.FragmentChatBinding;
import com.addukkanpartener.models.ChatRoomModel;
import com.addukkanpartener.models.DoctorTreatmentDataModel;
import com.addukkanpartener.models.RoomDataModel;
import com.addukkanpartener.models.RoomModel;
import com.addukkanpartener.models.UserModel;
import com.addukkanpartener.preferences.Preferences;
import com.addukkanpartener.remote.Api;
import com.addukkanpartener.tags.Tags;
import com.addukkanpartener.uis.activity_chat.ChatActivity;
import com.addukkanpartener.uis.activity_home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentChat extends Fragment {
    private FragmentChatBinding binding;
    private HomeActivity activity;
    private List<RoomModel> list,roomModelList;

    private RoomAdapter adapter;
    private UserModel userModel;
    private Preferences preferences;



    public static FragmentChat newInstance() {
        return new FragmentChat();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        list = new ArrayList<>();
        roomModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        adapter = new RoomAdapter(activity,list,this);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(adapter);
        binding.editQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchByName(s.toString());
            }
        });
        getRoom();
    }

    private void getRoom() {
        list.clear();
        adapter.notifyDataSetChanged();
        binding.tvNoData.setVisibility(View.GONE);
        Api.getService(Tags.base_url)
                .getRoom("Bearer "+userModel.getData().getToken(),userModel.getData().getId())
                .enqueue(new Callback<RoomDataModel>() {
                    @Override
                    public void onResponse(Call<RoomDataModel> call, Response<RoomDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData() != null) {
                                    if (response.body().getData().size() > 0) {
                                        binding.tvNoData.setVisibility(View.GONE);
                                        list.addAll(response.body().getData());
                                        roomModelList.clear();
                                        roomModelList.addAll(response.body().getData());
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        binding.tvNoData.setVisibility(View.VISIBLE);

                                    }
                                }
                            } else {
                                //    Toast.makeText(CountryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {
                            binding.progBar.setVisibility(View.GONE);

                            switch (response.code()) {
                                case 500:
                                    //  Toast.makeText(CountryActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    // Toast.makeText(CountryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            try {
                                Log.e("error_code", response.code() + "_");
                            } catch (NullPointerException e) {

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<RoomDataModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    //Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    //Toast.makeText(CountryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void searchByName(String query){
        list.clear();
        if (query.isEmpty()){
            list.addAll(roomModelList);
            adapter.notifyDataSetChanged();
        }else {
            List<RoomModel> data = new ArrayList<>();
            for (RoomModel roomModel:roomModelList){
                if (roomModel.getOther_user().getName().startsWith(query)){
                   data.add(roomModel);
                }
            }
            list.addAll(data);
            adapter.notifyDataSetChanged();

        }
    }

    public void setItemData(RoomModel roomModel) {

        ChatRoomModel chatRoomModel = new ChatRoomModel(roomModel.getId(),roomModel.getOther_user().getId(),roomModel.getOther_user().getLogo(),roomModel.getOther_user().getName());
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra("data", chatRoomModel);
        startActivity(intent);


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("dddd", "dddd");
    }


}
