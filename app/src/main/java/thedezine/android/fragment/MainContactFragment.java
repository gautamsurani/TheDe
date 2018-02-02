package thedezine.android.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import thedezine.android.R;
import thedezine.android.utils.Global;



public class MainContactFragment extends Fragment {

    private ViewPager viewPager;
    AddressFragment addressFragment;
    ContactFragment contactFragment;
    MapFragment mapFragment;
    TabLayout tabLayout;
    Global global;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        global = new Global(getActivity());
        viewPager = (ViewPager) view.findViewById(R.id.pagerContact);
        tabLayout = (TabLayout) view.findViewById(R.id.tabsContact);

        View view1=getActivity().getLayoutInflater().inflate(R.layout.custom_tab, null);
        View view2=getActivity().getLayoutInflater().inflate(R.layout.custom_tab, null);
        View view3=getActivity().getLayoutInflater().inflate(R.layout.custom_tab, null);

        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.tab_address_circle);
        view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.tab_contact_circle);
        view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.tab_map_circle);

        setupPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setCustomView(view1);
        tabLayout.getTabAt(1).setCustomView(view2);
        tabLayout.getTabAt(2).setCustomView(view3);

    }

    private void setupPager(ViewPager viewPager) {

       ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

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

        }

    }

    @Override
    public void onResume() {
        super.onResume();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Contact Us");
        }
    }
}
