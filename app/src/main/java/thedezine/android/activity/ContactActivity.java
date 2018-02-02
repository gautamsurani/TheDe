package thedezine.android.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import thedezine.android.R;
import thedezine.android.fragment.AddressFragment;
import thedezine.android.fragment.ContactFragment;
import thedezine.android.fragment.MapFragment;
import thedezine.android.utils.Global;
import thedezine.android.utils.Tools;



public class ContactActivity extends AppCompatActivity {

    private ViewPager viewPager;

    AddressFragment addressFragment;
    ContactFragment contactFragment;
    MapFragment mapFragment;
    TabLayout tabLayout;
    ProgressDialog progressDialog;
    Global global;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_view);
        viewPager = (ViewPager) findViewById(R.id.pagerContact);
        tabLayout = (TabLayout) findViewById(R.id.tabsContact);
        global = new Global(this);
        initToolbar();
        Tools.systemBarLolipop(this);

        View view1 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.tab_address_circle);

        View view2 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.tab_contact_circle);

        View view3 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.tab_map_circle);

        setupPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setCustomView(view1);
        tabLayout.getTabAt(1).setCustomView(view2);
        tabLayout.getTabAt(2).setCustomView(view3);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


      //  tabLayout.addTab(tabLayout.newTab().setCustomView(view1));


      //  tabLayout.getTabAt(0).setIcon(R.drawable.tab_back_circle);
       // tabLayout.getTabAt(1).setIcon(R.drawable.tab_contact);
      //  tabLayout.getTabAt(2).setIcon(R.drawable.tab_map);

    }

    private void setupPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        if (addressFragment == null) {
            addressFragment = new AddressFragment();
        }
        if (contactFragment == null) {
            contactFragment = new ContactFragment();
        }
        if (mapFragment == null) {
            mapFragment = new MapFragment();
        }
        adapter.addFragment(addressFragment, "");
        adapter.addFragment(contactFragment, "");
        adapter.addFragment(mapFragment, "");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        adapter.notifyDataSetChanged();
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Contact Us");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }


}
