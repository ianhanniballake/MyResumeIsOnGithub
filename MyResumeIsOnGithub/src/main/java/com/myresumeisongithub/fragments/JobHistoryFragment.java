package com.myresumeisongithub.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.myresumeisongithub.R;
import com.myresumeisongithub.model.JobHistory;
import com.myresumeisongithub.model.Resume;
import com.myresumeisongithub.sync.SyncService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JobHistoryFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private class JobHistoryAdapter extends BaseAdapter {
        final LayoutInflater mLayoutInflater;
        final SimpleDateFormat mDateFormat = new SimpleDateFormat("MMM yyyy");

        public JobHistoryAdapter(Context context) {
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mJobHistory.size();
        }

        @Override
        public Object getItem(final int position) {
            return null;
        }

        @Override
        public long getItemId(final int position) {
            return mJobHistory.get(position).getStartDate().getTime();
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = mLayoutInflater.inflate(R.layout.item_job_history, parent, false);
            }
            JobHistory jobHistory = mJobHistory.get(position);
            TextView titleView = (TextView) view.findViewById(R.id.title);
            String title = jobHistory.getTitle();
            if (TextUtils.isEmpty(title)) {
                titleView.setText("");
                titleView.setVisibility(View.GONE);
            } else {
                titleView.setText(title);
                titleView.setVisibility(View.VISIBLE);
            }
            TextView descriptionView = (TextView) view.findViewById(R.id.description);
            String description = jobHistory.getDescription();
            if (TextUtils.isEmpty(description)) {
                descriptionView.setText("");
                descriptionView.setVisibility(View.GONE);
            } else {
                descriptionView.setText(description);
                descriptionView.setVisibility(View.VISIBLE);
            }
            TextView companyView = (TextView) view.findViewById(R.id.company);
            String company = jobHistory.getCompany();
            if (TextUtils.isEmpty(company)) {
                companyView.setText("");
                companyView.setVisibility(View.GONE);
            } else {
                companyView.setText(company);
                companyView.setVisibility(View.VISIBLE);
            }
            TextView dateRangeView = (TextView) view.findViewById(R.id.date_range);
            Date startDate = jobHistory.getStartDate();
            Date endDate = jobHistory.getEndDate();
            if (startDate == null) {
                dateRangeView.setText("");
                dateRangeView.setVisibility(View.GONE);
            } else {
                if (endDate == null) {
                    dateRangeView.setText(getString(R.string.date_range_to_present, mDateFormat.format(startDate)));
                } else {
                    dateRangeView.setText(getString(R.string.date_range, mDateFormat.format(startDate),
                            mDateFormat.format(endDate)));
                }
                dateRangeView.setVisibility(View.VISIBLE);
            }
            return view;
        }
    }

    private List<JobHistory> mJobHistory = new ArrayList<JobHistory>();
    private AbsListView mListView;
    private JobHistoryAdapter mAdapter;

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        mAdapter = new JobHistoryAdapter(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_history, container, false);
        mListView = (AbsListView) view.findViewById(R.id.job_history_list);
        mListView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (TextUtils.equals(key, SyncService.RESUME_KEY)) {
            updateJobHistory(sharedPreferences);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (sp.contains(SyncService.RESUME_KEY)) {
            updateJobHistory(sp);
        }
        sp.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.unregisterOnSharedPreferenceChangeListener(this);
    }

    private void updateJobHistory(SharedPreferences sharedPreferences) {
        String resumeString = sharedPreferences.getString(SyncService.RESUME_KEY, null);
        if (TextUtils.isEmpty(resumeString)) {
            return;
        }
        Resume resume = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(resumeString, Resume.class);
        mJobHistory = resume.getJobHistory();
        if (mJobHistory == null)
            mJobHistory = new ArrayList<JobHistory>();
        mAdapter.notifyDataSetChanged();
    }
}
