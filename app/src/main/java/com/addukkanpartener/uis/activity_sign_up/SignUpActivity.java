package com.addukkanpartener.uis.activity_sign_up;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.addukkanpartener.R;
import com.addukkanpartener.adapters.CountriesAdapter;
import com.addukkanpartener.adapters.SpinnerCountryAdapter;
import com.addukkanpartener.adapters.SpinnerSpecialAdapter;
import com.addukkanpartener.databinding.ActivitySignUpBinding;
import com.addukkanpartener.databinding.DialogCountriesBinding;
import com.addukkanpartener.language.Language;
import com.addukkanpartener.models.CountryDataModel;
import com.addukkanpartener.models.CountryModel;
import com.addukkanpartener.models.PlaceGeocodeData;
import com.addukkanpartener.models.PlaceMapDetailsData;
import com.addukkanpartener.models.SignUpModel;
import com.addukkanpartener.models.SpecialDataModel;
import com.addukkanpartener.models.SpecialModel;
import com.addukkanpartener.models.UserModel;
import com.addukkanpartener.preferences.Preferences;
import com.addukkanpartener.remote.Api;
import com.addukkanpartener.share.Common;
import com.addukkanpartener.tags.Tags;
import com.addukkanpartener.uis.FragmentMapTouchListener;
import com.addukkanpartener.uis.activity_login.LoginActivity;
import com.addukkanpartener.uis.activity_verification_code.VerificationCodeActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private ActivitySignUpBinding binding;
    private SignUpModel signUpModel;
    private String lang = "ar";
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int READ_REQ = 1, CAMERA_REQ = 2;
    private Uri uri;

    private double lat = 0.0, lng = 0.0;
    private String address = "";
    private GoogleMap mMap;
    private Marker marker;
    private float zoom = 15.0f;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private final String fineLocPerm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int loc_req = 1225;
    private FragmentMapTouchListener fragment;
    private List<CountryModel> countryModelList, countryModels;
    private List<SpecialModel> specialModelList;
    private ProgressDialog dialog;
    private AlertDialog dialog2;
    private UserModel userModel;
    private Preferences preferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        initView();
    }

    private void initView() {
        specialModelList = new ArrayList<>();
        countryModelList = new ArrayList<>();
        countryModels = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.tvLogin.setText(Html.fromHtml(getString(R.string.sign_up_text)));
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        signUpModel = new SignUpModel();

        if (userModel!=null){
            String url = Tags.IMAGE_URL+userModel.getData().getLogo();
            Picasso.get().load(Uri.parse(url)).placeholder(R.drawable.ic_avatar).into(binding.image);
            signUpModel.setAddress(userModel.getData().getAddress());
            signUpModel.setCenter(userModel.getData().getHospital_place());
            signUpModel.setName(userModel.getData().getName());
            signUpModel.setPhone_code(userModel.getData().getPhone_code());
            signUpModel.setPhone(userModel.getData().getPhone());
            signUpModel.setEmail(userModel.getData().getEmail());
            signUpModel.setPassword("123456");
            signUpModel.setLat(userModel.getData().getLatitude());
            signUpModel.setLng(userModel.getData().getLongitude());
            signUpModel.setCv(userModel.getData().getAbout_user());
            signUpModel.setCountry_id(userModel.getData().getCountry_code());
            signUpModel.setSpecialize(userModel.getData().getSpecialization_id());
            binding.tvLogin.setVisibility(View.GONE);
            binding.tiPassword.setVisibility(View.GONE);
            binding.btnSignUp.setText(getString(R.string.update));
            binding.tvCode.setText(signUpModel.getPhone_code());
            binding.tvTitle.setText(R.string.update_profile);
        }

        binding.setModel(signUpModel);
        binding.btnSignUp.setOnClickListener(v -> {
            if (signUpModel.isDataValid(this)) {


                if (userModel==null){
                    if (uri == null) {
                        signUpModel.setImage("");
                    } else {
                        signUpModel.setImage(uri.toString());
                    }
                    navigatetoVerficationCode();

                }else {
                    if (uri == null) {
                        signUpModel.setImage("");
                        updateProfileWithoutImage();
                    } else {
                        signUpModel.setImage(uri.toString());
                        updateWithImage();
                    }

                }
            }
        });
        binding.spcountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    signUpModel.setPhone_code(countryModelList.get(i).getPhone_code());
                    signUpModel.setCountry_id(countryModelList.get(i).getCode());
                    binding.tvCode.setText(countryModelList.get(i).getPhone_code());

                } else {
                    binding.tvCode.setText("+966");
                    signUpModel.setPhone_code("+966");

                    signUpModel.setCountry_id("");
                }
                binding.setModel(signUpModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spspecial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    signUpModel.setSpecialize(specialModelList.get(i).getId());
                } else {
                    signUpModel.setSpecialize(0);
                }
                binding.setModel(signUpModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.tvLogin.setOnClickListener(v -> navigateToLoginActivity());

        binding.llBack.setOnClickListener(v -> onBackPressed());


        binding.fl.setOnClickListener(view -> openSheet());
        binding.flGallery.setOnClickListener(view -> {
            closeSheet();
            checkReadPermission();
        });

        binding.flCamera.setOnClickListener(view -> {
            closeSheet();
            checkCameraPermission();
        });

        binding.btnCancel.setOnClickListener(view -> closeSheet());
        binding.arrow.setOnClickListener(v -> dialog2.show());
        updateUI();
        getCountries();

    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigatetoVerficationCode() {
        Intent intent = new Intent(SignUpActivity.this, VerificationCodeActivity.class);
        intent.putExtra("data", signUpModel);
        startActivity(intent);
        finish();
    }


    private void createCountriesDialog() {

        dialog2 = new AlertDialog.Builder(this).create();
        CountriesAdapter countriesAdapter = new CountriesAdapter(countryModels, this);

        DialogCountriesBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_countries, null, false);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(countriesAdapter);

        dialog2.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog2.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setView(binding.getRoot());
    }

    private void sortCountries() {
        Collections.sort(countryModelList, (country1, country2) -> {
            return country1.getCountry_setting_trans_fk().getTitle().trim().compareToIgnoreCase(country2.getCountry_setting_trans_fk().getTitle().trim());
        });
    }


    private void getCountries() {
        dialog = Common.createProgressDialog(this, this.getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .getCountries(lang)
                .enqueue(new Callback<CountryDataModel>() {
                    @Override
                    public void onResponse(Call<CountryDataModel> call, Response<CountryDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        binding.arrow.setVisibility(View.VISIBLE);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData() != null) {
                                    if (response.body().getData().size() > 0) {
                                        updateCountryData(response.body().getData());
                                    } else {

                                    }
                                }
                            } else {
                                Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {
                            binding.progBar.setVisibility(View.GONE);
                            binding.arrow.setVisibility(View.VISIBLE);

                            switch (response.code()) {
                                case 500:
                                    Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                            binding.arrow.setVisibility(View.VISIBLE);

                            binding.progBar.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });

    }

    private void updateCountryData(List<CountryModel> data) {
        countryModelList.clear();

        countryModels.addAll(data);


        sortCountries();
        createCountriesDialog();
        countryModelList.clear();

        CountryModel countryModel = new CountryModel();
        countryModel.setCountry_setting_trans_fk(new CountryModel.CountrySettingTransFk(getResources().getString(R.string.choose_country)));
        countryModelList.add(countryModel);
        countryModelList.addAll(data);
        SpinnerCountryAdapter spinnerCountryAdapter = new SpinnerCountryAdapter(countryModelList, this);
        binding.spcountry.setAdapter(spinnerCountryAdapter);
        getSpecilal();

    }

    private void getSpecilal() {

        Api.getService(Tags.base_url)
                .getSpecial(lang)
                .enqueue(new Callback<SpecialDataModel>() {
                    @Override
                    public void onResponse(Call<SpecialDataModel> call, Response<SpecialDataModel> response) {
                        dialog.dismiss();

                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData() != null) {
                                    if (response.body().getData().size() > 0) {
                                        updateSpecialData(response.body().getData());
                                    }
                                }
                            } else {
                                Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {
                            dialog.dismiss();

                            switch (response.code()) {
                                case 500:
                                    Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            try {
                                Log.e("error_code", response.code() + "_");
                            } catch (NullPointerException e) {

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<SpecialDataModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });

    }

    private void updateSpecialData(List<SpecialModel> data) {
        specialModelList.clear();
        SpecialModel specialModel = new SpecialModel();
        specialModel.setSpecialization_trans_fk(new SpecialModel.SpecializationTransFk(getResources().getString(R.string.choose_special)));
        specialModelList.add(specialModel);
        specialModelList.addAll(data);
        SpinnerSpecialAdapter spinnerSpecialAdapter = new SpinnerSpecialAdapter(specialModelList, this);
        binding.spspecial.setAdapter(spinnerSpecialAdapter);

        if (userModel!=null){
            int country_pos = getCountryPos();
            int specialize_pos = getSpecializePos();
            binding.spcountry.setSelection(country_pos);
            binding.spspecial.setSelection(specialize_pos);

        }

    }

    private int getCountryPos() {
        int pos = -1;
        for (int index = 0;index<countryModelList.size();index++){
            if (userModel.getData().getCountry_code().equals(countryModelList.get(index).getCode())){
                pos = index;
                return pos;
            }
        }
        return pos;
    }

    private int getSpecializePos() {
        int pos = -1;
        for (int index = 0;index<specialModelList.size();index++){
            if (userModel.getData().getSpecialization_id()==specialModelList.get(index).getId()){
                pos = index;
                return pos;
            }
        }
        return pos;
    }

    private void updateProfileWithoutImage() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .updateWithoutImage("Bearer "+userModel.getData().getToken(),userModel.getData().getId(),signUpModel.getName(), signUpModel.getPhone_code(), signUpModel.getPhone(), "android", signUpModel.getCountry_id(), signUpModel.getAddress(), signUpModel.getCenter(), signUpModel.getLat(), signUpModel.getLng(), signUpModel.getSpecialize(), signUpModel.getEmail(), signUpModel.getCv())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body() != null && response.body().getData() != null) {
                                    Preferences preferences = Preferences.getInstance();
                                    preferences.create_update_userdata(SignUpActivity.this, response.body());
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            }else if (response.body() != null && response.body().getStatus() == 409) {
                                Toast.makeText(SignUpActivity.this, R.string.ph_found, Toast.LENGTH_SHORT).show();

                            }


                        } else {
                            dialog.dismiss();

                            switch (response.code()) {
                                case 500:
                                    break;
                                default:
                                    break;
                            }
                            try {
                                Log.e("error_code", response.code() + "_");
                            } catch (NullPointerException e) {

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
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void updateWithImage() {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        RequestBody user_id_part = Common.getRequestBodyText(String.valueOf(userModel.getData().getId()));
        RequestBody name_part = Common.getRequestBodyText(signUpModel.getName());
        RequestBody email_part = Common.getRequestBodyText(signUpModel.getEmail());
        RequestBody password_part = Common.getRequestBodyText(signUpModel.getPassword());
        RequestBody phone_part = Common.getRequestBodyText(signUpModel.getPhone());
        RequestBody phone_code_part = Common.getRequestBodyText(signUpModel.getPhone_code());
        RequestBody address_part = Common.getRequestBodyText(signUpModel.getAddress());
        RequestBody lat_part = Common.getRequestBodyText(signUpModel.getLat() + "");
        RequestBody lng_part = Common.getRequestBodyText(signUpModel.getLng() + "");
        RequestBody soft_part = Common.getRequestBodyText("android");
        RequestBody cv_part = Common.getRequestBodyText(signUpModel.getCv());
        RequestBody center_part = Common.getRequestBodyText(signUpModel.getCenter());
        RequestBody country_part = Common.getRequestBodyText(signUpModel.getCountry_id());
        RequestBody special_part = Common.getRequestBodyText(signUpModel.getSpecialize() + "");


        MultipartBody.Part image = Common.getMultiPart(this, Uri.parse(signUpModel.getImage()), "logo");


        Api.getService(Tags.base_url)
                .updateWithImage("Bearer "+userModel.getData().getToken(),user_id_part,name_part, phone_code_part, phone_part, soft_part, country_part, address_part, center_part, lat_part, lng_part, special_part, email_part, cv_part, image)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body() != null && response.body().getData() != null) {
                                    Preferences preferences = Preferences.getInstance();
                                    preferences.create_update_userdata(SignUpActivity.this, response.body());
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            }else if (response.body() != null && response.body().getStatus() == 409) {
                                Toast.makeText(SignUpActivity.this, R.string.ph_found, Toast.LENGTH_SHORT).show();

                            }


                        } else {
                            dialog.dismiss();

                            switch (response.code()) {
                                case 500:
                                    break;
                                default:
                                    break;
                            }
                            try {
                                Log.e("error_code", response.code() + "_");
                            } catch (NullPointerException e) {

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                } else {
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }

    public void openSheet() {
        binding.expandLayout.setExpanded(true, true);
    }

    public void closeSheet() {
        binding.expandLayout.collapse(true);

    }

    public void checkReadPermission() {
        closeSheet();
        if (ActivityCompat.checkSelfPermission(this, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM}, READ_REQ);
        } else {
            SelectImage(READ_REQ);
        }
    }

    public void checkCameraPermission() {

        closeSheet();

        if (ContextCompat.checkSelfPermission(this, write_permission) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, camera_permission) == PackageManager.PERMISSION_GRANTED
        ) {
            SelectImage(CAMERA_REQ);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, CAMERA_REQ);
        }
    }

    private void SelectImage(int req) {

        Intent intent = new Intent();

        if (req == READ_REQ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent, req);

        } else if (req == CAMERA_REQ) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, req);
            } catch (SecurityException e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == loc_req) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
                initGoogleApi();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == READ_REQ) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == CAMERA_REQ) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            startLocationUpdate();
        } else if (requestCode == READ_REQ && resultCode == Activity.RESULT_OK && data != null) {

            uri = data.getData();
            File file = new File(Common.getImagePath(this, uri));
            Picasso.get().load(file).fit().into(binding.image);

        } else if (requestCode == CAMERA_REQ && resultCode == Activity.RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            uri = getUriFromBitmap(bitmap);
            if (uri != null) {
                String path = Common.getImagePath(this, uri);

                if (path != null) {
                    Picasso.get().load(new File(path)).fit().into(binding.image);

                } else {
                    Picasso.get().load(uri).fit().into(binding.image);

                }
            }


        }

    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "", ""));
    }


    private void getGeoData(final double lat, double lng) {
        String location = lat + "," + lng;
        Api.getService("https://maps.googleapis.com/maps/api/")
                .getGeoData(location, "ar", getString(R.string.map_api_key))
                .enqueue(new Callback<PlaceGeocodeData>() {
                    @Override
                    public void onResponse(Call<PlaceGeocodeData> call, Response<PlaceGeocodeData> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            if (response.body().getResults().size() > 0) {
                                address = response.body().getResults().get(0).getFormatted_address().replace("Unnamed Road,", "");
                                signUpModel.setLat(lat);
                                signUpModel.setLng(lng);
                                signUpModel.setAddress(address);

                            }
                        } else {

                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<PlaceGeocodeData> call, Throwable t) {
                        try {
                            Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, fineLocPerm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{fineLocPerm}, loc_req);
        } else {
            mMap.setMyLocationEnabled(true);
            initGoogleApi();
        }
    }

    private void initGoogleApi() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    private void updateUI() {

        fragment = (FragmentMapTouchListener) getSupportFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap != null) {
            mMap = googleMap;
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            fragment.setListener(() -> binding.scrollView.requestDisallowInterceptTouchEvent(true));
            if (userModel==null){
                checkPermission();

            }else {
                addMarker(new LatLng(userModel.getData().getLatitude(),userModel.getData().getLongitude()));
            }

            mMap.setOnMapClickListener(latLng -> {
                lat = latLng.latitude;
                lng = latLng.longitude;
                LatLng latLng2 = new LatLng(lat, lng);
                addMarker(latLng2);
                getGeoData(lat, lng);

            });


        }
    }

    private void addMarker(LatLng latLng) {
        signUpModel.setLat(latLng.latitude);
        signUpModel.setLng(latLng.longitude);
        if (marker == null) {
            marker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        } else {
            marker.setPosition(latLng);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        initLocationRequest();
    }

    private void initLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setFastestInterval(1000);
        locationRequest.setInterval(60000);
        LocationSettingsRequest.Builder request = new LocationSettingsRequest.Builder();
        request.addLocationRequest(locationRequest);
        request.setAlwaysShow(false);


        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, request.build());
        result.setResultCallback(locationSettingsResult -> {
            Status status = locationSettingsResult.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    startLocationUpdate();
                    break;

                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        status.startResolutionForResult(this, 100);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                    break;

            }
        });


    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        getGeoData(lat, lng);
        addMarker(new LatLng(lat, lng));
        if (googleApiClient != null) {
            LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
            googleApiClient.disconnect();
            googleApiClient = null;
        }
    }

    public void setItemData(CountryModel countryModel) {
        signUpModel.setPhone_code(countryModel.getPhone_code());
        binding.setModel(signUpModel);
        dialog2.dismiss();
        binding.tvCode.setText(countryModel.getPhone_code());
    }

    @Override
    public void onBackPressed() {
        if (userModel!=null){
            finish();
        }else {
            navigateToLoginActivity();
        }
    }
}