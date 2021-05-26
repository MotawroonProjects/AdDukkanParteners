package com.addukkanpartener.uis.activity_home.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.addukkanpartener.R;
import com.addukkanpartener.databinding.FragmentMoreBinding;
import com.addukkanpartener.uis.activity_home.HomeActivity;
import com.addukkanpartener.uis.activity_language.LanguageActivity;


public class FragmentMore extends Fragment {
    private FragmentMoreBinding binding;
    private HomeActivity activity;


    public static FragmentMore newInstance() {
        return new FragmentMore();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        binding.llChangeLanguage.setOnClickListener(v -> {
            Intent intent = new Intent(activity, LanguageActivity.class);
            startActivityForResult(intent,100);
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode== Activity.RESULT_OK&&data!=null){
            String lang = data.getStringExtra("lang");
            activity.refreshActivity(lang);
        }
    }
}
