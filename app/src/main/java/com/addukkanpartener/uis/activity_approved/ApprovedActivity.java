package com.addukkanpartener.uis.activity_approved;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.addukkanpartener.R;
import com.addukkanpartener.databinding.ActivityApprovedBinding;
import com.addukkanpartener.databinding.ActivitySplashBinding;
import com.addukkanpartener.language.Language;
import com.bumptech.glide.Glide;

import io.paperdb.Paper;

public class ApprovedActivity extends AppCompatActivity {
    private ActivityApprovedBinding binding;
    private String lang;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_approved);
        initView();
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        Glide.with(this).asGif().load(R.drawable.wait).into(binding.image);

        binding.cardBack.setOnClickListener(v -> {
            finish();
        });
    }
}