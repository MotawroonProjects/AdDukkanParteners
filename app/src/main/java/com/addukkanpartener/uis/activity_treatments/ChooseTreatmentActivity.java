package com.addukkanpartener.uis.activity_treatments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.addukkanpartener.R;
import com.addukkanpartener.adapters.ChooseAdapter;
import com.addukkanpartener.adapters.TreatmentAdapter;
import com.addukkanpartener.databinding.ActivityChooseBinding;
import com.addukkanpartener.interfaces.Listeners;
import com.addukkanpartener.language.Language;
import com.addukkanpartener.models.CompanyDataModel;
import com.addukkanpartener.models.ResponseModel;
import com.addukkanpartener.models.TreatmentDataModel;
import com.addukkanpartener.models.TreatmentModel;
import com.addukkanpartener.models.UserModel;
import com.addukkanpartener.preferences.Preferences;
import com.addukkanpartener.remote.Api;
import com.addukkanpartener.share.Common;
import com.addukkanpartener.tags.Tags;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseTreatmentActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityChooseBinding binding;
    private String lang;
    private List<TreatmentModel> list;
    private TreatmentAdapter adapter;
    private Call<TreatmentDataModel> call;
    private UserModel userModel;
    private Preferences preferences;
    private int company_id;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        company_id = intent.getIntExtra("data", 0);
    }

    private void initView() {
        list = new ArrayList<>();
        Paper.init(this);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.setTitle(getResources().getString(R.string.choose_treatment));
        binding.progBar.setVisibility(View.GONE);
        adapter = new TreatmentAdapter(this,list);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
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
                if (s.toString().length()>0){
                    getTreatments(s.toString());
                }else {
                    getTreatments("all");
                }
            }
        });
        getTreatments("all");
    }

    private void getTreatments(String query) {
        list.clear();
        binding.tvNoData.setVisibility(View.GONE);
        binding.progBar.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();

        if (call != null) {
            call.cancel();
        }
Log.e("mmmmmmmm", userModel.getData().getCountry_code()+"");
        call = Api.getService(Tags.base_url)
                .getTreatments(lang,query,userModel.getData().getCountry_code(),String.valueOf(company_id));
        call.enqueue(new Callback<TreatmentDataModel>() {
            @Override
            public void onResponse(Call<TreatmentDataModel> call, Response<TreatmentDataModel> response) {
                binding.progBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    if (response.body() != null && response.body().getStatus() == 200) {
                        if (response.body().getData() != null) {
                            if (response.body().getData().size() > 0) {
                                binding.tvNoData.setVisibility(View.GONE);
                                list.clear();
                                list.addAll(response.body().getData());
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
            public void onFailure(Call<TreatmentDataModel> call, Throwable t) {
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

    @Override
    public void back() {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back();
    }

    public void setItemData(TreatmentModel treatmentModel) {
        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .addTreatment("Bearer "+userModel.getData().getToken(),userModel.getData().getId(),treatmentModel.getId(),treatmentModel.getProduct_company_id())
       .enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {

                    if (response.body() != null && response.body().getStatus() == 200) {
                        Intent intent = getIntent();
                        intent.putExtra("data", treatmentModel);
                        setResult(RESULT_OK,intent);
                        finish();
                    } else {
                        //    Toast.makeText(CountryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                    }


                } else {
                    dialog.dismiss();

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
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                try {
                    dialog.dismiss();
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
}
