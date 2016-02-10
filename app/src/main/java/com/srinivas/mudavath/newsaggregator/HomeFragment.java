package com.srinivas.mudavath.newsaggregator;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Mudavath Srinivas on 08-02-2016.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private final Handler mDrawerActionHandler = new Handler();
    private static final long DRAWER_CLOSE_DELAY_MS = 150;

    ImageView iv_the_hindu;
    ImageView iv_toi;
    ImageView iv_indian_express;
    ImageView iv_bbc;
    private int selectedItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_home, container, false);

        iv_the_hindu = (ImageView) v.findViewById(R.id.iv_the_hindu);
        iv_toi = (ImageView) v.findViewById(R.id.iv_toi);
        iv_indian_express = (ImageView) v.findViewById(R.id.iv_indian_express);
        iv_bbc = (ImageView) v.findViewById(R.id.iv_bbc);

        iv_the_hindu.setOnClickListener(this);
        iv_toi.setOnClickListener(this);
        iv_indian_express.setOnClickListener(this);
        iv_bbc.setOnClickListener(this);

        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_the_hindu:
                displayView(R.id.nav_the_hindu);
                break;
            case R.id.iv_toi:
                displayView(R.id.nav_toi);
                break;
            case R.id.iv_bbc:
                displayView(R.id.nav_bbc);
                break;
            case R.id.iv_indian_express:
                displayView(R.id.nav_indian_express);
                break;
            default:
                break;
        }
    }

    private void displayView(int id) {
        Fragment fragment = null;
        String title = null;
        Bundle bundle = new Bundle();
        Resources res=getResources();
        selectedItem=id;
        ((MainActivity)getActivity()).selectedItem=id;
        switch (id) {
            case R.id.nav_the_hindu:
                fragment = new TabFeedsFragment();
                bundle.putString("channel", res.getString(R.string.title_the_hindu));
                fragment.setArguments(bundle);
                title = getString(R.string.title_the_hindu);
                break;
            case R.id.nav_toi:
                fragment = new TabFeedsFragment();
                bundle.putString("channel", res.getString(R.string.title_toi));
                fragment.setArguments(bundle);
                title = getString(R.string.title_toi);
                break;
            case R.id.nav_bbc:
                fragment = new TabFeedsFragment();
                bundle.putString("channel", res.getString(R.string.title_bbc));
                fragment.setArguments(bundle);
                title = getString(R.string.title_bbc);
                break;
            case R.id.nav_indian_express:
                fragment = new TabFeedsFragment();
                bundle.putString("channel", res.getString(R.string.title_indian_express));
                fragment.setArguments(bundle);
                title = getString(R.string.title_indian_express);
                break;
            case R.id.nav_home:
                fragment = new FeedsFragment();
                title = getString(R.string.title_home);
                break;
            default:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
        }
        ((MainActivity)getActivity()).getNavigationView().getMenu().findItem(id).setChecked(true);

        if (fragment != null) {
            // set the toolbar title
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            mDrawerActionHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fragmentTransaction.commit();
                }
            }, DRAWER_CLOSE_DELAY_MS);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("menu_selected", selectedItem);
        super.onSaveInstanceState(outState);
    }
}
