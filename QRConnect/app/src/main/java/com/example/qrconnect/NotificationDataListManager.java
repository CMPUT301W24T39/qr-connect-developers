package com.example.qrconnect;

import java.util.ArrayList;

/**
 * The NotificationManager class manages the notifications of a user.
 */
public class NotificationDataListManager {
    private static NotificationDataListManager instance;
    private ArrayList<Notification> notificationsDataList;

    /**
     * The NotificationManager constructor.
     */
    private NotificationDataListManager() {
        notificationsDataList = new ArrayList<>();
    }

    /**
     * Getter for the instance of the NotificationManager.
     * @return instance of the NotificationManager.
     */
    public static synchronized NotificationDataListManager getInstance() {
        if (instance == null) {
            instance = new NotificationDataListManager();
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
