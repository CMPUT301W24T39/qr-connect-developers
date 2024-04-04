package com.example.qrconnect;

import java.util.HashMap;

/**
 * Represents an attendee and their check-in information for events.
 */
public class DisplayAttendee {
    private HashMap<String, Long> eventCheckInCount; // EventId to CheckIn times
    private String UserId;
    private String UserName;

    /**
     * Constructs a DisplayAttendee object with the given user ID and name.
     * Initializes the eventCheckInCount HashMap.
     * @param userID The user ID of the attendee.
     * @param name The name of the attendee.
     */
    public DisplayAttendee(String userID, String name) {
        this.UserId = userID;
        this.UserName = name;
        this.eventCheckInCount = new HashMap<>();
    }

    /**
     * Updates the check-in count for a specific event.
     * @param eventId The ID of the event.
     * @param checkInCount The updated check-in count for the event.
     */
    public void updateCheckInCount(String eventId, long checkInCount) {
        this.eventCheckInCount.put(eventId, checkInCount);
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
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
            return 0L;
        }
        return this.eventCheckInCount.getOrDefault(eventID, 0L);
    }
}
