package com.example.qrconnect;

import java.util.HashMap;

public class Attendee extends User{

    /*
    * eventCheckInCount key
    * */
    private HashMap <Integer, Integer> eventCheckInCount;
    public Attendee(String userID, String name, String contactInformation) {
        super(userID, name, contactInformation);
        eventCheckInCount = new HashMap<>();
    }

    @Override
    public void uploadProfilePicture(String imagePath) {

    }

    @Override
    public void removeProfilePicture() {

    }

    public void checkInEvent(int eventID) {
        if (eventCheckInCount.containsKey(eventID)) {
            int count = eventCheckInCount.get(eventID);
            eventCheckInCount.put(eventID, count + 1);
        } else {
            eventCheckInCount.put(eventID, 1);
        }
    }

    public int getCheckInCount(Integer eventID) {
        if (eventID == null) {
            throw new IllegalArgumentException("EventID cannot be null.");
        }
        return eventCheckInCount.getOrDefault(eventID, 0);
    }


}
