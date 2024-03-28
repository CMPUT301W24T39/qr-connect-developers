package com.example.qrconnect;

import java.util.HashMap;

public class User {
    private HashMap<String, Long> eventCheckInCount;

    protected String email;
    protected String firstName;
    protected Boolean isLocationTracking;
    protected Boolean isProfilePictureUploaded;
    protected String lastName;
    protected String phone;
    protected String profilePictureURL;
    protected String pronouns;
    protected String userID;

    public User(String userID, String firstName, String lastName) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = "";
        this.isLocationTracking = false;
        this.isProfilePictureUploaded = false;
        this.phone = "";
        this.profilePictureURL = "";
        this.pronouns = "";
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

    /**
     * Updates the check-in count for a specific event.
     * @param eventId The ID of the event.
     * @param checkInCount The updated check-in count for the event.
     */
    public void updateCheckInCount(String eventId, long checkInCount) {
        this.eventCheckInCount.put(eventId, checkInCount);
    }

    /**
     * Gets the number of times an attendee has checked into an event.
     * Throws an exception if the event ID does not exist.
     * @param eventID The ID of the event.
     * @return The number of times an attendee has checked into an event.
     * @throws IllegalArgumentException If the eventID is null.
     * @throws IllegalArgumentException If the eventID is not found.
     */
    public long getCheckInCount(String eventID) {
        if (eventID == null) {
            throw new IllegalArgumentException("EventID cannot be null.");
        }
        if (!eventCheckInCount.containsKey(eventID)){
            throw new IllegalArgumentException("EventID not found in the check-in count map.");
        }
        return this.eventCheckInCount.getOrDefault(eventID, 0L);
    }
}
