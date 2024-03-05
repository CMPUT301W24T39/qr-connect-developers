package com.example.qrconnect;

import android.widget.ImageView;

import java.sql.Time;
import java.util.Date;

public class Event {
    private String eventTitle;
    private Date date;
    private Time time;
    private String location;
    private Integer capacity;
    private String announcement;

    private ImageView QRCodeImage;
    private ImageView PromoQRCodeImage;

    private Integer eventCheckInId;

    private Integer eventPromoId;

    public ImageView getQRCodeImage() {
        return QRCodeImage;
    }

    public void setQRCodeImage(ImageView QRCodeImage) {
        this.QRCodeImage = QRCodeImage;
    }

    public ImageView getPromoQRCodeImage() {
        return PromoQRCodeImage;
    }

    public void setPromoQRCodeImage(ImageView promoQRCodeImage) {
        PromoQRCodeImage = promoQRCodeImage;
    }


    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }


    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public Integer getEventCheckInId() {
        return eventCheckInId;
    }

    public void setEventCheckInId(Integer eventCheckInId) {
        this.eventCheckInId = eventCheckInId;
    }

    public Integer getEventPromoId() {
        return eventPromoId;
    }

    public void setEventPromoId(Integer eventPromoId) {
        this.eventPromoId = eventPromoId;
    }

    @Override
    public String toString() {
        return eventTitle;
    }
}