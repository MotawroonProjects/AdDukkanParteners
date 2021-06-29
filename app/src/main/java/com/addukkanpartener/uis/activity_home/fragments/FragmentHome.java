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
import com.addukkanpartener.models.ChartDataModel;
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
import java.util.List;

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
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        binding.setModel(userModel);


        binding.llPrescription.setOnClickListener(v -> {
            Intent intent = new Intent(activity, PrescriptionActivity.class);
            startActivity(intent);
        });
        if (userModel != null) {
            getNotificationCount();
        }
        binding.flNotifcation.setOnClickListener(v -> {
            if (userModel == null) {
                activity.navigateToLoginActivity();
            } else {
                binding.setNotCount("0");
                Intent intent = new Intent(activity, NotificationActivity.class);
                startActivity(intent);
            }
        });
        getCharts();

    }

    private void getCharts() {
        Api.getService(Tags.base_url).getCharts(userModel.getData().getToken(), userModel.getData().getId())
                .enqueue(new Callback<ChartDataModel>() {
                    @Override
                    public void onResponse(Call<ChartDataModel> call, Response<ChartDataModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                updateChart(response.body().getData());
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
                    public void onFailure(Call<ChartDataModel> call, Throwable t) {
                        try {

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                            }


                        } catch (Exception e) {
                        }

                    }
                });
    }

    private void updateChart(ChartDataModel.Data data) {
        ValueLineSeries series = new ValueLineSeries();
        series.setColor(ContextCompat.getColor(activity, R.color.color3));

        for (ChartDataModel.Chart chart : data.getChart()) {
            if (isMonthIn(data.getChart(), "Jan")) {
                series.addPoint(new ValueLinePoint(chart.getMonth(), (float) chart.getClients()));

            }else{
                series.addPoint(new ValueLinePoint("Jan",0.0f));

            }
            if (isMonthIn(data.getChart(), "Feb")) {
                series.addPoint(new ValueLinePoint(chart.getMonth(), (float) chart.getClients()));

            }else{
                series.addPoint(new ValueLinePoint("Feb",0.0f));

            }

            if (isMonthIn(data.getChart(), "Mar")) {
                series.addPoint(new ValueLinePoint(chart.getMonth(), (float) chart.getClients()));

            }else {
                series.addPoint(new ValueLinePoint("Mar",0.0f));

            }
            if (isMonthIn(data.getChart(), "Apr")) {
                series.addPoint(new ValueLinePoint(chart.getMonth(), (float) chart.getClients()));

            }else{

                series.addPoint(new ValueLinePoint("Apr",0.0f));

            }
            if (isMonthIn(data.getChart(), "Mai")) {
                series.addPoint(new ValueLinePoint(chart.getMonth(), (float) chart.getClients()));

            }else{
                series.addPoint(new ValueLinePoint("Mai",0.0f));

            }
            if (isMonthIn(data.getChart(), "Jun")) {
                series.addPoint(new ValueLinePoint(chart.getMonth(), (float) chart.getClients()));

            }else{
                series.addPoint(new ValueLinePoint("Jun",0.0f));

            }
            if (isMonthIn(data.getChart(), "Jul")) {
                series.addPoint(new ValueLinePoint(chart.getMonth(), (float) chart.getClients()));

            }else{

                series.addPoint(new ValueLinePoint("Jul",0.0f));

            }

            if (isMonthIn(data.getChart(), "Aug")) {
                series.addPoint(new ValueLinePoint(chart.getMonth(), (float) chart.getClients()));

            }else{
                series.addPoint(new ValueLinePoint("Aug",0.0f));

            }
            if (isMonthIn(data.getChart(), "Sep")) {
                series.addPoint(new ValueLinePoint(chart.getMonth(), (float) chart.getClients()));

            }else{
                series.addPoint(new ValueLinePoint("Sep",0.0f));

            }
            if (isMonthIn(data.getChart(), "Oct")) {
                series.addPoint(new ValueLinePoint(chart.getMonth(), (float) chart.getClients()));

            }else{
                series.addPoint(new ValueLinePoint("Oct",0.0f));

            }
            if (isMonthIn(data.getChart(), "Nov")) {
                series.addPoint(new ValueLinePoint(chart.getMonth(), (float) chart.getClients()));

            }else{
                series.addPoint(new ValueLinePoint("Nov",0.0f));

            } if (isMonthIn(data.getChart(), "Dec")) {
                series.addPoint(new ValueLinePoint(chart.getMonth(), (float) chart.getClients()));

            }else {
                series.addPoint(new ValueLinePoint("Dec",0.0f));

            }
        }


        binding.cubiclinechart.addSeries(series);
        binding.cubiclinechart.startAnimation();

        binding.tvRate.setText(data.getPercentage_purchase_prescriptions() + " %");
        binding.progressRate.setProgress((int) data.getPercentage_purchase_prescriptions());
    }

    private boolean isMonthIn(List<ChartDataModel.Chart> charts, String month) {
        boolean state = false;
        for (ChartDataModel.Chart chart : charts) {
            if (chart.getMonth().equals(month)) {
                state = true;
                return state;
            }
        }
        return state;
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
