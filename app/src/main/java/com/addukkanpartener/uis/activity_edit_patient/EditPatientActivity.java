package com.addukkanpartener.uis.activity_edit_patient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.addukkanpartener.R;
import com.addukkanpartener.adapters.CountryAdapter2;
import com.addukkanpartener.databinding.ActivityEditPatientBinding;
import com.addukkanpartener.databinding.ActivityLoginBinding;
import com.addukkanpartener.databinding.ActivitySignUpBinding;
import com.addukkanpartener.databinding.DialogAlertBinding;
import com.addukkanpartener.databinding.DialogCountryBinding;
import com.addukkanpartener.language.Language;
import com.addukkanpartener.models.CountryDataModel;
import com.addukkanpartener.models.CountryModel;
import com.addukkanpartener.models.EditModel;
import com.addukkanpartener.models.SignUpModel;
import com.addukkanpartener.models.UserModel;
import com.addukkanpartener.preferences.Preferences;
import com.addukkanpartener.remote.Api;
import com.addukkanpartener.share.Common;
import com.addukkanpartener.tags.Tags;
import com.addukkanpartener.uis.activity_verification_code.VerificationCodeActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPatientActivity extends AppCompatActivity {
    private ActivityEditPatientBinding binding;
    private EditModel editModel;
    private String lang = "ar";
    private CountryAdapter2 adapter;
    private List<CountryModel> list;
    private UserModel userModel;
    private UserModel.User client;

    private Preferences preferences;
    private AlertDialog dialog;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int READ_REQ = 1, CAMERA_REQ = 2;
    private Uri uri = null;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_patient);
        getDataFromIntent();
        initView();
    }
    private void getDataFromIntent() {
        Intent intent = getIntent();
        client = (UserModel.User) intent.getSerializableExtra("data");
    }
    private void initView() {
        list = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        editModel = new EditModel();
        binding.setModel(editModel);
        binding.btnEditPatient.setOnClickListener(v -> {
            if (editModel.isDataValid(this)) {


                    updateProfile();

            }
        });

        if (userModel!=null){
            editModel.setPhone_code(client.getPhone_code());
            editModel.setPhone(client.getPhone());
            editModel.setName(client.getName());
            editModel.setEmail(client.getEmail());
//            editModel.setCountry_code(client.getUser_country().getCode());
            editModel.setPassword("123456");
            binding.setModel(editModel);
            binding.tvPhonCode.setText(client.getPhone_code());
        }




        binding.llBack.setOnClickListener(v -> finish());

        binding.arrow.setOnClickListener(v -> createDialogAlert());

        getCountries();
    }




    private void getCountries() {

        Api.getService(Tags.base_url)
                .getCountries(lang)
                .enqueue(new Callback<CountryDataModel>() {
                    @Override
                    public void onResponse(Call<CountryDataModel> call, Response<CountryDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData() != null) {
                                    if (response.body().getData().size() > 0) {
                                        list.clear();
                                        list.addAll(response.body().getData());

                                        binding.arrow.setVisibility(View.VISIBLE);
                                    } else {
                                        binding.arrow.setVisibility(View.GONE);

                                    }
                                }
                            } else {
                                Toast.makeText(EditPatientActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {
                            binding.progBar.setVisibility(View.GONE);

                            switch (response.code()) {
                                case 500:
                                    Toast.makeText(EditPatientActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(EditPatientActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            try {
                                Log.e("error_code", response.code() + "_");
                            } catch (NullPointerException e) {

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<CountryDataModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(EditPatientActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(EditPatientActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                            Log.e("ddd",e.getMessage()+"__");
                        }
                    }
                });

    }






    private void updateProfile() {

        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Log.e("vvvvv",client.getId()+"____");
        Log.e("vvvvv",userModel.getData().getId()+"____");


        Api.getService(Tags.base_url)
                .editPatient("Bearer "+userModel.getData().getToken(),client.getId(),editModel.getName(),editModel.getEmail(),editModel.getPhone_code(),editModel.getPhone(),editModel.getPassword(),"android",editModel.getCountry_code(),userModel.getData().getId())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {

                            if (response.body()!=null&&response.body().getStatus()==200){
                                if (response.body() != null&&response.body().getData()!=null){
                                setResult(RESULT_OK);
                                    finish();
                                }
                            }else if (response.body()!=null&&response.body().getStatus()==404){
                                Toast.makeText(EditPatientActivity.this, R.string.user_not_found, Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(EditPatientActivity.this,getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {
                            dialog.dismiss();

                            switch (response.code()){
                                case 500:
                                    Toast.makeText(EditPatientActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(EditPatientActivity.this,getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            try {
                                Log.e("error_code",response.code()+"_");
                            } catch (NullPointerException e){

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(EditPatientActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                }
                                else if (t.getMessage().toLowerCase().contains("socket")||t.getMessage().toLowerCase().contains("canceled")){ }
                                else {
                                    Toast.makeText(EditPatientActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });



    }

    public void setItemCountry(CountryModel model) {
        binding.tvPhonCode.setText(model.getPhone_code());

        editModel.setPhone_code(model.getPhone_code());
        editModel.setCountry_code(model.getCode());
        dialog.dismiss();
    }

    private  void createDialogAlert() {
        dialog = new AlertDialog.Builder(this)
                .create();

        DialogCountryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_country, null, false);
        if (adapter==null){
            adapter = new CountryAdapter2(this,list);
            binding.recView.setLayoutManager(new LinearLayoutManager(this));
            binding.recView.setAdapter(adapter);

        }
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }



}