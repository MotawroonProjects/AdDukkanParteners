package com.addukkanpartener.uis.activity_home.fragments.profile.fragmentchild;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.addukkanpartener.R;
import com.addukkanpartener.adapters.DoctorTreatmentAdapter;
import com.addukkanpartener.databinding.FragmentSelectedTreatmentsBinding;
import com.addukkanpartener.models.CompanyDataModel;
import com.addukkanpartener.models.CompanyModel;
import com.addukkanpartener.models.DoctorTreatmentDataModel;
import com.addukkanpartener.models.DoctorTreatmentModel;
import com.addukkanpartener.models.ResponseModel;
import com.addukkanpartener.models.TreatmentModel;
import com.addukkanpartener.models.UserModel;
import com.addukkanpartener.preferences.Preferences;
import com.addukkanpartener.remote.Api;
import com.addukkanpartener.share.Common;
import com.addukkanpartener.tags.Tags;
import com.addukkanpartener.uis.activity_company.ChooseCompanyActivity;
import com.addukkanpartener.uis.activity_home.HomeActivity;
import com.addukkanpartener.uis.activity_treatments.ChooseTreatmentActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentSelectedTreatments extends Fragment {
    private FragmentSelectedTreatmentsBinding binding;
    private HomeActivity activity;
    private CompanyModel companyModel;
    private TreatmentModel treatmentModel;
    private List<DoctorTreatmentModel> list;
    private DoctorTreatmentAdapter adapter;
    private Preferences preferences;
    private UserModel userModel;
    private String lang = "ar";



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
        list = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        binding.recView.setLayoutManager(new GridLayoutManager(activity, 2));
        adapter = new DoctorTreatmentAdapter(activity,list,this);
        binding.recView.setAdapter(adapter);
        binding.llchoosetreatment.setOnClickListener(v -> {
            if (companyModel!=null){
                Intent intent = new Intent(activity, ChooseTreatmentActivity.class);
                intent.putExtra("data", companyModel.getId());
                startActivityForResult(intent,200);
            }else {
                Toast.makeText(activity, R.string.choose_company, Toast.LENGTH_SHORT).show();
            }

        });
        binding.llchossecompany.setOnClickListener(v -> {
            Intent intent = new Intent(activity, ChooseCompanyActivity.class);
            startActivityForResult(intent,100);
        });

        getData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode== Activity.RESULT_OK&&data!=null){
            companyModel = (CompanyModel) data.getSerializableExtra("data");
            binding.tvCompany.setText(companyModel.getProduct_company_trans_fk().getTitle());
        }else if (requestCode==200&&resultCode== Activity.RESULT_OK&&data!=null){
             treatmentModel = (TreatmentModel) data.getSerializableExtra("data");
             binding.tvTreatment.setText(treatmentModel.getProduct_trans_fk().getTitle());
             getData();

        }
    }



    private void getData() {

        list.clear();
        adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);
        binding.tvNoData.setVisibility(View.GONE);

        Api.getService(Tags.base_url)
                .getMyTreatment("Bearer "+userModel.getData().getToken(),lang,userModel.getData().getId(),"")
                .enqueue(new Callback<DoctorTreatmentDataModel>() {
                    @Override
                    public void onResponse(Call<DoctorTreatmentDataModel> call, Response<DoctorTreatmentDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData() != null) {
                                    if (response.body().getData().size() > 0) {
                                        binding.tvNoData.setVisibility(View.GONE);
                                        list.clear();
                                        list.addAll(response.body().getData());
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        binding.tvNoData.setVisibility(View.VISIBLE);

                                    }
                                }
                            } else {
                                //    Toast.makeText(CountryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {
                            binding.progBar.setVisibility(View.GONE);

                            switch (response.code()) {
                                case 500:
                                    //  Toast.makeText(CountryActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    // Toast.makeText(CountryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            try {
                                Log.e("error_code", response.code() + "_");
                            } catch (NullPointerException e) {

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<DoctorTreatmentDataModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    //Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    //Toast.makeText(CountryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });

    }

    public void delete(DoctorTreatmentModel doctorTreatmentModel, int adapterPosition) {
        ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .deleteTreatment("Bearer "+userModel.getData().getToken(),userModel.getData().getId(),doctorTreatmentModel.getProduct_id())
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                list.remove(adapterPosition);
                                adapter.notifyItemRemoved(adapterPosition);
                                if (list.size()==0){
                                    binding.tvNoData.setVisibility(View.VISIBLE);
                                }
                            } else {
                                //    Toast.makeText(CountryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {
                            dialog.dismiss();

                            switch (response.code()) {
                                case 500:
                                    //  Toast.makeText(CountryActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    // Toast.makeText(CountryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            try {
                                Log.e("error_code", response.code() + "_");
                            } catch (NullPointerException e) {

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    //Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    //Toast.makeText(CountryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });
    }
}
