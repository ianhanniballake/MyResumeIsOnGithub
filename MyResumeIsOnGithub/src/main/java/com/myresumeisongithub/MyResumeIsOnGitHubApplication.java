package com.myresumeisongithub;

import android.app.Application;
import android.os.StrictMode;

import com.google.tagmanager.Logger;
import com.google.tagmanager.TagManager;

public class MyResumeIsOnGitHubApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TagManager tagManager = TagManager.getInstance(this);
        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults();
            tagManager.getLogger().setLogLevel(Logger.LogLevel.INFO);
            // Only load from the local (default) data
            tagManager.setRefreshMode(TagManager.RefreshMode.DEFAULT_CONTAINER);
        }
    }
}
