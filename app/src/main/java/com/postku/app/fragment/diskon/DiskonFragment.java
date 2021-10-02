package com.postku.app.fragment.diskon;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.postku.app.R;
import com.postku.app.fragment.product.TabPagerAdapter;

public class DiskonFragment extends Fragment {
    private Context c;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public DiskonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diskon, container, false);
        c = getActivity();
        setupView(view);
        return view;
    }
    private void setupView(View view) {
        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tabs);
        TabDiskonAdapter adapter = new TabDiskonAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


    }
}