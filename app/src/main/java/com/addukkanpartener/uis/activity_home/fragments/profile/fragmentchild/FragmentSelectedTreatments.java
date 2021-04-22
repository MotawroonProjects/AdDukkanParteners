package com.addukkanpartener.uis.activity_home.fragments.profile.fragmentchild;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.addukkanpartener.R;
import com.addukkanpartener.adapters.PatientsAdapter;
import com.addukkanpartener.adapters.TreatmentsAdapter;
import com.addukkanpartener.databinding.FragmentPatientsBinding;
import com.addukkanpartener.databinding.FragmentSelectedTreatmentsBinding;
import com.addukkanpartener.uis.activity_company.ChooseCompanyActivity;
import com.addukkanpartener.uis.activity_home.HomeActivity;
import com.addukkanpartener.uis.activity_treatments.ChooseTreatmentActivity;

public class FragmentSelectedTreatments extends Fragment {
    private FragmentSelectedTreatmentsBinding binding;
    private HomeActivity activity;


    public static FragmentSelectedTreatments newInstance() {
        return new FragmentSelectedTreatments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_selected_treatments, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        binding.recView.setLayoutManager(new GridLayoutManager(activity,3));
        binding.recView.setAdapter(new TreatmentsAdapter(activity));
        binding.progBar.setVisibility(View.GONE);
binding.llchoosetreatment.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(activity, ChooseTreatmentActivity.class);
        startActivity(intent);
    }
});
        binding.llchossecompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, ChooseCompanyActivity.class);
                startActivity(intent);
            }
        });
    }


}
