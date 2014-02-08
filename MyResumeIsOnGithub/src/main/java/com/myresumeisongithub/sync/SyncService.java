package com.myresumeisongithub.sync;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.tagmanager.Container;
import com.google.tagmanager.ContainerOpener;
import com.google.tagmanager.TagManager;
import com.myresumeisongithub.R;
import com.myresumeisongithub.model.Address;
import com.myresumeisongithub.model.JobHistory;
import com.myresumeisongithub.model.Resume;

import java.lang.reflect.Type;
import java.util.List;

public class SyncService extends IntentService {
    public final static String RESUME_KEY = "RESUME_KEY";
    private static final String FIRST_NAME = "firstName";
    private static final String MIDDLE_NAME = "middleName";
    private static final String LAST_NAME = "lastName";
    private static final String PROFILE_IMAGE = "profileImage";

    public SyncService() {
        super(SyncService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        TagManager tagManager = TagManager.getInstance(this);
        Container container = ContainerOpener.openContainer(tagManager, getString(R.string.container_id),
                ContainerOpener.OpenType.PREFER_NON_DEFAULT, ContainerOpener.DEFAULT_TIMEOUT_IN_MILLIS).get();
        final Resume resumeObject = parseResume(container, gson);
        Log.d(SyncService.class.getSimpleName(), "Got resume: " + gson.toJson(resumeObject));
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().putString(RESUME_KEY, gson.toJson(resumeObject)).commit();
    }

    private static Resume parseResume(Container container, Gson gson) {
        final String firstName = container.getString(FIRST_NAME);
        final String middleName = container.getString(MIDDLE_NAME);
        final String lastName = container.getString(LAST_NAME);
        final String profileImage = container.getString(PROFILE_IMAGE);
        final Address address = gson.fromJson(container.getString("address"), Address.class);
        final String phone = container.getString("phone");
        final String email = container.getString("email");
        Type jobHistoryCollectionType = new TypeToken<List<JobHistory>>() {
        }.getType();
        final List<JobHistory> jobHistory = gson.fromJson(container.getString("jobHistory"),
                jobHistoryCollectionType);
        return new Resume(firstName, middleName, lastName, profileImage, address, phone, email, jobHistory);
    }
}
