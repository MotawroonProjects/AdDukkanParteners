package com.addukkanpartener.uis.activity_home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.addukkanpartener.models.NotificationCountModel;
import com.addukkanpartener.models.UserModel;
import com.addukkanpartener.preferences.Preferences;
import com.addukkanpartener.remote.Api;
import com.addukkanpartener.tags.Tags;
import com.addukkanpartener.uis.activity_add_prescription.PrescriptionActivity;
import com.addukkanpartener.uis.activity_home.HomeActivity;
import com.addukkanpartener.uis.activity_notification.NotificationActivity;

import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHome extends Fragment {
    private FragmentHomeBinding binding;
    private HomeActivity activity;

    private Preferences preferences;
    private UserModel userModel;
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
        binding.setNotCount("0");
        preferences= Preferences.getInstance();
        userModel=preferences.getUserData(activity);
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
        if(userModel!=null){
            getNotificationCount();
        }
        binding.flNotifcation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userModel == null) {
                    activity.navigateToLoginActivity();
                } else {
                    binding.setNotCount("0");
                    Intent intent = new Intent(activity, NotificationActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
    private void getNotificationCount() {
        if (userModel == null) {
            binding.setNotCount("0");

            return;
        }
        Api.getService(Tags.base_url).getNotificationCount(userModel.getData().getToken(), userModel.getData().getId())
                .enqueue(new Callback<NotificationCountModel>() {
                    @Override
                    public void onResponse(Call<NotificationCountModel> call, Response<NotificationCountModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                binding.setNotCount(String.valueOf(response.body().getData().getCount()));
                            }
                        } else {

                            try {
                                Log.e("error", response.code() + "_" + response.errorBody().string());

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationCountModel> call, Throwable t) {
                        try {

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                            }


                        } catch (Exception e) {
                        }

                    }
                });
    }

}
