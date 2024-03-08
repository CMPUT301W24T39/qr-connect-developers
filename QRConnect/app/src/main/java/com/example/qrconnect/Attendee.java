package com.example.qrconnect;

public class Attendee extends User{
    public Attendee(String userID, String name, String contactInformation) {
        super(userID, name, contactInformation);
    }

    @Override
    public void uploadProfilePicture(String imagePath) {

    }

    @Override
    public void removeProfilePicture() {

    }

    public void checkInQrcodeScan(){

    }
}
