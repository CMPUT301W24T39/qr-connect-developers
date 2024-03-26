package com.example.qrconnect;

import java.io.Serializable;
import java.util.HashMap;

/**
 * The Event class defines an event.
 * Contains details such as title, date, time, location, capacity, announcement, and associated QR codes.
 */
public class Event implements Serializable {
    private String eventTitle;
    private String date;
    private String time;
    private String location;
    private Integer capacity;
    private String announcement;
//    private Bitmap QRCodeImage;
//    private Bitmap PromoQRCodeImage;
    private String checkInQRCodeImageUrl;
    private String promoQRCodeImageUrl;
    private String eventId;
    private String hostId;
    private HashMap<String, Long> attendeeListIdToCheckInTimes;

    private HashMap<String, String> attendeeListIdToName;

    /**
     * Constructs an Event object with the specified details.
     * @param eventTitle the title of an event.
     * @param date the data of an event.
     * @param time the time of an event.
     * @param location the location of an event.
     * @param capacity the capacity of an event.
     * @param announcement the announcement of an event.
     * @param checkInQRCodeImageUrl the id of the QR code of an event.
     * @param promoQRCodeImageUrl the id of the promotion QR code of an event.
     * @param eventId the id of an event.
     * @param hostId the id of an event organizer.
     * @param attendeeListIdToCheckInTimes the hashMap with userId and check-in#.
     */
    public Event(String eventTitle, String date, String time, String location,
                 Integer capacity, String announcement, String checkInQRCodeImageUrl,
                 String promoQRCodeImageUrl, String eventId, String hostId,
                 HashMap<String, Long> attendeeListIdToCheckInTimes,
                 HashMap<String, String> attendeeListIdToName) {
        this.eventTitle = eventTitle;
        this.date = date;
        this.time = time;
        this.location = location;
        this.capacity = capacity;
        this.announcement = announcement;
//        this.QRCodeImage = QRCodeImage;
//        this.PromoQRCodeImage = promoQRCodeImage;
        this.checkInQRCodeImageUrl = checkInQRCodeImageUrl;
        this.promoQRCodeImageUrl = promoQRCodeImageUrl;
        this.eventId = eventId;
        this.hostId = hostId;
        this.attendeeListIdToCheckInTimes = attendeeListIdToCheckInTimes == null ? new HashMap<>() : attendeeListIdToCheckInTimes;
        this.attendeeListIdToName = attendeeListIdToName == null ? new HashMap<>() : attendeeListIdToName;
    }

    /**
     * Empty constructor for the Event class.
     */
    public Event(){};

//    /**
//     * This method gets the QR code of an event.
//     * @return Return a QR code image.
//     */
//    public Bitmap getQRCodeImage() {
//        return QRCodeImage;
//    }

//    /**
//     * This method sets a QR code image to QRCodeImage attribute.
//     * @param QRCodeImage the QR code of an event.
//     */
//    public void setQRCodeImage(Bitmap QRCodeImage) {
//        this.QRCodeImage = QRCodeImage;
//    }
//
//    /**
//     * This method gets the Promotion QR code of an event.
//     * @return Return the promotion QR code image.
//     */
//    public Bitmap getPromoQRCodeImage() {
//        return PromoQRCodeImage;
//    }
//
//    /**
//     * This method sets a QR code image to QRCodeImage attribute.
//     * @param promoQRCodeImage the promotion QR code of an event.
//     */
//    public void setPromoQRCodeImage(Bitmap promoQRCodeImage) {
//        PromoQRCodeImage = promoQRCodeImage;
//    }

    /**
     * This method gets the title of an event.
     * @return Return the title of an event.
     */
    public String getEventTitle() {
        return eventTitle;
    }

    /**
     * This method sets a title of an event.
     * @param eventTitle the title of an event.
     */
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    /**
     * This method gets the date of an event.
     * @return Return the date of an event.
     */
    public String getDate() {
        return date;
    }

    /**
     * This method sets the date of an event.
     * @param date the date of an event.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * This method gets the time of an event.
     * @return Return the time of an event.
     */
    public String getTime() {
        return time;
    }

    /**
     * This method sets the time of an event.
     * @param time the time of an event.
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * This method gets the location of an event.
     * @return Return the location of an event.
     */
    public String getLocation() {
        return location;
    }

    /**
     * This method sets the location of an event.
     * @param location the location of an event.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * This method gets the capacity of an event.
     * @return  Return the capacity of an event.
     */
    public Integer getCapacity() {
        return capacity;
    }


    /**
     * This method sets the capacity of an event.
     * @param capacity the capacity of an event.
     */
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    /**
     * This method gets the announcement of an event.
     * @return  Return the announcement of an event.
     */
    public String getAnnouncement() {
        return announcement;
    }

    /**
     * This method sets the announcement of an event.
     * @param announcement the announcement of an event.
     */
    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    /**
     * This method gets the eventCheckInId of an event.
     * @return  Return the eventCheckInId of an event.
     */
    public String getEventCheckInId() {
        return checkInQRCodeImageUrl;
    }

    /**
     * This method sets the eventCheckInId of an event.
     * @param checkInQRCodeImageUrl the eventCheckInId of an event.
     */
    public void setEventCheckInId(String checkInQRCodeImageUrl) {
        this.checkInQRCodeImageUrl = checkInQRCodeImageUrl;
    }

    /**
     * This method gets the eventPromoId of an event.
     * @return  Return the eventPromoId of an event.
     */
    public String getEventPromoId() {
        return promoQRCodeImageUrl;
    }

    /**
     * This method sets the eventPromoId of an event.
     * @param promoQRCodeImageUrl the eventPromoId of an event.
     */
    public void setEventPromoId(String promoQRCodeImageUrl) {
        this.promoQRCodeImageUrl = promoQRCodeImageUrl;
    }

    /**
     * This method gets the eventId of an event.
     * @return  Return the eventId of an event.
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * This method sets the eventId of an event.
     * @param eventId the eventId of an event.
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * Convert the event title to String.
     * @return the event title.
     */
    @Override
    public String toString() {
        return eventTitle;
    }

    /**
     * Adds an attendee to the list of attendees for this event.
     * If the attendee is already in the list, their check-in time is incremented.
     * @param attendeeUserId The ID of the attendee to add.
     */
    public void addAttendee(String attendeeUserId, String userName) {
        long checkInTime = this.attendeeListIdToCheckInTimes.getOrDefault(attendeeUserId, 0L);
        this.attendeeListIdToCheckInTimes.put(attendeeUserId, checkInTime + 1);
        if (!this.attendeeListIdToName.containsKey(attendeeUserId)){
            this.attendeeListIdToName.put(attendeeUserId, userName);
        }
    }

    /**
     * Retrieves the attendee list for this event.
     * @return A hashmap containing attendee IDs as keys and their corresponding check-in times as values.
     */
    public HashMap<String, Long> getAttendeeListIdToCheckInTimes() { return this.attendeeListIdToCheckInTimes; }

    public HashMap<String, String> getAttendeeListIdToName() { return this.attendeeListIdToName; }


    /**
     * Checks if a given user ID is in the list of attendees for this event.
     * @param checkUserId The user ID to check.
     * @return True if the user is in the attendee list, false otherwise.
     */
    public boolean isAttendeeInThisEvent(String checkUserId){
        return this.attendeeListIdToCheckInTimes.containsKey(checkUserId);
    }

    /**
     * Retrieves the host ID for this event.
     * @return The host ID.
     */
    public String getHostId(){ return this.hostId; }

    /**
     * Sets the host ID for this event.
     * @param hostId The host ID to set.
     */
    public void setHostId(String hostId) { this.hostId = hostId; }

}