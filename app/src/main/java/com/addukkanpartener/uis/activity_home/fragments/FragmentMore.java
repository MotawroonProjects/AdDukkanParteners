package com.addukkanpartener.uis.activity_home.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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

import com.addukkanpartener.R;
import com.addukkanpartener.databinding.FragmentMoreBinding;
import com.addukkanpartener.models.RoomDataModel;
import com.addukkanpartener.models.SettingDataModel;
import com.addukkanpartener.models.UserModel;
import com.addukkanpartener.preferences.Preferences;
import com.addukkanpartener.remote.Api;
import com.addukkanpartener.share.Common;
import com.addukkanpartener.tags.Tags;
import com.addukkanpartener.uis.activity_contact_us.ContactUsActivity;
import com.addukkanpartener.uis.activity_home.HomeActivity;
import com.addukkanpartener.uis.activity_language.LanguageActivity;
import com.addukkanpartener.uis.activity_view.ViewActivity;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentMore extends Fragment {
    private FragmentMoreBinding binding;
    private HomeActivity activity;
    private String lang;
    private SettingDataModel.Settings settings;
    private Preferences preferences;
    private UserModel userModel;

    public static FragmentMore newInstance() {
        return new FragmentMore();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        getSetting();
        binding.llChangeLanguage.setOnClickListener(v -> {
            Intent intent = new Intent(activity, LanguageActivity.class);
            startActivityForResult(intent, 100);
        });

        binding.llReport.setOnClickListener(v -> {
            if (settings != null) {
                String url = Tags.base_url+lang+"/charts?doctor_id="+userModel.getData().getId()+"&view_type=webView";
                navigateToWebView(url);


            } else {
                Toast.makeText(activity, R.string.not_ava, Toast.LENGTH_SHORT).show();
            }
        });

        binding.llAboutApp.setOnClickListener(v -> {
            if (settings != null) {
                String url = settings.getAbout_us_link();
                if (!url.isEmpty()) {
                    navigateToWebView(url);
                }

            } else {
                Toast.makeText(activity, R.string.not_ava, Toast.LENGTH_SHORT).show();
            }
        });
        binding.llContactUs.setOnClickListener(v -> {
            Intent intent = new Intent(activity, ContactUsActivity.class);
            startActivity(intent);
        });
        binding.llTerms.setOnClickListener(v -> {
            if (settings != null) {
                String url = settings.getTerms_link();
                if (!url.isEmpty()) {
                    navigateToWebView(url);
                }

            } else {
                Toast.makeText(activity, R.string.not_ava, Toast.LENGTH_SHORT).show();
            }
        });

        binding.imageFacebook.setOnClickListener(v -> {
            if (settings != null && !settings.getFacebook().isEmpty()) {
                String url = settings.getFacebook();
                openSocialMedia(url);


            } else {
                Toast.makeText(activity, R.string.not_ava, Toast.LENGTH_SHORT).show();
            }
        });

        binding.imageInstagram.setOnClickListener(v -> {
            if (settings != null && !settings.getInstagram().isEmpty()) {
                String url = settings.getInstagram();
                openSocialMedia(url);


            } else {
                Toast.makeText(activity, R.string.not_ava, Toast.LENGTH_SHORT).show();
            }
        });

        binding.imageTwitter.setOnClickListener(v -> {
            if (settings != null && !settings.getTwitter().isEmpty()) {
                String url = settings.getTwitter();
                openSocialMedia(url);


            } else {
                Toast.makeText(activity, R.string.not_ava, Toast.LENGTH_SHORT).show();
            }
        });

        binding.imageWhatsApp.setOnClickListener(v -> {
            if (settings != null && !settings.getWhatup().isEmpty()) {
                String number = settings.getWhatup();
                openWhatsApp(number);


            } else {
                Toast.makeText(activity, R.string.not_ava, Toast.LENGTH_SHORT).show();
            }
        });

        binding.llLogout.setOnClickListener(v -> {
            activity.logout();
        });
    }

    private void getSetting() {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .getSetting(lang)
                .enqueue(new Callback<SettingDataModel>() {
                    @Override
                    public void onResponse(Call<SettingDataModel> call, Response<SettingDataModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData() != null) {
                                    settings = response.body().getData();
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
                    public void onFailure(Call<SettingDataModel> call, Throwable t) {
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

    private void navigateToWebView(String url) {
        Intent intent = new Intent(activity, ViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    private void openSocialMedia(String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void openWhatsApp(String number) {
        if (!number.startsWith("+")) {
            number = "+" + number;
        }


        String url = "https://api.whatsapp.com/send?phone=" + number;

        try {
            PackageManager packageManager = activity.getPackageManager();
            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(activity, "WhatsApp not found", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            String lang = data.getStringExtra("lang");
            activity.refreshActivity(lang);
        }
    }
}
