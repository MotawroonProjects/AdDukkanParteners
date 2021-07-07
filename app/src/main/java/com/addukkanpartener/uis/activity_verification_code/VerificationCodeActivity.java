package com.addukkanpartener.uis.activity_verification_code;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;


import com.addukkanpartener.R;
import com.addukkanpartener.databinding.ActivityVerificationCodeBinding;
import com.addukkanpartener.language.Language;
import com.addukkanpartener.models.SignUpModel;
import com.addukkanpartener.models.UserModel;
import com.addukkanpartener.preferences.Preferences;
import com.addukkanpartener.remote.Api;
import com.addukkanpartener.share.Common;
import com.addukkanpartener.tags.Tags;
import com.addukkanpartener.uis.activity_home.HomeActivity;
import com.addukkanpartener.uis.activity_sign_up.SignUpActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationCodeActivity extends AppCompatActivity {
    private ActivityVerificationCodeBinding binding;
    private boolean canSend = false;
    private CountDownTimer countDownTimer;
    private FirebaseAuth mAuth;
    private String verificationId;
    private String smsCode = "";
    private SignUpModel signUpModel;
    private String lang;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verification_code);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            signUpModel = (SignUpModel) intent.getSerializableExtra("data");


        }
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        mAuth = FirebaseAuth.getInstance();
        String mPhone = signUpModel.getPhone_code() + signUpModel.getPhone();
        binding.setPhone(mPhone);

        binding.btnConfirm.setOnClickListener(v -> {
            String sms = binding.edtCode.getText().toString().trim();
            if (!sms.isEmpty()) {
                checkValidCode(sms);

            } else {
                binding.edtCode.setError(getString(R.string.inv_code));
            }
        });
        binding.btnResendCode.setOnClickListener(view -> {
            if (canSend) {
                canSend = false;
                resendCode();

            }
        });
        sendSmsCode();
        //onSuccessCode();
    }


    public void sendSmsCode() {
        startCounter();

        mAuth.setLanguageCode("en");

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                smsCode = phoneAuthCredential.getSmsCode();
                checkValidCode(smsCode);
            }

            @Override
            public void onCodeSent(@NonNull String verification_id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verification_id, forceResendingToken);
                VerificationCodeActivity.this.verificationId = verification_id;
                Log.e("verification_id", verification_id);
            }


            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                if (e.getMessage() != null) {
                    onCodeFailed(e.getMessage());
                } else {
                    onCodeFailed(getString(R.string.failed));

                }
            }
        };
        PhoneAuthProvider.getInstance()
                .verifyPhoneNumber(
                        signUpModel.getPhone_code() + signUpModel.getPhone(),
                        120,
                        TimeUnit.SECONDS,
                        this,
                        mCallBack

                );
    }

    public void checkValidCode(String code) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        if (verificationId != null) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            mAuth.signInWithCredential(credential)
                    .addOnSuccessListener(authResult -> {
                        dialog.dismiss();
                        onSuccessCode();
                    }).addOnFailureListener(e -> {
                dialog.dismiss();
                if (e.getMessage() != null) {
                    try {
                        onCodeFailed(e.getMessage());

                    } catch (Exception ex) {

                    }
                } else {
                    onCodeFailed(getString(R.string.failed));

                }
            });
        }

    }


    private void startCounter() {
        countDownTimer = new CountDownTimer(120000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) ((millisUntilFinished / 1000) / 60);
                int seconds = (int) ((millisUntilFinished / 1000) % 60);

                String time = String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds);
                onCounterStarted(time);
                //binding.btnResendCode.setText(time);

            }

            @Override
            public void onFinish() {
                onCounterFinished();
                binding.btnResendCode.setText(getResources().getString(R.string.resend2));


            }
        };

        countDownTimer.start();
    }

    public void resendCode() {
        if (countDownTimer != null) {
            countDownTimer.start();
        }
        sendSmsCode();
    }

    public void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }


    public void onSuccessCode() {

        if (signUpModel.getImage().isEmpty()) {
            signUpWithoutImage();
        } else {
            signUpWithImage();
        }
    }


    private void navigateToHomeActivty() {
        Intent intent = new Intent(VerificationCodeActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
    private void signUpWithoutImage() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .signUp(signUpModel.getName(), signUpModel.getPhone_code(), signUpModel.getPhone(), signUpModel.getPassword(), "android", signUpModel.getCountry_id(), signUpModel.getAddress(), signUpModel.getCenter(), signUpModel.getLat(), signUpModel.getLng(), signUpModel.getSpecialize(), signUpModel.getEmail(), signUpModel.getCv())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body() != null && response.body().getData() != null) {
                                    Preferences preferences = Preferences.getInstance();
                                    preferences.create_update_userdata(VerificationCodeActivity.this, response.body());
                                    navigateToHomeActivty();
                                }
                            } else if (response.body() != null && response.body().getStatus() == 404) {
                                Toast.makeText(VerificationCodeActivity.this, R.string.user_not_found, Toast.LENGTH_SHORT).show();

                            }else if (response.body() != null && response.body().getStatus() == 409) {
                                Toast.makeText(VerificationCodeActivity.this, R.string.ph_found, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {
                            dialog.dismiss();

                            switch (response.code()) {
                                case 500:
                                    Toast.makeText(VerificationCodeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(VerificationCodeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(VerificationCodeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void signUpWithImage() {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
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
                .signUpwithImage(name_part, phone_code_part, phone_part, password_part, soft_part, country_part, address_part, center_part, lat_part, lng_part, special_part, email_part, cv_part, image)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body() != null && response.body().getData() != null) {
                                    Preferences preferences = Preferences.getInstance();
                                    preferences.create_update_userdata(VerificationCodeActivity.this, response.body());
                                    navigateToHomeActivty();
                                }
                            } else if (response.body() != null && response.body().getStatus() == 404) {
                                Toast.makeText(VerificationCodeActivity.this, R.string.user_not_found, Toast.LENGTH_SHORT).show();

                            }else if (response.body() != null && response.body().getStatus() == 409) {
                                Toast.makeText(VerificationCodeActivity.this, R.string.ph_found, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {
                            dialog.dismiss();

                            switch (response.code()) {
                                case 500:
                                    Toast.makeText(VerificationCodeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(VerificationCodeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(VerificationCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }

    public void onCounterStarted(String time) {
        binding.tvResend.setText(R.string.resend);
        binding.btnResendCode.setText(String.format(Locale.ENGLISH, "%s", time));
        binding.btnResendCode.setTextColor(ContextCompat.getColor(VerificationCodeActivity.this, R.color.colorPrimary));
        binding.btnResendCode.setBackgroundResource(R.color.transparent);
    }

    public void onCounterFinished() {
        canSend = true;
        binding.tvResend.setText("");

        binding.btnResendCode.setText(R.string.resend2);
        binding.btnResendCode.setTextColor(ContextCompat.getColor(VerificationCodeActivity.this, R.color.gray6));
        binding.btnResendCode.setBackgroundResource(R.color.white);
    }

    public void onCodeFailed(String msg) {
        Common.CreateDialogAlert(this, msg);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }
}