package com.addukkanpartener.uis.activity_home.fragments.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.TextViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.addukkanpartener.R;
import com.addukkanpartener.adapters.PagerAdapter;
import com.addukkanpartener.databinding.FragmentProfileBinding;
import com.addukkanpartener.models.UserModel;
import com.addukkanpartener.preferences.Preferences;
import com.addukkanpartener.uis.activity_home.HomeActivity;
import com.addukkanpartener.uis.activity_home.fragments.FragmentMore;
import com.addukkanpartener.uis.activity_home.fragments.profile.fragmentchild.FragmentMyPrescriptions;
import com.addukkanpartener.uis.activity_home.fragments.profile.fragmentchild.FragmentPatients;
import com.addukkanpartener.uis.activity_home.fragments.profile.fragmentchild.FragmentSelectedTreatments;
import com.addukkanpartener.uis.activity_sign_up.SignUpActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class FragmentProfile extends Fragment {
    private FragmentProfileBinding binding;
    private HomeActivity activity;
    private String lang = "ar";
    private PagerAdapter pagerAdapter;
    private TextView tvCount;
    private Preferences preferences;
    private UserModel userModel;

    public static FragmentProfile newInstance() {
        return new FragmentProfile();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        binding.setModel(userModel);
        binding.tab.setupWithViewPager(binding.pager);
        pagerAdapter = new PagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragment(getFragments());
        pagerAdapter.addTitle(getTitles());
        binding.pager.setAdapter(pagerAdapter);
        View tab_item1 = LayoutInflater.from(activity).inflate(R.layout.tab_custom_view, null);
        tvCount = tab_item1.findViewById(R.id.tvCount);
        binding.tab.getTabAt(0).setCustomView(tab_item1);

        binding.editProfile.setOnClickListener(view -> {
            Intent intent = new Intent(activity, SignUpActivity.class);
            startActivityForResult(intent,100);
        });

        binding.pager.setOffscreenPageLimit(getFragments().size());
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(FragmentPatients.newInstance());
        fragmentList.add(FragmentSelectedTreatments.newInstance());
        fragmentList.add(FragmentMyPrescriptions.newInstance());


        return fragmentList;

    }

    private List<String> getTitles() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.patients));
        titles.add(getString(R.string.selected_treatments));
        titles.add(getString(R.string.my_pres));

        return titles;

    }


    public void updatecount(int count) {
        //  View tab_item1 = binding.tab.getTabAt(0).getCustomView();
        tvCount.setText(count + "");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode== Activity.RESULT_OK){
            userModel = preferences.getUserData(activity);
            binding.setModel(userModel);

        }
    }
}
