package com.myresumeisongithub.model;

import android.text.TextUtils;

public class Resume {
    private String firstName;
    private String middleName;
    private String lastName;
    private Address address;
    private String phone;
    private String email;

    public String getProfileImage() {
        return profileImage;
    }

    private String profileImage;

    public Resume() {
    }

    public Resume(String firstName, String middleName, String lastName, String profileImage, Address address,
                  String phone, String email) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.profileImage = profileImage;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public String getFullAddress() {
        return address.toString();
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        StringBuilder fullName = new StringBuilder(firstName);
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
