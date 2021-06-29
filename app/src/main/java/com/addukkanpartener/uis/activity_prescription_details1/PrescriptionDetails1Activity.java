package com.addukkanpartener.uis.activity_prescription_details1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;

import com.addukkanpartener.R;
import com.addukkanpartener.adapters.PrescriptionItemDetailsAdapter;
import com.addukkanpartener.databinding.ActivityLoginBinding;
import com.addukkanpartener.databinding.ActivityPrescriptionDetails1Binding;
import com.addukkanpartener.language.Language;
import com.addukkanpartener.models.LoginModel;
import com.addukkanpartener.models.OrderDataModel;
import com.addukkanpartener.models.OrderModel;
import com.addukkanpartener.models.SettingDataModel;
import com.addukkanpartener.models.SingleOrderDataModel;
import com.addukkanpartener.models.UserModel;
import com.addukkanpartener.preferences.Preferences;
import com.addukkanpartener.remote.Api;
import com.addukkanpartener.tags.Tags;
import com.addukkanpartener.uis.activity_print.PrintActivity;
import com.addukkanpartener.uis.activity_sign_up.SignUpActivity;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrescriptionDetails1Activity extends AppCompatActivity {

    private ActivityPrescriptionDetails1Binding binding;
    private String lang = "ar";
    private OrderModel orderModel;
    private int order_id;
    private PrescriptionItemDetailsAdapter adapter;
    private UserModel userModel;
    private Preferences preferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_prescription_details1);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        order_id = intent.getIntExtra("data",0);
    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));


        binding.llBack.setOnClickListener(v -> finish());

        binding.btnPrint.setOnClickListener(v -> {
            Intent intent = new Intent(this, PrintActivity.class);
            intent.putExtra("data", orderModel);
            startActivity(intent);
        });

        binding.btnShare.setOnClickListener(v -> {
            String shareLink = Tags.base_url+"api/share/user_id/"+orderModel.getUser_id()+"/code/"+orderModel.getCode();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,shareLink);
            startActivity(intent);
        });
        getOrderData();
    }

    private void getOrderData() {
        Api.getService(Tags.base_url)
                .getClientPrescriptionDetails("Bearer "+userModel.getData().getToken(),lang, userModel.getData().getCountry_code(),order_id)
                .enqueue(new Callback<SingleOrderDataModel>() {
                    @Override
                    public void onResponse(Call<SingleOrderDataModel> call, Response<SingleOrderDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData() != null) {
                                    orderModel = response.body().getData();

                                    updateUi();
                                }
                            } else {
                                //    Toast.makeText(CountryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {
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
                    public void onFailure(Call<SingleOrderDataModel> call, Throwable t) {
                        try {
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

    private void updateUi() {
        binding.setModel(orderModel);
        binding.llData.setVisibility(View.VISIBLE);
        adapter = new PrescriptionItemDetailsAdapter(this, orderModel.getPrescription_details_fk());
        binding.recView.setAdapter(adapter);
    }

    public void setItemData(OrderModel.PrescriptionDetailsFk prescriptionDetailsFk) {


    }
}