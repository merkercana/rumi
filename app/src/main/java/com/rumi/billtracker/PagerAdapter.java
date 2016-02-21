package com.rumi.billtracker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.rumi.billtracker.BillFragment;
import com.rumi.billtracker.MemberFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 2016-02-21.
 */

public class PagerAdapter extends FragmentStatePagerAdapter{
    public PagerAdapter(FragmentManager fm){
        super(fm);
    }

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
