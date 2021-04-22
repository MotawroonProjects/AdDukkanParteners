package com.addukkanpartener.uis.activity_home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.addukkanpartener.R;
import com.addukkanpartener.databinding.ActivityHomeBinding;
import com.addukkanpartener.language.Language;
import com.addukkanpartener.preferences.Preferences;
import com.addukkanpartener.uis.activity_home.fragments.FragmentChat;
import com.addukkanpartener.uis.activity_home.fragments.FragmentHome;
import com.addukkanpartener.uis.activity_home.fragments.FragmentMore;
import com.addukkanpartener.uis.activity_home.fragments.profile.FragmentProfile;

import java.util.List;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private String lang = "";
    private Preferences preferences;
    private FragmentManager fragmentManager;
    private FragmentHome fragmentHome;
    private FragmentChat fragmentChat;
    private FragmentMore fragmentMore;
    private FragmentProfile fragmentProfile;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();
    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        updateCartCount(0);

        displayFragmentHome();
        binding.llHome.setOnClickListener(v -> displayFragmentHome());
        binding.llProfile.setOnClickListener(v -> displayFragmentProfile());
        binding.llChat.setOnClickListener(v -> displayFragmentChat());
        binding.llMore.setOnClickListener(v -> displayFragmentMore());
    }

    private void updateCartCount(int count) {
        binding.setNotCount(String.valueOf(count));
    }


    public void displayFragmentHome() {
        if (fragmentHome == null) {
            fragmentHome = FragmentHome.newInstance();

        }


        if (fragmentChat != null && fragmentChat.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentChat).commit();
        }

        if (fragmentMore != null && fragmentMore.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentMore).commit();
        }
        if (fragmentProfile != null && fragmentProfile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentProfile).commit();
        }

        if (fragmentHome.isAdded()) {
            fragmentManager.beginTransaction().show(fragmentHome).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentHome, "fragmentHome").commit();
        }

        updateNavUiHome();
    }

    private void displayFragmentChat() {
        if (fragmentChat == null) {
            fragmentChat = FragmentChat.newInstance();

        }


        if (fragmentHome != null && fragmentHome.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentHome).commit();
        }

        if (fragmentMore != null && fragmentMore.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentMore).commit();
        }
        if (fragmentProfile != null && fragmentProfile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentProfile).commit();
        }

        if (fragmentChat.isAdded()) {
            fragmentManager.beginTransaction().show(fragmentChat).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentChat, "fragment_chat").commit();
        }

        updateNavUiChat();
    }

    private void displayFragmentMore() {

        if (fragmentMore == null) {
            fragmentMore = FragmentMore.newInstance();

        }


        if (fragmentHome != null && fragmentHome.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentHome).commit();
        }

        if (fragmentChat != null && fragmentChat.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentChat).commit();
        }
        if (fragmentProfile != null && fragmentProfile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentProfile).commit();
        }

        if (fragmentMore.isAdded()) {
            fragmentManager.beginTransaction().show(fragmentMore).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentMore, "fragment_more").commit();
        }

        updateNavUiMore();
    }

    private void displayFragmentProfile() {
        if (fragmentProfile == null) {
            fragmentProfile = FragmentProfile.newInstance();

        }


        if (fragmentHome != null && fragmentHome.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentHome).commit();
        }

        if (fragmentChat != null && fragmentChat.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentChat).commit();
        }
        if (fragmentMore != null && fragmentMore.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentMore).commit();
        }

        if (fragmentProfile.isAdded()) {
            fragmentManager.beginTransaction().show(fragmentProfile).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentProfile, "fragmentProfile").commit();
        }
        updateNavUiProfile();
    }

    private void updateNavUiHome() {
        binding.iconHome.setSaturation(1.0f);
        binding.tvHome.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        binding.iconChat.setSaturation(0.0f);
        binding.tvChat.setTextColor(ContextCompat.getColor(this, R.color.gray9));

        binding.iconMore.setSaturation(0.0f);
        binding.tvMore.setTextColor(ContextCompat.getColor(this, R.color.gray9));

        binding.iconProfile.setSaturation(0.0f);
        binding.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.gray9));

    }


    private void updateNavUiChat() {
        binding.iconChat.setSaturation(1.0f);
        binding.tvChat.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        binding.iconHome.setSaturation(0.0f);
        binding.tvHome.setTextColor(ContextCompat.getColor(this, R.color.gray9));

        binding.iconMore.setSaturation(0.0f);
        binding.tvMore.setTextColor(ContextCompat.getColor(this, R.color.gray9));

        binding.iconProfile.setSaturation(0.0f);
        binding.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.gray9));

    }


    private void updateNavUiMore() {
        binding.iconMore.setSaturation(1.0f);
        binding.tvMore.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        binding.iconHome.setSaturation(0.0f);
        binding.tvHome.setTextColor(ContextCompat.getColor(this, R.color.gray9));

        binding.iconChat.setSaturation(0.0f);
        binding.tvChat.setTextColor(ContextCompat.getColor(this, R.color.gray9));

        binding.iconProfile.setSaturation(0.0f);
        binding.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.gray9));

    }

    private void updateNavUiProfile() {
        binding.iconProfile.setSaturation(1.0f);
        binding.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        binding.iconHome.setSaturation(0.0f);
        binding.tvHome.setTextColor(ContextCompat.getColor(this, R.color.gray9));

        binding.iconChat.setSaturation(0.0f);
        binding.tvChat.setTextColor(ContextCompat.getColor(this, R.color.gray9));

        binding.iconMore.setSaturation(0.0f);
        binding.tvMore.setTextColor(ContextCompat.getColor(this, R.color.gray9));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onBackPressed() {
        if (fragmentHome != null && fragmentHome.isAdded() && fragmentHome.isVisible()) {

            finish();
        } else {
            displayFragmentHome();
        }
    }
}