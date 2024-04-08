package com.example.qrconnect;

/**
 * This class maintains the attributes of a user
 */
public class UserProfile {
    protected String email;
    protected String firstName;
    protected Boolean isLocationTracking;
    protected Boolean isProfilePictureUploaded;
    protected String lastName;
    protected String phone;
    protected String pronouns;
    protected String homepage;
    protected String userID;

    /**
     * The constructor of a user
     * @param userID the id of the user
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     */
    public UserProfile(String userID, String firstName, String lastName) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = "";
        this.isLocationTracking = false;
        this.isProfilePictureUploaded = false;
        this.phone = "";
        this.pronouns = "";
        this.homepage = "";
    }

    /**
     * Get the email of the user
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email to the user
     * @param email the email to be set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the first name of the user
     * @return the first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set the first name of the user
     * @param firstName the first name of the user
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Get the first name of the user
     * @return the first name of the user
     */
    public Boolean getLocationTracking() {
        return isLocationTracking;
    }

    /**
     * Set the location tracking of the user
     * @param locationTracking the location tracking of the user
     */
    public void setLocationTracking(Boolean locationTracking) {
        isLocationTracking = locationTracking;
    }
    /**
     * Get the first name of the user
     * @return the first name of the user
     */
    public boolean getProfilePictureUploaded() {
        return isProfilePictureUploaded;
    }

    /**
     * Set the profile picture of the user
     * @param profilePictureUploaded the profile picture
     */
    public void setProfilePictureUploaded(Boolean profilePictureUploaded) {
        isProfilePictureUploaded = profilePictureUploaded;
    }
    /**
     * Get the last name of the user
     * @return the last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set the last name of the user
     * @param lastName the last name of the user
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    /**
     * Get the phone of the user
     * @return the phone of the user
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Set the phone of the user
     * @param phone the phone of the user
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
    /**
     * Get the pronouns of the user
     * @return the pronouns of the user
     */
    public String getPronouns() {
        return pronouns;
    }
    /**
     * Set the pronouns of the user
     * @param pronouns the pronouns of the user
     */
    public void setPronouns(String pronouns) {
        this.pronouns = pronouns;
    }
    /**
     * Get the homepage of the user
     * @return the homepage of the user
     */
    public String getHomepage() {
        return homepage;
    }

    /**
     * Set the homepage of the user
     * @param homepage the homepage of the user
     */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }
    /**
     * Get the userID of the user
     * @return the userID of the user
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Set the id of the user
     * @param userID the id of the user
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }
}
