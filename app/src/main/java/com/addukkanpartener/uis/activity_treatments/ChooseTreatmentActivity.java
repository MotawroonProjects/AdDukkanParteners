package com.addukkanpartener.uis.activity_treatments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.addukkanpartener.R;
import com.addukkanpartener.adapters.ChooseAdapter;
import com.addukkanpartener.databinding.ActivityChooseBinding;
import com.addukkanpartener.interfaces.Listeners;
import com.addukkanpartener.language.Language;

import io.paperdb.Paper;

public class ChooseTreatmentActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityChooseBinding binding;
    private String lang;

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
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.setTitle(getResources().getString(R.string.choose_treatment));
        binding.progBar.setVisibility(View.GONE);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(new ChooseAdapter(this));

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
}
