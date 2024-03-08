package com.example.qrconnect;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * The Event class defines an event.
 * Contains details such as title, date, time, location, capacity, announcement, and associated QR codes.
 */
public class Event {
    private String eventTitle;
    private Date date;
    private Time time;
    private String location;
    private Integer capacity;
    private String announcement;

    private Bitmap QRCodeImage;
    private Bitmap PromoQRCodeImage;

    private String eventCheckInId;

    private String eventPromoId;

    private String eventId;
    protected ArrayList<Attendee> AttendeeList;

    /**
     * Constructs an Event object with the specified details.
     * @param eventTitle the title of an event.
     * @param date the data of an event.
     * @param time the time of an event.
     * @param location the location of an event.
     * @param capacity the capacity of an event.
     * @param announcement the announcement of an event.
     * @param QRCodeImage the QR code of an event.
     * @param promoQRCodeImage the promotion QR code of an event.
     * @param eventCheckInId the id of the QR code of an event.
     * @param eventPromoId the id of the promotion QR code of an event.
     * @param eventId the id of an event.
     */
    public Event(String eventTitle, Date date, Time time, String location, Integer capacity, String announcement, Bitmap QRCodeImage, Bitmap promoQRCodeImage, String eventCheckInId, String eventPromoId, String eventId) {
        this.eventTitle = eventTitle;
        this.date = date;
        this.time = time;
        this.location = location;
        this.capacity = capacity;
        this.announcement = announcement;
        this.QRCodeImage = QRCodeImage;
        this.PromoQRCodeImage = promoQRCodeImage;
        this.eventCheckInId = eventCheckInId;
        this.eventPromoId = eventPromoId;
        this.eventId = eventId;
        this.AttendeeList = new ArrayList<>();
    }

    /**
     * Empty constructor for the Event class.
     */
    public Event(){};

    /**
     * This method gets the QR code of an event.
     * @return Return a QR code image.
     */

    public Bitmap getQRCodeImage() {
        return QRCodeImage;
    }

    /**
     * This method sets a QR code image to QRCodeImage attribute.
     * @param QRCodeImage the QR code of an event.
     */
    public void setQRCodeImage(Bitmap QRCodeImage) {
        this.QRCodeImage = QRCodeImage;
    }

    /**
     * This method gets the Promotion QR code of an event.
     * @return Return the promotion QR code image.
     */

    public Bitmap getPromoQRCodeImage() {
        return PromoQRCodeImage;
    }

    /**
     * This method sets a QR code image to QRCodeImage attribute.
     * @param promoQRCodeImage the promotion QR code of an event.
     */
    public void setPromoQRCodeImage(Bitmap promoQRCodeImage) {
        PromoQRCodeImage = promoQRCodeImage;
    }

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
    public Date getDate() {
        return date;
    }

    /**
     * This method sets the date of an event.
     * @param date the date of an event.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * This method gets the time of an event.
     * @return Return the time of an event.
     */
    public Time getTime() {
        return time;
    }

    /**
     * This method sets the time of an event.
     * @param time the time of an event.
     */
    public void setTime(Time time) {
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
        return eventCheckInId;
    }

    /**
     * This method sets the eventCheckInId of an event.
     * @param eventCheckInId the eventCheckInId of an event.
     */
    public void setEventCheckInId(String eventCheckInId) {
        this.eventCheckInId = eventCheckInId;
    }

    /**
     * This method gets the eventPromoId of an event.
     * @return  Return the eventPromoId of an event.
     */
    public String getEventPromoId() {
        return eventPromoId;
    }

    /**
     * This method sets the eventPromoId of an event.
     * @param eventPromoId the eventPromoId of an event.
     */
    public void setEventPromoId(String eventPromoId) {
        this.eventPromoId = eventPromoId;
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
     * @param attendee the attendee to add.
     */
    public void addAttendee(Attendee attendee) {
        AttendeeList.add(attendee);
    }
}