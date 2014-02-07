package com.myresumeisongithub.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.myresumeisongithub.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class that handles interaction with the Navigation Drawer on both high level and lower level Activities
 */
public abstract class DrawerActivity extends FragmentActivity implements OnClickListener, DrawerListener {
    /**
     * Interface to receive callbacks on Navigation Drawer visibility changes
     */
    public interface DrawerVisibilityListener {
        /**
         * Callback for when the Navigation Drawer is closed
         */
        public void onDrawerClosed();

        /**
         * Callback for when the Navigation Drawer is opened
         */
        public void onDrawerOpened();
    }

    /**
     * Boolean Preference indicating whether the user has ever manually opened the Navigation Drawer
     */
    public static final String HAS_OPENED_DRAWER = "PREF_HAS_OPENED_DRAWER";
    private static final String MENU_PENDING_SELECTED_ITEM = "MENU_PENDING_SELECTED_ITEM";
    /**
     * Drawer Layout associated with the Navigation Drawer
     */
    protected DrawerLayout mDrawerLayout;
    private final List<DrawerVisibilityListener> mDrawerVisibilityListeners = new ArrayList<DrawerVisibilityListener>();
    private final List<View> mMenuItems = new ArrayList<View>();
    private CharSequence pendingSelectedMenuItemTitle = null;

    /**
     * Getter for the title of the current content. Used to restore the title after the Navigation Drawer closes. If you
     * do not provide a title, the label for the current Activity per the Manifest will be used.
     *
     * @return The title with the current content
     */
    protected CharSequence getContentTitle() {
        try {
            final PackageManager packageManager = getPackageManager();
            return packageManager.getActivityInfo(getComponentName(), 0).loadLabel(packageManager);
        } catch (final NameNotFoundException e) {
            Log.w(getClass().getSimpleName(), "Error getting Activity label", e);
            return getText(R.string.drawer_about_me);
        }
    }

    /**
     * Getter for the R.layout ID to be used for setContentView. Note that this layout MUST include a DrawerLayout with
     * the id drawer_layout
     *
     * @return ID of the layout to pass to setContentView
     */
    protected abstract int getContentView();

    /**
     * Gets the default DrawerListener. Any DrawerListener you set MUST pass calls to this listener as well
     *
     * @return The default DrawerListener
     */
    protected DrawerListener getDrawerListener() {
        return this;
    }

    /**
     * Getter for the currently selected Navigation Drawer menu item
     *
     * @return The currently selected menu item in the Navigation Drawer
     */
    protected CharSequence getSelectedMenuItem() {
        for (final View menuView : mMenuItems) {
            if (!menuView.isEnabled()) {
                return menuView.getContentDescription();
            }
        }
        return getText(R.string.drawer_about_me);
    }

    /**
     * Returns whether the drawer is currently open
     *
     * @return Whether the drawer is open
     */
    public boolean isDrawerOpen() {
        return mDrawerLayout.isDrawerOpen(Gravity.LEFT);
    }

    @Override
    public void onClick(final View v) {
        pendingSelectedMenuItemTitle = v.getContentDescription();
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        mDrawerLayout.setDrawerListener(getDrawerListener());
        final View menuView = findViewById(R.id.menu);
        final Button btnAboutMe = (Button) menuView.findViewById(R.id.drawer_about_me);
        btnAboutMe.setOnClickListener(this);
        mMenuItems.add(btnAboutMe);
        final Button btnJobHistory = (Button) menuView.findViewById(R.id.drawer_job_history);
        btnJobHistory.setOnClickListener(this);
        mMenuItems.add(btnJobHistory);
        final Button btnEducation = (Button) menuView.findViewById(R.id.drawer_education);
        btnEducation.setOnClickListener(this);
        mMenuItems.add(btnEducation);
        final Button btnPortfolio = (Button) menuView.findViewById(R.id.drawer_portfolio);
        btnPortfolio.setOnClickListener(this);
        mMenuItems.add(btnPortfolio);
        if (savedInstanceState != null) {
            pendingSelectedMenuItemTitle = savedInstanceState
                    .getCharSequence(DrawerActivity.MENU_PENDING_SELECTED_ITEM);
            mDrawerLayout.closeDrawers();
        }
    }

    @Override
    public void onDrawerClosed(final View drawerView) {
        for (final DrawerVisibilityListener drawerVisibilityListener : mDrawerVisibilityListeners) {
            drawerVisibilityListener.onDrawerClosed();
        }
        if (pendingSelectedMenuItemTitle == null) {
            getActionBar().setTitle(getContentTitle());
            supportInvalidateOptionsMenu();
        } else {
            selectMenuItem(pendingSelectedMenuItemTitle);
            showContent(pendingSelectedMenuItemTitle);
            pendingSelectedMenuItemTitle = null;
        }
    }

    @Override
    public void onDrawerOpened(final View drawerView) {
        getActionBar().setTitle(R.string.app_name);
        supportInvalidateOptionsMenu();
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putBoolean(DrawerActivity.HAS_OPENED_DRAWER, true).apply();
        for (final DrawerVisibilityListener drawerVisibilityListener : mDrawerVisibilityListeners) {
            drawerVisibilityListener.onDrawerOpened();
        }
        // Close the keyboard if it is open as it will draw over the Navigation Drawer
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mDrawerLayout.getWindowToken(), 0);
    }

    @Override
    public void onDrawerSlide(final View drawerView, final float slideOffset) {
        // Nothing to do
    }

    @Override
    public void onDrawerStateChanged(final int newState) {
        // Nothing to do
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isDrawerOpen()) {
                    mDrawerLayout.closeDrawers();
                } else {
                    final Intent upIntent = NavUtils.getParentActivityIntent(this);
                    if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                        // This activity is NOT part of this app's task, so create a new task
                        // when navigating up, with a synthesized back stack.
                        TaskStackBuilder.create(this)
                                // Add all of this activity's parents to the back stack
                                .addNextIntentWithParentStack(upIntent)
                                        // Navigate up to the closest parent
                                .startActivities();
                    } else {
                        // This activity is part of this app's task, so simply
                        // navigate up to the logical parent activity.
                        NavUtils.navigateUpTo(this, upIntent);
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(DrawerActivity.MENU_PENDING_SELECTED_ITEM, pendingSelectedMenuItemTitle);
    }

    /**
     * Register a new Navigation Drawer visibility listener. Ensure that you call unregisterDrawerVisibilityListener
     *
     * @param drawerVisibilityListener Navigation Drawer visibility listener to register
     */
    public void registerDrawerVisibilityListener(final DrawerVisibilityListener drawerVisibilityListener) {
        mDrawerVisibilityListeners.add(drawerVisibilityListener);
    }

    /**
     * Selects a menu item in the Navigation Drawer by name
     *
     * @param menuItemTitle Title of the menu item to select
     */
    protected void selectMenuItem(final CharSequence menuItemTitle) {
        // Use two passes to ensure that all items appear enabled (i.e., not selected) before the selected item becomes
        // enabled
        for (final View menuView : mMenuItems) {
            menuView.setEnabled(true);
        }
        for (final View menuView : mMenuItems) {
            if (TextUtils.equals(menuView.getContentDescription(), menuItemTitle)) {
                menuView.setEnabled(false);
            }
        }
    }

    /**
     * Shows the content associated with the given title
     *
     * @param title Title of the content to show
     */
    protected void showContent(final CharSequence title) {
        startActivity(MainActivity.getIntent(this, title));
    }

    /**
     * Unregisters a previously registered Navigation Drawer visibility listener
     *
     * @param drawerVisibilityListener Navigation Drawer visibility listener to unregister
     */
    public void unregisterDrawerVisibilityListener(final DrawerVisibilityListener drawerVisibilityListener) {
        mDrawerVisibilityListeners.remove(drawerVisibilityListener);
    }
}
