package com.addukkanpartener.uis.activity_home.fragments.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.addukkanpartener.R;
import com.addukkanpartener.adapters.PagerAdapter;
import com.addukkanpartener.databinding.FragmentProfileBinding;
import com.addukkanpartener.uis.activity_home.HomeActivity;
import com.addukkanpartener.uis.activity_home.fragments.FragmentMore;
import com.addukkanpartener.uis.activity_home.fragments.profile.fragmentchild.FragmentPatients;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class FragmentProfile extends Fragment {
    private FragmentProfileBinding binding;
    private HomeActivity activity;
    private String lang = "ar";
    private PagerAdapter pagerAdapter;


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
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        binding.tab.setupWithViewPager(binding.pager);
        pagerAdapter = new PagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragment(getFragments());
        pagerAdapter.addTitle(getTitles());
        binding.pager.setAdapter(pagerAdapter);
        View tab_item1 = LayoutInflater.from(activity).inflate(R.layout.tab_custom_view, null);
        binding.tab.getTabAt(0).setCustomView(tab_item1);


    }
    private List<Fragment> getFragments() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(FragmentPatients.newInstance());
        fragmentList.add(FragmentMore.newInstance());


        return fragmentList;

    }

    private List<String> getTitles() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.patients));
        titles.add(getString(R.string.Selected_treatments));

        return titles;

    }


}
