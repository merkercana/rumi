package com.rumi.billtracker;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GroupActivity extends AppCompatActivity {

    ViewPager pager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        pager = (ViewPager)findViewById(R.id.view_pager);
        setupViewPager(pager);

        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager);



    }


    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BillFragment(), "Bills");
        adapter.addFragment(new MemberFragment(), "Members");
        viewPager.setAdapter(adapter);
    }
}
