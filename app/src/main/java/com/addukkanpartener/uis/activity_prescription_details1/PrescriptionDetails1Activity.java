package com.addukkanpartener.uis.activity_prescription_details1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;

import com.addukkanpartener.R;
import com.addukkanpartener.databinding.ActivityLoginBinding;
import com.addukkanpartener.databinding.ActivityPrescriptionDetails1Binding;
import com.addukkanpartener.language.Language;
import com.addukkanpartener.models.LoginModel;
import com.addukkanpartener.uis.activity_print.PrintActivity;
import com.addukkanpartener.uis.activity_sign_up.SignUpActivity;

import io.paperdb.Paper;

public class PrescriptionDetails1Activity extends AppCompatActivity {

    private ActivityPrescriptionDetails1Binding binding;
    private String lang = "ar";

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_prescription_details1);
        initView();
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.llBack.setOnClickListener(v -> finish());

        binding.btnPrint.setOnClickListener(v -> {
            Intent intent = new Intent(this, PrintActivity.class);
            startActivity(intent);
        });

    }
}