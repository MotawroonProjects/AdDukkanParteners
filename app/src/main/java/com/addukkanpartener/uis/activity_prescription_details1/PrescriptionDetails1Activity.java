package com.addukkanpartener.uis.activity_prescription_details1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;

import com.addukkanpartener.R;
import com.addukkanpartener.adapters.PrescriptionItemDetailsAdapter;
import com.addukkanpartener.databinding.ActivityLoginBinding;
import com.addukkanpartener.databinding.ActivityPrescriptionDetails1Binding;
import com.addukkanpartener.language.Language;
import com.addukkanpartener.models.LoginModel;
import com.addukkanpartener.models.OrderModel;
import com.addukkanpartener.uis.activity_print.PrintActivity;
import com.addukkanpartener.uis.activity_sign_up.SignUpActivity;

import io.paperdb.Paper;

public class PrescriptionDetails1Activity extends AppCompatActivity {

    private ActivityPrescriptionDetails1Binding binding;
    private String lang = "ar";
    private OrderModel orderModel;
    private PrescriptionItemDetailsAdapter adapter;

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
        orderModel = (OrderModel) intent.getSerializableExtra("data");
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setModel(orderModel);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PrescriptionItemDetailsAdapter(this, orderModel.getPrescription_details_fk());
        binding.recView.setAdapter(adapter);

        binding.llBack.setOnClickListener(v -> finish());

        binding.btnPrint.setOnClickListener(v -> {
            Intent intent = new Intent(this, PrintActivity.class);
            intent.putExtra("data", orderModel);
            startActivity(intent);
        });

    }

    public void setItemData(OrderModel.PrescriptionDetailsFk prescriptionDetailsFk) {


    }
}