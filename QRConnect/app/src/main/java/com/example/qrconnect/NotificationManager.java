package com.example.qrconnect;

import java.util.ArrayList;

/**
 * The NotificationManager class manages the notifications of a user.
 */
public class NotificationManager {
    private static NotificationManager instance;
    private ArrayList<Notification> notificationsDataList;

    /**
     * The NotificationManager constructor.
     */
    private NotificationManager() {
        notificationsDataList = new ArrayList<>();
    }

    /**
     * Getter for the instance of the NotificationManager.
     * @return instance of the NotificationManager.
     */
    public static synchronized NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    /**
     * Shared notification data list.
     * @return the a list of notifications of the user.
     */
    public ArrayList<Notification> getNotificationsDataList() {
        return notificationsDataList;
    }
}
