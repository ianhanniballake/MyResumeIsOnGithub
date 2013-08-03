package com.myresumeisongithub.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.myresumeisongithub.R;
import com.myresumeisongithub.fragments.AboutMeFragment;
import com.myresumeisongithub.fragments.EducationFragment;
import com.myresumeisongithub.fragments.JobHistoryFragment;
import com.myresumeisongithub.fragments.PortfolioFragment;
import com.myresumeisongithub.sync.SyncService;

import java.util.HashMap;
import java.util.Map;

/**
 * Main Activity for China Glaze which serves as the high level view for the application
 */
public class MainActivity extends DrawerActivity {
    private static final String SELECTED_FRAGMENT = "SELECTED_FRAGMENT";
    private ActionBarDrawerToggle mDrawerToggle;
    private Map<CharSequence, Class<? extends Fragment>> mFragmentMap;
    private CharSequence mSelectedFragmentTitle = null;

    /**
     * Creates a new Intent which launches this Activity selecting the given content
     *
     * @param context Context to use to create the Intent
     * @param title   Title of the content to show
     * @return Intent that starts this Activity, showing the given title
     */
    public static Intent getIntent(final Context context, final CharSequence title) {
        final Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(MainActivity.SELECTED_FRAGMENT, title);
        return intent;
    }

    @Override
    protected CharSequence getContentTitle() {
        return getSelectedMenuItem();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
                mDrawerLayout, /* DrawerLayout object */
                R.drawable.ic_navigation_drawer, /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open, /* "open drawer" description for accessibility */
                R.string.drawer_close /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(final View drawerView) {
                super.onDrawerClosed(drawerView);
                getDrawerListener().onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(final View drawerView) {
                super.onDrawerOpened(drawerView);
                getDrawerListener().onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(final View drawerView, final float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                getDrawerListener().onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerStateChanged(final int newState) {
                super.onDrawerStateChanged(newState);
                getDrawerListener().onDrawerStateChanged(newState);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        // Ensure there's a fragment showing
        final FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.main_frame) == null) {
            fragmentManager.beginTransaction().add(R.id.main_frame, new AboutMeFragment()).commit();
        }
        if (savedInstanceState != null) {
            mSelectedFragmentTitle = savedInstanceState.getCharSequence(MainActivity.SELECTED_FRAGMENT);
        } else if (getIntent().hasExtra(MainActivity.SELECTED_FRAGMENT)) {
            mSelectedFragmentTitle = getIntent().getCharSequenceExtra(MainActivity.SELECTED_FRAGMENT);
            showContent(mSelectedFragmentTitle);
        }
        if (mSelectedFragmentTitle == null) {
            // First launch
            mSelectedFragmentTitle = getText(R.string.drawer_about_me);
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            if (!sharedPreferences.getBoolean(DrawerActivity.HAS_OPENED_DRAWER, false)) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        }
        selectMenuItem(mSelectedFragmentTitle);
        startService(new Intent(this, SyncService.class));
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerToggle.onOptionsItemSelected(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(MainActivity.SELECTED_FRAGMENT, mSelectedFragmentTitle);
    }

    @Override
    protected void selectMenuItem(final CharSequence menuItemTitle) {
        super.selectMenuItem(menuItemTitle);
        mSelectedFragmentTitle = menuItemTitle;
        if (isDrawerOpen()) {
            getActionBar().setTitle(getText(R.string.my_name));
        } else {
            getActionBar().setTitle(mSelectedFragmentTitle);
        }
    }

    @Override
    protected void showContent(final CharSequence title) {
        if (mFragmentMap == null) {
            mFragmentMap = new HashMap<CharSequence, Class<? extends Fragment>>();
            mFragmentMap.put(getText(R.string.drawer_about_me), AboutMeFragment.class);
            mFragmentMap.put(getText(R.string.drawer_job_history), JobHistoryFragment.class);
            mFragmentMap.put(getText(R.string.drawer_education), EducationFragment.class);
            mFragmentMap.put(getText(R.string.drawer_portfolio), PortfolioFragment.class);
        }
        Fragment newContent;
        try {
            newContent = mFragmentMap.get(title).newInstance();
        } catch (final InstantiationException e) {
            Log.e(MainActivity.class.getSimpleName(), "Error instantiating " + title, e);
            return;
        } catch (final IllegalAccessException e) {
            Log.e(MainActivity.class.getSimpleName(), "Error instantiating " + title, e);
            return;
        }
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        final FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.main_frame, newContent);
        ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}
