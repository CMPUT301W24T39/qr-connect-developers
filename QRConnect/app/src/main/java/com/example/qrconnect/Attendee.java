package com.example.qrconnect;

import java.util.HashMap;

/**
 * The Attendee class represents an attendee, who is a user.
 * Initializes an attendee with basic information.
 * It extends the User class.
 */
public class Attendee extends User{
    private HashMap <Integer, Integer> eventCheckInCount;

    /**
     * Constructs an instance of the Attendee class.
     * @param userID The ID of the user (attendee).
     * @param name The name of the user (attendee).
     * @param contactInformation contact information of the user (attendee).
     */
    public Attendee(String userID, String name, String contactInformation) {
        super(userID, name, contactInformation);
        eventCheckInCount = new HashMap<>();
    }

    /**
     * Uploads a profile picture for the attendee (user).
     * This method is not implemented in this class.
     * @param imagePath The path to the image.
     */
    @Override
    public void uploadProfilePicture(String imagePath) { }
    // This method is not implemented here.

    /**
     * Removes the uploaded profile picture of the attendee (user).
     * This method is not implemented in this class.
     */
    @Override
    public void removeProfilePicture() { }
    // This method is not implemented here.

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