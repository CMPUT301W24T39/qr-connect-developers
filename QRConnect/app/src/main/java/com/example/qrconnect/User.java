package com.example.qrconnect;

import com.google.common.collect.Sets;

/**
 * This is the User class.
 * Abstract base class for users in the event management system.
 * Defines common attributes and operations for all user types, including organizers, attendees,
 * and administrators. Subclasses are responsible for implementing abstract methods to support
 * specific user interactions like profile management.
 */
abstract class User {
    protected String userID;
    protected String name;
    protected String contactInformation;

    /**
     * This is a constructor for the User Class to initialize user information.
     * @param userID the ID of the user.
     * @param name the name of the user.
     * @param contactInformation the contact information of the user.
     */
    public User(String userID, String name, String contactInformation) {
        this.userID = userID;
        this.name = name;
        this.contactInformation = contactInformation;// Optional, can be null for Administrators
    }

    /**
     * Abstract method to upload a profile picture.
     * @param imagePath path to the image.
     */
    public abstract void uploadProfilePicture(String imagePath);

    /**
     * Abstract method to remove an uploaded profile picture (go back to the default profile picture).
     */
    public abstract void removeProfilePicture();


    /**
     * Returns the ID of the user.
     * @return the ID of the user.
     */
    public String getUserID() {
        return userID;
    }

    /**
    * Sets the ID of the user to a given ID.
    * @param userID ID of the user.
    */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * Returns name of the user.
     * @return name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     * @param name name of the user.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the contact information of the user.
     * @return contact information of the user.
     */
    public String getContactInformation() {
        return contactInformation;
    }

    /**
     * Set the contact information of the user.
     * @param contactInformation contact information of the user.
     */
    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }
}
