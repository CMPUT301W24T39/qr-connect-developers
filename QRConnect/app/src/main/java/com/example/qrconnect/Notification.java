package com.example.qrconnect;

/**
 * The Notification class defines a notification.
 * Contains details such as event, title, and description.
 */
public class Notification{
    private String event;
    private String title;
    private String description;

    /**
     * Constructs a Notification object with the specified details.
     * @param event the name of the event of the notification.
     * @param title the title of the notification.
     * @param description the description of the notification.
     */
    Notification(String event, String title, String description){
        this.event = event;
        this.title = title;
        this.description = description;
    }

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
}