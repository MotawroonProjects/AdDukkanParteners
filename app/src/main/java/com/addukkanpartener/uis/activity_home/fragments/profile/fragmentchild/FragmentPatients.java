package com.addukkanpartener.uis.activity_home.fragments.profile.fragmentchild;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.addukkanpartener.R;
import com.addukkanpartener.adapters.PatientsAdapter;
import com.addukkanpartener.databinding.FragmentPatientsBinding;
import com.addukkanpartener.uis.activity_home.HomeActivity;

public class FragmentPatients extends Fragment {
    private FragmentPatientsBinding binding;
    private HomeActivity activity;


    public static FragmentPatients newInstance() {
        return new FragmentPatients();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_patients, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(new PatientsAdapter(activity));

    }


}
