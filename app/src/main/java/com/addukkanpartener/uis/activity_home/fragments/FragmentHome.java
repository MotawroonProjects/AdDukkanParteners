package com.addukkanpartener.uis.activity_home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.addukkanpartener.R;
import com.addukkanpartener.databinding.FragmentHomeBinding;
import com.addukkanpartener.uis.activity_add_prescription.PrescriptionActivity;
import com.addukkanpartener.uis.activity_home.HomeActivity;

import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

public class FragmentHome extends Fragment {
    private FragmentHomeBinding binding;
    private HomeActivity activity;


    public static FragmentHome newInstance() {
        return new FragmentHome();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        ValueLineSeries series = new ValueLineSeries();
        series.setColor(ContextCompat.getColor(activity,R.color.color3));

        series.addPoint(new ValueLinePoint("Jan", 2.4f));
        series.addPoint(new ValueLinePoint("Feb", 3.4f));
        series.addPoint(new ValueLinePoint("Mar", .4f));
        series.addPoint(new ValueLinePoint("Apr", 1.2f));
        series.addPoint(new ValueLinePoint("Mai", 2.6f));
        series.addPoint(new ValueLinePoint("Jun", 1.0f));
        series.addPoint(new ValueLinePoint("Jul", 3.5f));
        series.addPoint(new ValueLinePoint("Aug", 2.4f));
        series.addPoint(new ValueLinePoint("Sep", 2.4f));
        series.addPoint(new ValueLinePoint("Oct", 3.4f));
        series.addPoint(new ValueLinePoint("Nov", .4f));
        series.addPoint(new ValueLinePoint("Dec", 1.3f));

        binding.cubiclinechart.addSeries(series);
        binding.cubiclinechart.startAnimation();

        binding.llPrescription.setOnClickListener(v -> {
            Intent intent = new Intent(activity, PrescriptionActivity.class);
            startActivity(intent);
        });
    }


}
