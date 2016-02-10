package com.srinivas.mudavath.newsaggregator;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mudavath Srinivas on 08-02-2016.
 */
public class TabFeedsFragment extends Fragment {

    Context mContext;

    public String channel;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String[] tablist;
    private ArrayList<Fragment> fragmentslist;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext=getActivity();
        View v=inflater.inflate(R.layout.fragment_tab_feeds, container, false);

        channel = getArguments().getString("channel");
        if(TextUtils.isEmpty(channel)){
            getActivity().getFragmentManager().popBackStack();
        }

        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        Bundle bundle = new Bundle();
        FeedsFragment fragment;


        if (viewPager != null) {
            fragmentslist = new ArrayList<Fragment>();
            switch (channel){
                case "The Hindu":
                    tablist = new String[]{"International", "National","Sports"};
                    fragment = new FeedsFragment();
                    bundle = new Bundle();
                    bundle.putString("url", Util.HINDU_INTERNATIONAL_URL);
                    fragment.setArguments(bundle);
                    fragmentslist.add(fragment);
                    fragment = new FeedsFragment();
                    bundle = new Bundle();
                    bundle.putString("url", Util.HINDU_NATIONAL_URL);
                    fragment.setArguments(bundle);
                    fragmentslist.add(fragment);
                    fragment = new FeedsFragment();
                    bundle = new Bundle();
                    bundle.putString("url", Util.HINDU_SPORT_URL);
                    fragment.setArguments(bundle);
                    fragmentslist.add(fragment);
                    break;
                case "Times Of India":
                    tablist = new String[]{"International", "India","Sports"};
                    fragment = new FeedsFragment();
                    bundle = new Bundle();
                    bundle.putString("url", Util.TOI_INTERNATIONAL_URL);
                    fragment.setArguments(bundle);
                    fragmentslist.add(fragment);
                    fragment = new FeedsFragment();
                    bundle = new Bundle();
                    bundle.putString("url", Util.TOI_INDIA_URL);
                    fragment.setArguments(bundle);
                    fragmentslist.add(fragment);
                    fragment = new FeedsFragment();
                    bundle = new Bundle();
                    bundle.putString("url", Util.TOI_SPORT_URL);
                    fragment.setArguments(bundle);
                    fragmentslist.add(fragment);
                    break;
                case "The Indian Express":
                    tablist = new String[]{"World", "India","Sports"};
                    fragment = new FeedsFragment();
                    bundle = new Bundle();
                    bundle.putString("url", Util.INDIAN_EXPRESS_WORLD_URL);
                    fragment.setArguments(bundle);
                    fragmentslist.add(fragment);
                    fragment = new FeedsFragment();
                    bundle = new Bundle();
                    bundle.putString("url", Util.INDIAN_EXPRESS_INDIA_URL);
                    fragment.setArguments(bundle);
                    fragmentslist.add(fragment);
                    fragment = new FeedsFragment();
                    bundle = new Bundle();
                    bundle.putString("url", Util.INDIAN_EXPRESS_SPORTS_URL);
                    fragment.setArguments(bundle);
                    fragmentslist.add(fragment);
                    break;
                case "BBC":
                    tablist = new String[]{"World", "Asia","Sports"};
                    fragment = new FeedsFragment();
                    bundle = new Bundle();
                    bundle.putString("url", Util.BBC_WORLD_URL);
                    fragment.setArguments(bundle);
                    fragmentslist.add(fragment);
                    fragment = new FeedsFragment();
                    bundle = new Bundle();
                    bundle.putString("url", Util.BBC_ASIA_URL);
                    fragment.setArguments(bundle);
                    fragmentslist.add(fragment);
                    fragment = new FeedsFragment();
                    bundle = new Bundle();
                    bundle.putString("url", Util.BBC_SPORT_URL);
                    fragment.setArguments(bundle);
                    fragmentslist.add(fragment);
                    break;
                default:
                    fragment = new FeedsFragment();
                    bundle = new Bundle();
                    bundle.putString("url", Util.INDIAN_EXPRESS_WORLD_URL);
                    fragment.setArguments(bundle);
                    fragmentslist.add(fragment);
                    fragment = new FeedsFragment();
                    bundle = new Bundle();
                    bundle.putString("url", Util.INDIAN_EXPRESS_INDIA_URL);
                    fragment.setArguments(bundle);
                    fragmentslist.add(fragment);
                    fragment = new FeedsFragment();
                    bundle = new Bundle();
                    bundle.putString("url", Util.INDIAN_EXPRESS_SPORTS_URL);
                    fragment.setArguments(bundle);
                    fragmentslist.add(fragment);
                    break;
            }

            setupViewPager(viewPager, fragmentslist, tablist);
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setCurrentItem(0);
        }

        return v;
    }


    private void setupViewPager(ViewPager viewPager, ArrayList<Fragment> listfragment, String[] tabs) {
        Adapter  adapter = new Adapter(getActivity().getSupportFragmentManager(), mContext);
        for (int i = 0; i < tabs.length; i++) {
            adapter.addFragment(listfragment.get(i), tabs[i]);
            Log.d("fragments list", "viewpager  fragments list = " + listfragment.get(i));
        }
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();
        Context context;

        public Adapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position){
            return mFragmentTitles.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }

}
