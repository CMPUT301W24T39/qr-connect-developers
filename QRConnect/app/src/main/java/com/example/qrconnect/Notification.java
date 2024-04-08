package com.example.qrconnect;

import java.util.Date;

/**
 * The Notification class defines a notification.
 * Contains details such as event, title, and description.
 */
public class Notification{
    private String event;
    private String title;
    private String description;
    private String date;
    private boolean read;
    private String eventId;
    private String type;

    /**
     * Constructs a Notification object with the specified details.
     * @param event the name of the event of the notification.
     * @param title the title of the notification.
     * @param description the description of the notification.
     * @param date the date of the notification.
     * @param read the read status of the notification.
     * @param eventId the event id associated with the notification.
     */
    Notification(String event, String title, String description, String date, boolean read, String eventId, String type){
        this.event = event;
        this.title = title;
        this.description = description;
        this.date = date;
        this.read = read;
        this.eventId = eventId;
        this.type = type;
    }

    // Notification getters
    /**
     * Getter for the notification event.
     * @return the name of the event of the notification.
     */
    public String getNotificationEvent(){
        return this.event;
    }

    /**
     * Getter for the notification title.
     * @return the title of the notification.
     */
    public String getNotificationTitle(){
        return this.title;
    }

    /**
     * Getter for the notification description.
     * @return the description of the notification.
     */
    public String getNotificationDescription(){
        return this.description;
    }

    /**
     * Getter for the notification date.
     * @return the date of the notification.
     */
    public String getNotificationDate(){
        return this.date;
    }

    /**
     * Getter for the notification read status.
     * @return the read status of the notification.
     */
    public boolean getNotificationRead(){
        return this.read;
    }

    /**
     * Getter for the notification event id.
     * @return the event id associated with the notification.
     */
    public String getNotificationEventId(){
        return this.eventId;
    }

    // Notification setters
    /**
     * Setter for the notification event.
     * @param event the name of the event of the notification.
     */
    public void setNotificationEvent(String event){
        this.event = event;
    }

    /**
     * Setter for the notification title.
     * @param title the title of the notification.
     */
    public void setNotificationTitle(String title){
        this.title = title;
    }

    /**
     * Setter for the notification description.
     * @param description the description of the notification.
     */
    public void setNotificationDescription(String description){
        this.description = description;
    }

    /**
     * Setter for the notification date.
     * @param date the description of the notification.
     */
    public void setNotificationDate(String date){
        this.date = date;
    }

    /**
     * Setter for the notification read status.
     * @param read the read status of the notification.
     */
    public void setNotificationRead(boolean read){ this.read = read; }

    /**
     * Setter for the notification event id.
     * @param eventId the event id associated with the notification.
     */
    public void setNotificationEventId(String eventId){ this.eventId = eventId; }

    public void setNotificationType(String type) {
        this.type = type;
    }

    public String getNotificationType() {
        return type;
    }
}