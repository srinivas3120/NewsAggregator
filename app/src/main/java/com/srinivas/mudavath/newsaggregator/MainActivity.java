package com.srinivas.mudavath.newsaggregator;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity{

    boolean doubleBackToExitPressedOnce = false;
    private final Handler mDrawerActionHandler = new Handler();
    private static final long DRAWER_CLOSE_DELAY_MS = 150;

    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private ActionBar ab;
    public NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView.OnNavigationItemSelectedListener navigationViewListener;

    private TextView tv_username;
    private ImageView civ_profile_pic;
    public int selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setUpActionBar();
        setUpNavigationDrawer();

        navigationView.getMenu().findItem(R.id.nav_editorials).setChecked(true);
        displayView(R.id.nav_editorials);

    }

    private void initializeViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        View headerLayout = navigationView.inflateHeaderView(R.layout.navigation_header);
        civ_profile_pic= (ImageView) headerLayout.findViewById(R.id.civ_profile_pic);
        tv_username= (TextView) headerLayout.findViewById(R.id.tv_username);
    }

    private void setUpActionBar() {
        ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.ic_navigation_menu_white);
        ab.setTitle("Home");
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void setUpNavigationDrawer() {
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,R.string.open_drawer, R.string.close_drawer) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                closeKeyBoardIfOpen();
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we don't want anything to happen so we leave this blank.
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                tv_username.setText("Mudavath Srinivas");
                civ_profile_pic.setImageResource(R.drawable.profile_pic_default_profile_pic);
                super.onDrawerOpened(drawerView);
            }

        };

    }

    public NavigationView getNavigationView(){
        return navigationView;
    }


    private void setupDrawerContent(NavigationView navigationView) {

        navigationViewListener=new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                displayView(menuItem.getItemId());
                return true;
            }
        };
        navigationView.setNavigationItemSelectedListener(navigationViewListener);
    }

    private void displayView(int id) {
        Fragment fragment = null;
        String title = null;
        Bundle bundle = new Bundle();
        Resources res=getResources();
        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        selectedItem=id;
        switch (id) {
            case R.id.nav_the_hindu:
                fragment = new TabFeedsFragment();
                bundle.putString("channel", res.getString(R.string.title_the_hindu));
                fragment.setArguments(bundle);
                title = getString(R.string.title_the_hindu);
                break;
            case R.id.nav_ie_opinions:
                fragment = new TabFeedsFragment();
                bundle.putString("channel", res.getString(R.string.title_ie_opinions));
                fragment.setArguments(bundle);
                title = getString(R.string.title_ie_opinions);
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
            case R.id.nav_editorials:
            default:
                fragment = new TabFeedsFragment();
                bundle.putString("channel", res.getString(R.string.title_editorials));
                fragment.setArguments(bundle);
                title = getString(R.string.title_editorials);
                break;
        }

        if (fragment != null) {
            // set the toolbar title
            getSupportActionBar().setTitle(title);
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            mDrawerActionHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fragmentTransaction.commit();
                }
            }, DRAWER_CLOSE_DELAY_MS);

        }
    }

    private void closeKeyBoardIfOpen() {
        View view = MainActivity.this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else {

            View view = MainActivity.this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Util.showBottomToast(this,"Please click BACK again to exit");

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("menu_selected", selectedItem);
        super.onSaveInstanceState(outState);
    }
}
