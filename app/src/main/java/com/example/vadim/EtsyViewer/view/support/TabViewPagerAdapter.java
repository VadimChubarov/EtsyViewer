package com.example.vadim.EtsyViewer.view.support;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vadim.EtsyViewer.R;

import java.util.ArrayList;
import java.util.List;

public class TabViewPagerAdapter extends FragmentPagerAdapter
{
    private Context targetContext;
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();
    private final List<Integer> fragmentImageResList = new ArrayList<>();

    public TabViewPagerAdapter(FragmentManager fragmentManager, Context targetContext) {
        super(fragmentManager);
        this.targetContext = targetContext;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }

    public void addFragment(Fragment fragment, String title, int imageResourse) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
        fragmentImageResList.add(imageResourse);
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(targetContext).inflate(R.layout.main_tab_layout, null);
        TextView tabTitle = view.findViewById(R.id.tab_layout_title);
        tabTitle.setText(fragmentTitleList.get(position));
        ImageView tabImage =  view.findViewById(R.id.tab_layout_image);
        tabImage.setImageResource(fragmentImageResList.get(position));
        return view;
    }

}
