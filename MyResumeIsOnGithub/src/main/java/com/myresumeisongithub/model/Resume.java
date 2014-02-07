package com.myresumeisongithub.model;

import android.text.TextUtils;

public class Resume {
    private String firstName;
    private String middleName;
    private String lastName;

    public String getProfileImage() {
        return profileImage;
    }

    private String profileImage;

    public Resume() {
    }

    public Resume(String firstName, String middleName, String lastName, String profileImage) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.profileImage = profileImage;
    }

    @Override
    public String toString() {
        StringBuffer fullName = new StringBuffer(firstName);
        if (!TextUtils.isEmpty(middleName)) {
            if (fullName.length() > 0)
                fullName.append(' ');
            fullName.append(middleName);
        }
        if (!TextUtils.isEmpty(lastName)) {
            if (fullName.length() > 0)
                fullName.append(' ');
            fullName.append(lastName);
        }
        return fullName.toString();
    }
}
