package com.example.qrconnect;

import android.graphics.Bitmap;

public class UserProfile {
    protected String userID;
    protected String firstName;
    protected String lastName;
    protected String pronouns;
    protected String email;
    protected String phone;
    protected Bitmap generatedProfilePicture;
    //uploaded profile picture
    protected Boolean isProfilePictureUploaded;
    protected Boolean isLocationTracking;

    public UserProfile(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPronouns() {
        return pronouns;
    }

    public void setPronouns(String pronouns) {
        this.pronouns = pronouns;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Bitmap getGeneratedProfilePicture() {
        return generatedProfilePicture;
    }

    public void setGeneratedProfilePicture(Bitmap generatedProfilePicture) {
        this.generatedProfilePicture = generatedProfilePicture;
    }

    public Boolean getProfilePictureUploaded() {
        return isProfilePictureUploaded;
    }

    public void setProfilePictureUploaded(Boolean profilePictureUploaded) {
        isProfilePictureUploaded = profilePictureUploaded;
    }

    public Boolean getLocationTracking() {
        return isLocationTracking;
    }

    public void setLocationTracking(Boolean locationTracking) {
        isLocationTracking = locationTracking;
    }
}
