package com.myresumeisongithub.sync;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.myresumeisongithub.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class SyncService extends IntentService {
    private final static String ASSET_URL_PREFIX = "file:///android_asset/";
    private final static String META_DATA_KEY_RESUME_URL = "resumeUrl";
    public final static String RESUME_KEY = "RESUME_KEY";

    public SyncService() {
        super(SyncService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Read resume URL from AndroidManifest <meta-data> element
        String resumeUrl = null;
        try {
            final ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(),
                    PackageManager.GET_META_DATA);
            if (applicationInfo.metaData != null) {
                resumeUrl = applicationInfo.metaData.getString(META_DATA_KEY_RESUME_URL);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(SyncService.class.getSimpleName(), "Could not find ourselves", e);
            return;
        }
        if (TextUtils.isEmpty(resumeUrl)) {
            throw new IllegalArgumentException("No resumeUrl found - you must add a <meta-data> element to your AndroidManifest.xml with the key " +
                    META_DATA_KEY_RESUME_URL);
        }
        // Retrieve the resume
        String resume;
        try {
            InputStream resumeInputStream;
            if (resumeUrl.startsWith(ASSET_URL_PREFIX)) {
                resumeInputStream = getAssets().open(resumeUrl.substring(ASSET_URL_PREFIX.length()));
            } else {
                resumeInputStream = new URL(resumeUrl).openConnection().getInputStream();
            }
            resume = new Scanner(resumeInputStream).useDelimiter("\\A").next();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid resume URL " + resumeUrl, e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid resume URL " + resumeUrl, e);
        }
        // Parse the resume
        Gson gson = new Gson();
        Resume resumeObject = gson.fromJson(resume, Resume.class);
        Log.d(SyncService.class.getSimpleName(), "Got resume: " + gson.toJson(resumeObject));
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().putString(RESUME_KEY, gson.toJson(resumeObject)).commit();
    }
}
