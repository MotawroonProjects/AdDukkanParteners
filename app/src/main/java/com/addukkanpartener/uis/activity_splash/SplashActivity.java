package com.addukkanpartener.uis.activity_splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.addukkanpartener.R;
import com.addukkanpartener.databinding.ActivitySplashBinding;
import com.addukkanpartener.language.Language;
import com.addukkanpartener.models.AppLocalSettings;
import com.addukkanpartener.models.UserModel;
import com.addukkanpartener.preferences.Preferences;
import com.addukkanpartener.uis.activity_home.HomeActivity;
import com.addukkanpartener.uis.activity_language.LanguageActivity;
import com.addukkanpartener.uis.activity_login.LoginActivity;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        initView();
    }

    private void initView() {

        checkData();


    }

    private void checkData() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Preferences preferences = Preferences.getInstance();
            UserModel userModel = preferences.getUserData(this);
            AppLocalSettings settings = preferences.isLanguageSelected(this);

            Intent intent;

            if (settings == null) {
                intent = new Intent(this, LanguageActivity.class);
                intent.putExtra("data", true);

                startActivityForResult(intent, 100);

            } else {
                if (settings.isLanguageSelected()) {
                   if (userModel==null){
                       intent = new Intent(this, LoginActivity.class);
                   }else {
                       intent = new Intent(this, HomeActivity.class);
                   }
                    startActivity(intent);
                    finish();

                } else {
                    intent = new Intent(this, LanguageActivity.class);
                    intent.putExtra("data", true);
                    startActivityForResult(intent, 100);

                }
            }


        }, 2000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            String lang = data.getStringExtra("lang");
            refreshActivity(lang);
        }
    }

    public void refreshActivity(String lang) {

        Preferences preferences = Preferences.getInstance();
        AppLocalSettings settings = preferences.isLanguageSelected(this);
        if (settings == null) {
            settings = new AppLocalSettings();

        }
        settings.setLanguageSelected(true);
        preferences.setIsLanguageSelected(this, settings);
        Paper.init(this);
        Paper.book().write("lang", lang);
        Language.updateResources(this, lang);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


}