package com.addukkanpartener.uis.activity_company;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.addukkanpartener.R;
import com.addukkanpartener.adapters.ChooseAdapter;
import com.addukkanpartener.adapters.CompanyAdapter;
import com.addukkanpartener.databinding.ActivityChooseBinding;
import com.addukkanpartener.interfaces.Listeners;
import com.addukkanpartener.language.Language;
import com.addukkanpartener.models.AllUserModel;
import com.addukkanpartener.models.CompanyDataModel;
import com.addukkanpartener.models.CompanyModel;
import com.addukkanpartener.remote.Api;
import com.addukkanpartener.tags.Tags;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseCompanyActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityChooseBinding binding;
    private String lang;
    private List<CompanyModel> list;
    private Call<CompanyDataModel> call;
    private CompanyAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose);
        initView();

    }

    private void initView() {
        list = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.setTitle(getResources().getString(R.string.choose_company));
        binding.progBar.setVisibility(View.GONE);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CompanyAdapter(this,list);
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
                    getCompanies(s.toString());
                }else {
                    getCompanies("all");
                }
            }
        });
        getCompanies("all");
    }

    private void getCompanies(String query) {
        list.clear();
        binding.tvNoData.setVisibility(View.GONE);
        binding.progBar.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();

        if (call != null) {
            call.cancel();
        }

        call = Api.getService(Tags.base_url)
                .getCompany(lang,query);
        call.enqueue(new Callback<CompanyDataModel>() {
            @Override
            public void onResponse(Call<CompanyDataModel> call, Response<CompanyDataModel> response) {
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
            public void onFailure(Call<CompanyDataModel> call, Throwable t) {
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

    public void setItemData(CompanyModel companyModel) {
        Intent intent = getIntent();
        intent.putExtra("data", companyModel);
        setResult(RESULT_OK,intent);
        finish();
    }
}
