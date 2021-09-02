package com.addukkanpartener.uis.activity_home.fragments.profile.fragmentchild;

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
import com.addukkanpartener.adapters.DoctorPrescriptionAdapter;
import com.addukkanpartener.adapters.PatientsAdapter;
import com.addukkanpartener.databinding.FragmentMyPrescriptionBinding;
import com.addukkanpartener.databinding.FragmentPatientsBinding;
import com.addukkanpartener.models.AllUserModel;
import com.addukkanpartener.models.ClientPrescriptionDetailsDataModel;
import com.addukkanpartener.models.ClientPrescriptionDetailsModel;
import com.addukkanpartener.models.UserModel;
import com.addukkanpartener.preferences.Preferences;
import com.addukkanpartener.remote.Api;
import com.addukkanpartener.tags.Tags;
import com.addukkanpartener.uis.activity_client_prescription.ClientPrescriptionActivity;
import com.addukkanpartener.uis.activity_home.HomeActivity;
import com.addukkanpartener.uis.activity_prescription_details1.PrescriptionDetails1Activity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMyPrescriptions extends Fragment {
    private FragmentMyPrescriptionBinding binding;
    private HomeActivity activity;
    private List<ClientPrescriptionDetailsModel> list;
    private Preferences preferences;
    private UserModel userModel;
    private DoctorPrescriptionAdapter adapter;

    public static FragmentMyPrescriptions newInstance() {
        return new FragmentMyPrescriptions();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_prescription, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        list = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        adapter = new DoctorPrescriptionAdapter(list,activity,this);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(adapter);
        getPrescriptions();

    }

    private void getPrescriptions() {
        list.clear();
        binding.tvNoData.setVisibility(View.GONE);
        binding.progBar.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();

        Api.getService(Tags.base_url)
                .getMyPrescription("Bearer "+userModel.getData().getToken(),userModel.getData().getId())
                .enqueue(new Callback<ClientPrescriptionDetailsDataModel>() {
                    @Override
                    public void onResponse(Call<ClientPrescriptionDetailsDataModel> call, Response<ClientPrescriptionDetailsDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData() != null) {
                                    binding.tvNoData.setVisibility(View.GONE);
                                    list.clear();
                                    list.addAll(response.body().getData());
                                    adapter.notifyDataSetChanged();
                                    if (list.size()>0){
                                        binding.tvNoData.setVisibility(View.GONE);
                                    }else {
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
                    public void onFailure(Call<ClientPrescriptionDetailsDataModel> call, Throwable t) {
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

    public void setItemData(ClientPrescriptionDetailsModel clientPrescriptionDetailsModel) {
        Intent intent = new Intent(activity, PrescriptionDetails1Activity.class);
        intent.putExtra("data", clientPrescriptionDetailsModel.getId());
        startActivity(intent);
    }
}
