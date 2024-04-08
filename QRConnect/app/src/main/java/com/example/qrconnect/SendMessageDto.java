package com.example.qrconnect;

public class SendMessageDto {
    private String to;
    private NotificationBody notification;

    public SendMessageDto(String to, NotificationBody notification) {
        this.to = to;
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public NotificationBody getNotification() {
        return notification;
    }

    public void setNotification(NotificationBody notification) {
        this.notification = notification;
    }
}

