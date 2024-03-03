package com.example.qrconnect;

public class Organizer extends User{


    public Organizer(String userID, String name, String contactInformation) {
        super(userID, name, contactInformation);
    }

    @Override
    public void uploadProfilePicture(String imagePath) {

    }

    @Override
    public void removeProfilePicture() {

    }

    public void createEvent(/* Event details */) {
        // Implementation for creating a new event
    }

    public String generateUniqueQRCode(/* Event details */) {
        // Implementation for generating a unique QR code for an event
        return ""; // Placeholder return
    }

    public void viewAttendeeList(/* Event ID */) {
        // Implementation for viewing the list of attendees for an event
    }

    public void sendNotifications(String message) {
        // Implementation for sending notifications to attendees
    }

    public void uploadEventPoster(String posterPath) {
        // Implementation for uploading an event poster
    }

    public void trackRealTimeAttendance(/* Event ID */) {
        // Implementation for tracking real-time attendance
    }

    public void shareQRCodeImage(/* QR code details */) {
        // Implementation for sharing a QR code image
    }

    public void generatePromotionQRCode(/* Event details */) {
        // Implementation for generating a promotional QR code
    }

    public void seeCheckInLocationOnMap(/* Event ID */) {
        // Implementation for seeing check-in locations on a map
    }

    public void seeAttendeeCheckInCount(/* Attendee ID */) {
        // Implementation for seeing the check-in count of an attendee
    }

    public void optionallyLimitAttendees(int limit) {
        // Implementation for optionally limiting the number of attendees
    }

}
