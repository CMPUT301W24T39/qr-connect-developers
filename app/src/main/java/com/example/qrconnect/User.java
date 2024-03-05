package com.example.qrconnect;
/**
 * Abstract base class for users in the event management system.
 * Defines common attributes and operations for all user types, including organizers, attendees,
 * and administrators. Subclasses are responsible for implementing abstract methods to support
 * specific user interactions like profile management
 */
abstract class User {
    protected String userID;
    protected String name;
    protected String contactInformation;

    // Constructor to initialize user information
    public User(String userID, String name, String contactInformation) {
        this.userID = userID;
        this.name = name;
        this.contactInformation = contactInformation;// Optional, can be null for Administrators
    }
    public abstract void uploadProfilePicture(String imagePath);
    public abstract void removeProfilePicture();
    // Abstract method to update profile information

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }
}
