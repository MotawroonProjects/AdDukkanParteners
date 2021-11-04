package com.addukkanpartener.uis.activity_client_prescription;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.addukkanpartener.R;
import com.addukkanpartener.adapters.ClientPrescriptionAdapter;
import com.addukkanpartener.adapters.CompanyAdapter;
import com.addukkanpartener.databinding.ActivityChooseBinding;
import com.addukkanpartener.databinding.ActivityClientPrescriptionBinding;
import com.addukkanpartener.language.Language;
import com.addukkanpartener.models.ClientPrescriptionDetailsDataModel;
import com.addukkanpartener.models.ClientPrescriptionDetailsModel;
import com.addukkanpartener.models.CompanyDataModel;
import com.addukkanpartener.models.CompanyModel;
import com.addukkanpartener.models.SingleOrderDataModel;
import com.addukkanpartener.models.UserModel;
import com.addukkanpartener.preferences.Preferences;
import com.addukkanpartener.remote.Api;
import com.addukkanpartener.tags.Tags;
import com.addukkanpartener.uis.activity_edit_patient.EditPatientActivity;
import com.addukkanpartener.uis.activity_prescription_details1.PrescriptionDetails1Activity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientPrescriptionActivity extends AppCompatActivity {
    private ActivityClientPrescriptionBinding binding;
    private String lang;
    private List<ClientPrescriptionDetailsModel> list;
    private ClientPrescriptionAdapter adapter;
    private Preferences preferences;
    private UserModel userModel;
    private UserModel.User client;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_client_prescription);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        client = (UserModel.User) intent.getSerializableExtra("data");
    }

    private void initView() {
        list = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setModel(client);
        binding.progBar.setVisibility(View.GONE);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ClientPrescriptionAdapter(list,this);
        binding.recView.setAdapter(adapter);
        binding.llBack.setOnClickListener(v -> finish());
        getData();

        binding.iconEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditPatientActivity.class);
            intent.putExtra("data", client);
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        getData();
        super.onResume();
    }

    private void getData() {
        list.clear();
        binding.tvNoData.setVisibility(View.GONE);
        binding.progBar.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();

        Api.getService(Tags.base_url)
                .getClientPrescription("Bearer "+userModel.getData().getToken(),lang,client.getId())
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
        Intent intent = new Intent(this, PrescriptionDetails1Activity.class);
        intent.putExtra("data", clientPrescriptionDetailsModel.getId());
        startActivity(intent);
    }
}