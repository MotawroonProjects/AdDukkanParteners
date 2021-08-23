package com.addukkanpartener.uis.activity_login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.addukkanpartener.R;
import com.addukkanpartener.databinding.ActivityLoginBinding;
import com.addukkanpartener.language.Language;
import com.addukkanpartener.models.LoginModel;
import com.addukkanpartener.models.UserModel;
import com.addukkanpartener.preferences.Preferences;
import com.addukkanpartener.remote.Api;
import com.addukkanpartener.share.Common;
import com.addukkanpartener.tags.Tags;
import com.addukkanpartener.uis.activity_approved.ApprovedActivity;
import com.addukkanpartener.uis.activity_home.HomeActivity;
import com.addukkanpartener.uis.activity_sign_up.SignUpActivity;
import com.addukkanpartener.uis.activity_verification_code.VerificationCodeActivity;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginModel loginModel;
    private String lang = "ar";

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initView();
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.tvSignUp.setText(Html.fromHtml(getString(R.string.don_t_have_account_sign_up)));
        loginModel = new LoginModel();
        binding.setModel(loginModel);
        binding.btnLogin.setOnClickListener(v -> {
            if (loginModel.isDataValid(this)) {
                login();
            }
        });

        binding.tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });


    }

    private void login() {

        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .login(loginModel.getPhone(),loginModel.getPassword())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {

                            if (response.body()!=null&&response.body().getStatus()==200){
                                if (response.body() != null&&response.body().getData()!=null){
                                    if (response.body().getData().getApproved_status().equals("new")){
                                        navigateToApprove();
                                    }else if (response.body().getData().getApproved_status().equals("accepted")){
                                        Preferences preferences = Preferences.getInstance();
                                        preferences.create_update_userdata(LoginActivity.this,response.body());
                                        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Toast.makeText(LoginActivity.this, R.string.not_approved, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }else if (response.body()!=null&&response.body().getStatus()==404){
                                Toast.makeText(LoginActivity.this, R.string.user_not_found, Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(LoginActivity.this,getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {
                            dialog.dismiss();

                            switch (response.code()){
                                case 500:
                                    Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(LoginActivity.this,getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(LoginActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                }
                                else if (t.getMessage().toLowerCase().contains("socket")||t.getMessage().toLowerCase().contains("canceled")){ }
                                else {
                                    Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });


    }

    private void navigateToApprove() {
        Intent intent = new Intent(this, ApprovedActivity.class);
        startActivity(intent);
        finish();
    }
}