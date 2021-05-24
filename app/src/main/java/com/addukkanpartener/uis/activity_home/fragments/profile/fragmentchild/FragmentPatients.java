package com.addukkanpartener.uis.activity_home.fragments.profile.fragmentchild;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.addukkanpartener.R;
import com.addukkanpartener.adapters.PatientsAdapter;
import com.addukkanpartener.databinding.FragmentPatientsBinding;
import com.addukkanpartener.models.AllUserModel;
import com.addukkanpartener.models.UserModel;
import com.addukkanpartener.remote.Api;
import com.addukkanpartener.tags.Tags;
import com.addukkanpartener.uis.activity_home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentPatients extends Fragment {
    private FragmentPatientsBinding binding;
    private HomeActivity activity;
    private List<UserModel.User> userList;
    private PatientsAdapter patientsAdapter;
    private String query;
    private Call<AllUserModel> call;

    public static FragmentPatients newInstance() {
        return new FragmentPatients();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_patients, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        userList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        patientsAdapter = new PatientsAdapter(activity, userList);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(patientsAdapter);
        // binding.progBar.setVisibility(View.GONE);
        binding.editQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                query = binding.editQuery.getText().toString();
                getPatinets();
            }
        });
        getPatinets();

    }

    private void getPatinets() {
        userList.clear();
        binding.tvNoData.setVisibility(View.GONE);
        binding.progBar.setVisibility(View.VISIBLE);
        patientsAdapter.notifyDataSetChanged();

        if (call != null) {
            call.cancel();
        }
        call = Api.getService(Tags.base_url)
                .getPatient(query, "off");
        call.enqueue(new Callback<AllUserModel>() {
            @Override
            public void onResponse(Call<AllUserModel> call, Response<AllUserModel> response) {
                binding.progBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    if (response.body() != null && response.body().getStatus() == 200) {
                        if (response.body().getData() != null) {
                            if (response.body().getData().size() > 0) {
                                binding.tvNoData.setVisibility(View.GONE);
                                updatePatientData(response.body());
                            } else {
                                activity.updatecount(0);
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
            public void onFailure(Call<AllUserModel> call, Throwable t) {
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

    private void updatePatientData(AllUserModel data) {
        userList.addAll(data.getData());
        patientsAdapter.notifyDataSetChanged();
        activity.updatecount(data.getCount());
    }


}
