package com.example.qrconnect;

/**
 * This is the Organizer class is for an organizer, they are a user.
 * Initializes an organizer.
 */
public class Organizer extends User{

    /**
     * Constructor for the organizer class.
     * @param userID ID of the user (organizer).
     * @param name name of the user (organizer).
     * @param contactInformation contact information of the user (organizer).
     */
    public Organizer(String userID, String name, String contactInformation) {
        super(userID, name, contactInformation);
    }

    /**
     * Uploads a profile picture for the organizer (user).
     * This method is not implemented in this class.
     * @param imagePath The path to the image.
     */
    @Override
    public void uploadProfilePicture(String imagePath) {

    }

    /**
     * Removes the uploaded profile picture of the organizer (user).
     * This method is not implemented in this class.
     */
    @Override
    public void removeProfilePicture() {

    }

    // FUTURE IMPLEMENTATION:

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
