package com.example.qrconnect;

import java.util.HashMap;

public class User {
    private HashMap<Integer, Integer> eventCheckInCount;

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
     * Increments the number of times an attendee has checked into an event.
     * @param eventID The ID of the event.
     */
    public void checkInEvent(int eventID) {
        if (eventCheckInCount.containsKey(eventID)) {
            int count = eventCheckInCount.get(eventID);
            eventCheckInCount.put(eventID, count + 1);
        } else {
            eventCheckInCount.put(eventID, 1);
        }
    }

    /**
     * Gets the number of times an attendee has checked into an event.
     * Throws an exception if the event ID does not exist.
     * @param eventID The ID of the event.
     * @return The number of times an attendee has checked into an event.
     * @throws IllegalArgumentException If the eventID is null.
     */
    public int getCheckInCount(Integer eventID) {
        if (eventID == null) {
            throw new IllegalArgumentException("EventID cannot be null.");
        }
        return eventCheckInCount.getOrDefault(eventID, 0);
    }
}
