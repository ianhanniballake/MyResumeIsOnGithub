package com.myresumeisongithub.sync;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.myresumeisongithub.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class SyncService extends IntentService {
    private final static String ASSET_URL_PREFIX = "file:///android_asset/";

    public SyncService() {
        super(SyncService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String resumeUrl = getString(R.string.resumeUrl);
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
        try {
            final JSONObject resumeObject = new JSONObject(resume);
            // Parse and save the resume
        } catch (JSONException e) {
            Log.e(SyncService.class.getSimpleName(), "Invalid resume found at " + resumeUrl, e);
        }
    }
}
