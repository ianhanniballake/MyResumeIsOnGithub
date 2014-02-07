package com.myresumeisongithub.model;

import android.text.TextUtils;

public class Address {
    private String street;
    private String city;
    private String state;
    private String zip;

    public Address() {
    }

    public Address(String street, String city, String state, String zip) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    @Override
    public String toString() {
        StringBuilder fullAddress = new StringBuilder(street);
        if (!TextUtils.isEmpty(city)) {
            if (fullAddress.length() > 0)
                fullAddress.append(", ");
            fullAddress.append(city);
        }
        if (!TextUtils.isEmpty(state)) {
            if (fullAddress.length() > 0)
                fullAddress.append(", ");
            fullAddress.append(state);
        }
        if (!TextUtils.isEmpty(zip)) {
            if (fullAddress.length() > 0)
                fullAddress.append(' ');
            fullAddress.append(zip);
        }
        return fullAddress.toString();
    }
}
