package com.myresumeisongithub.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.myresumeisongithub.R;
import com.myresumeisongithub.model.Resume;
import com.myresumeisongithub.sync.SyncService;
import com.squareup.picasso.Picasso;

public class AboutMeFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private Resume mResume = null;
    private TextView mFullName;
    private ImageView mProfileImage;
    private TextView mAddress;
    private TextView mPhone;
    private TextView mEmail;

    private void updateResume(SharedPreferences sharedPreferences) {
        String resume = sharedPreferences.getString(SyncService.RESUME_KEY, null);
        if (TextUtils.isEmpty(resume)) {
            return;
        }
        mResume = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(resume, Resume.class);
        final String profileImage = mResume.getProfileImage();
        if (TextUtils.isEmpty(profileImage))
            mProfileImage.setVisibility(View.GONE);
        else {
            mProfileImage.setVisibility(View.VISIBLE);
            Picasso.with(getActivity()).load(mResume.getProfileImage()).into(mProfileImage);
        }
        mFullName.setText(mResume.toString());
        mAddress.setText(mResume.getFullAddress());
        mPhone.setText(mResume.getPhone());
        mEmail.setText(mResume.getEmail());
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (sp.contains(SyncService.RESUME_KEY)) {
            updateResume(sp);
        }
        sp.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_me, container, false);
        mProfileImage = (ImageView) view.findViewById(R.id.profileImage);
        mFullName = (TextView) view.findViewById(R.id.fullName);
        mAddress = (TextView) view.findViewById(R.id.address);
        mPhone = (TextView) view.findViewById(R.id.phone);
        mEmail = (TextView) view.findViewById(R.id.email);
        return view;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (TextUtils.equals(key, SyncService.RESUME_KEY)) {
            updateResume(sharedPreferences);
        }
    }
}
