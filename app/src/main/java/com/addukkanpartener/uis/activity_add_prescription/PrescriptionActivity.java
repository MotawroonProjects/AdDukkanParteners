package com.addukkanpartener.uis.activity_add_prescription;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;

import com.addukkanpartener.R;
import com.addukkanpartener.databinding.ActivityLoginBinding;
import com.addukkanpartener.databinding.ActivityPrescriptionBinding;
import com.addukkanpartener.language.Language;
import com.addukkanpartener.models.LoginModel;
import com.addukkanpartener.uis.activity_prescription_details1.PrescriptionDetails1Activity;
import com.addukkanpartener.uis.activity_sign_up.SignUpActivity;

import io.paperdb.Paper;

public class PrescriptionActivity extends AppCompatActivity {

    private ActivityPrescriptionBinding binding;
    private String lang = "ar";
    private int amount = 1;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_prescription);
        initView();
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setAmount("1");
        binding.llBack.setOnClickListener(v -> finish());

        binding.imageIncrease.setOnClickListener(v -> {
            amount += 1;
            binding.setAmount(String.valueOf(amount));
        });

        binding.imageDecrease.setOnClickListener(v -> {
            if (amount > 1) {
                amount -= 1;
                binding.setAmount(String.valueOf(amount));
            }

        });

        binding.llAdd.setOnClickListener(v -> {
            amount = 1;
            binding.setAmount(String.valueOf(amount));
            binding.tvAutoComplete.setText(null);

            Intent intent = new Intent(this, PrescriptionDetails1Activity.class);
            startActivity(intent);
        });

    }
}