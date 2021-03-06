package com.addukkanpartener.uis.activity_add_prescription;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.addukkanpartener.R;
import com.addukkanpartener.adapters.ClientAdapter;
import com.addukkanpartener.adapters.PrescriptionAdapter;
import com.addukkanpartener.adapters.PrescriptionItemAdapter;
import com.addukkanpartener.adapters.SpinnerClientAdapter;
import com.addukkanpartener.adapters.SpinnerCountryAdapter;
import com.addukkanpartener.databinding.ActivityPrescriptionBinding;
import com.addukkanpartener.databinding.ClientDialogBinding;
import com.addukkanpartener.language.Language;
import com.addukkanpartener.models.AddPrescriptionModel;
import com.addukkanpartener.models.AllUserModel;
import com.addukkanpartener.models.CountryDataModel;
import com.addukkanpartener.models.CountryModel;
import com.addukkanpartener.models.DoctorTreatmentDataModel;
import com.addukkanpartener.models.DoctorTreatmentModel;
import com.addukkanpartener.models.SignUpModel;
import com.addukkanpartener.models.SingleOrderDataModel;
import com.addukkanpartener.models.TreatmentDataModel;
import com.addukkanpartener.models.TreatmentModel;
import com.addukkanpartener.models.UserModel;
import com.addukkanpartener.preferences.Preferences;
import com.addukkanpartener.remote.Api;
import com.addukkanpartener.share.Common;
import com.addukkanpartener.tags.Tags;
import com.addukkanpartener.uis.activity_prescription_details1.PrescriptionDetails1Activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.os.Build.VERSION_CODES.KITKAT;

public class PrescriptionActivity extends AppCompatActivity {

    private ActivityPrescriptionBinding binding;
    private String lang = "ar";
    private int amount = 1;
    private List<DoctorTreatmentModel> list;
    private Call<TreatmentDataModel> call;
    private UserModel userModel;
    private Preferences preferences;
    private PrescriptionAdapter prescriptionAdapter;
    private DoctorTreatmentModel selectedTreatment;
    private List<UserModel.User> clientList;
    private List<CountryModel> countryModelList;
    private SpinnerCountryAdapter countriesAdapter;
    private AlertDialog dialog;
    private SignUpModel signUpModel;
    private PrescriptionItemAdapter prescriptionItemAdapter;
    private AddPrescriptionModel addPrescriptionModel;
    private ClientAdapter clientAdapter;
    private Call<AllUserModel> searchCall;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_prescription);
        initView();
    }

    private void initView() {
        countryModelList = new ArrayList<>();
        clientList = new ArrayList<>();
        list = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setAmount("1");
        addPrescriptionModel = new AddPrescriptionModel();
        binding.setModel(addPrescriptionModel);
        signUpModel = new SignUpModel();
        CountryModel countryModel = new CountryModel();
        countryModel.setCountry_setting_trans_fk(new CountryModel.CountrySettingTransFk(getString(R.string.choose_country)));
        countryModelList.add(countryModel);
        prescriptionAdapter = new PrescriptionAdapter(this, R.layout.spinner_row, list);

        binding.tvAutoComplete.setAdapter(prescriptionAdapter);
        binding.tvAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    getTreatments(s.toString());

                } else {
                    selectedTreatment = null;
                    amount = 1;
                    binding.setAmount(String.valueOf(amount));
                }
            }
        });
        binding.tvAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            selectedTreatment = (DoctorTreatmentModel) parent.getItemAtPosition(position);
            binding.tvAutoComplete.setText(selectedTreatment.getProduct_data().getProduct_trans_fk().getTitle());

        });

        clientAdapter = new ClientAdapter(this, clientList);
        binding.recViewClient.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewClient.setAdapter(clientAdapter);
        binding.tvClient.setText(getString(R.string.ch_client));


        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        prescriptionItemAdapter = new PrescriptionItemAdapter(this, addPrescriptionModel.getProducts_list());
        binding.recView.setAdapter(prescriptionItemAdapter);


        binding.llBack.setOnClickListener(v -> finish());

        binding.imageIncrease.setOnClickListener(v -> {
            if (selectedTreatment != null) {
                amount += 1;
                binding.setAmount(String.valueOf(amount));
            }

        });

        binding.imageDecrease.setOnClickListener(v -> {

            if (selectedTreatment != null) {
                if (amount > 1) {
                    amount -= 1;
                    binding.setAmount(String.valueOf(amount));
                }
            }


        });

        binding.llAdd.setOnClickListener(v -> {
            if (selectedTreatment != null) {
                AddPrescriptionModel.ItemModel itemModel = new AddPrescriptionModel.ItemModel(selectedTreatment.getProduct_id(), amount, selectedTreatment.getProduct_data().getProduct_trans_fk().getTitle(), selectedTreatment.getProduct_data().getProduct_default_price().getPrice());
                addPrescriptionModel.addItem(itemModel);
                prescriptionItemAdapter.notifyDataSetChanged();
                binding.setTotal(addPrescriptionModel.getTotal());
                selectedTreatment = null;
                binding.tvAutoComplete.setText("");
                amount = 1;
                binding.setAmount(String.valueOf(amount));


            }


        });

        binding.tvClient.setOnClickListener(v -> {
            binding.llSearch.setVisibility(View.VISIBLE);

        });
        binding.btnConfirm.setOnClickListener(v -> {
            if (addPrescriptionModel.isDataValid(this)) {
                addPrescriptionModel.setDoctor_id(userModel.getData().getId());
                addPrescriptionModel.setCountry_code(userModel.getData().getCountry_code());

                addOrder();
            }
        });


        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = "all";
                if (!s.toString().isEmpty()) {
                    binding.imageClear.setVisibility(View.VISIBLE);
                    query = s.toString();
                } else {
                    binding.imageClear.setVisibility(View.GONE);

                }

                getPatients(query);

            }
        });
        binding.imageClear.setOnClickListener(v -> {
            binding.edtSearch.setText(null);
        });
        binding.btnAddClient.setOnClickListener(v -> dialog.show());
        getCountries();
        getTreatments("all");
        getPatients("all");
        createCountryDialogAlert();

    }


    private void addOrder() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Log.e("ccccccc", addPrescriptionModel.getProducts_list().get(0).getProduct_id()+"___");
        Api.getService(Tags.base_url)
                .addOrder("Bearer " + userModel.getData().getToken(), addPrescriptionModel)
                .enqueue(new Callback<SingleOrderDataModel>() {
                    @Override
                    public void onResponse(Call<SingleOrderDataModel> call, Response<SingleOrderDataModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                Intent intent = new Intent(PrescriptionActivity.this, PrescriptionDetails1Activity.class);
                                intent.putExtra("data", response.body().getData().getId());
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            dialog.dismiss();

                            try {
                                Log.e("mmmmmmmmmm", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                // Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("mmmmmmmmmm", response.code() + "");

                                // Toast.makeText(LoginActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SingleOrderDataModel> call, Throwable t) {
                        try {
                            dialog.dismiss();

                            if (t.getMessage() != null) {
                                Log.e("error", t.toString() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(PrescriptionActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    // Toast.makeText(LoginActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void getCountries() {
        Api.getService(Tags.base_url)
                .getCountries(lang)
                .enqueue(new Callback<CountryDataModel>() {
                    @Override
                    public void onResponse(Call<CountryDataModel> call, Response<CountryDataModel> response) {
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData() != null) {
                                    if (response.body().getData().size() > 0) {
                                        countryModelList.addAll(response.body().getData());
                                        if (countriesAdapter != null) {
                                            runOnUiThread(() -> countriesAdapter.notifyDataSetChanged());

                                        }
                                    } else {

                                    }
                                }
                            } else {
                                Toast.makeText(PrescriptionActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {

                            try {
                                Log.e("error_code", response.code() + "_");
                            } catch (NullPointerException e) {

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<CountryDataModel> call, Throwable t) {
                        try {

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });

    }

    private void getPatients(String query) {
        clientList.clear();
        clientAdapter.notifyDataSetChanged();

        if (searchCall!=null){
            searchCall.cancel();
        }
        searchCall = Api.getService(Tags.base_url).getPatient(query, userModel.getData().getId() + "", "off");

        searchCall.enqueue(new Callback<AllUserModel>() {
            @Override
            public void onResponse(Call<AllUserModel> call, Response<AllUserModel> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null && response.body().getStatus() == 200) {
                        if (response.body().getData() != null) {
                            if (response.body().getData().size() > 0) {
                                clientList.addAll(response.body().getData());
                                clientAdapter.notifyDataSetChanged();

                            }
                        }
                    } else {
                        //    Toast.makeText(CountryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                    }


                } else {

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
            public void onFailure(Call<AllUserModel> call, Throwable t) {
                try {
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


    private void getTreatments(String query) {

        if (call != null) {
            call.cancel();
        }


        Api.getService(Tags.base_url)
                .getMyTreatment("Bearer " + userModel.getData().getToken(), lang, userModel.getData().getId(), query)
                .enqueue(new Callback<DoctorTreatmentDataModel>() {
                    @Override
                    public void onResponse(Call<DoctorTreatmentDataModel> call, Response<DoctorTreatmentDataModel> response) {
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData() != null) {
                                    if (response.body().getData().size() > 0) {
                                        list.clear();
                                        list.addAll(response.body().getData());
                                        runOnUiThread(() -> prescriptionAdapter.notifyDataSetChanged());

                                    }
                                }
                            } else {
                                //    Toast.makeText(CountryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {

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

    private void createCountryDialogAlert() {
        dialog = new AlertDialog.Builder(this)
                .create();

        ClientDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.client_dialog, null, false);
        countriesAdapter = new SpinnerCountryAdapter(countryModelList, this);
        binding.spinnerCountry.setAdapter(countriesAdapter);
        binding.imageClose.setOnClickListener(v -> dialog.dismiss()

        );
        binding.spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryModel countryModel = countryModelList.get(position);
                signUpModel.setCountry_id(String.valueOf(countryModel.getCode()));
                signUpModel.setPhone_code(countryModel.getPhone_code());
                binding.setModel(signUpModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.setModel(signUpModel);
        binding.btnAdd.setOnClickListener(v -> {
            if (signUpModel.isData2Valid(this)) {
                addClient(signUpModel, binding);
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());

    }

    private void addClient(SignUpModel model, ClientDialogBinding binding) {
        String password = getRandomPassword(6);
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .addClient("Bearer " + userModel.getData().getToken(), userModel.getData().getId() + "", model.getName(), model.getPhone_code(), model.getPhone(), password, model.getEmail(), model.getCountry_id())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        Log.e("code", response.body().getStatus() + "__");
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                binding.setModel(new SignUpModel());
                                clientList.add(0, response.body().getData());
                                clientAdapter.notifyDataSetChanged();

                            } else if (response.body().getStatus() == 409) {
                                dialog.dismiss();
                                getPatients("all");
                            }
                        } else {
                            dialog.dismiss();

                            try {
                                Log.e("mmmmmmmmmm", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                // Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("mmmmmmmmmm", response.code() + "");

                                // Toast.makeText(LoginActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();

                            if (t.getMessage() != null) {
                                Log.e("error", t.toString() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(PrescriptionActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    // Toast.makeText(LoginActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }

    private String getRandomPassword(int length) {
        String num = "0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(num.charAt(rnd.nextInt(num.length())));
        return sb.toString();
    }


    public void updateItem(int adapterPosition, AddPrescriptionModel.ItemModel model) {
        addPrescriptionModel.addItem(model);
        binding.setTotal(addPrescriptionModel.getTotal());
    }

    public void deleteItem(AddPrescriptionModel.ItemModel model) {
        addPrescriptionModel.deleteItem(model);
        binding.setTotal(addPrescriptionModel.getTotal());

    }

    @Override
    public void onBackPressed() {
        if (binding.llSearch.getVisibility() == View.VISIBLE) {
            binding.llSearch.setVisibility(View.GONE);
            binding.edtSearch.setText(null);
        } else {
            super.onBackPressed();
        }

    }

    public void setClientItem(UserModel.User user) {
        binding.llSearch.setVisibility(View.GONE);
        binding.tvClient.setText(user.getName());
        addPrescriptionModel.setClient_id(user.getId());

    }
}