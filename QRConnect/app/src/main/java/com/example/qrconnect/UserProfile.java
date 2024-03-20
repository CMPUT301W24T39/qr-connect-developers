package com.example.qrconnect;

import android.graphics.Bitmap;

public class UserProfile {
    protected String email;
    protected String firstName;
    protected Boolean isLocationTracking;
    protected Boolean isProfilePictureUploaded;
    protected String lastName;
    protected String phone;
    protected String profilePictureURL;
    protected String pronouns;
    protected String userID;

    public UserProfile(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Boolean getLocationTracking() {
        return isLocationTracking;
    }

    public void setLocationTracking(Boolean locationTracking) {
        isLocationTracking = locationTracking;
    }

    public boolean getProfilePictureUploaded() {
        return isProfilePictureUploaded;
    }

    public void setProfilePictureUploaded(Boolean profilePictureUploaded) {
        isProfilePictureUploaded = profilePictureUploaded;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public void setProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
    }

    public String getPronouns() {
        return pronouns;
    }

    public void setPronouns(String pronouns) {
        this.pronouns = pronouns;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
