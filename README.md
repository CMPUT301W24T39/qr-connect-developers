# QRConnect - QR Code Event Check-In System

QRConnect is an Android application designed to streamline event management by enabling attendees to check in using QR codes on their mobile devices. With QRConnect, organizers can effortlessly track attendance, manage event details, and send notifications in real-time. This README provides an overview of the system's features, scenarios, user stories, actors, and glossary.

## Features

### QR Code Scanning
- Attendees can easily check in by scanning event-specific QR codes.

### Firebase Integration
- Utilizes Firebase for storing event details, attendee lists, and real-time check-in status updates.

### Multi-User Interaction
- Distinguishes between organizers and attendees with different app roles and permissions.

### Geolocation Verification (Optional)
- Optionally verifies attendee presence at the event location using geolocation.

### Image Upload
- Allows organizers to upload event posters and attendees to upload profile pictures for personalization.

## Scenario

### Update for Project 3
John, an event organizer, utilizes QRConnect for his upcoming tech conference. He generates a unique QR code for the event and attendees sign up indicating their attendance. As attendees arrive, they scan the QR code for seamless check-ins, updating John's dashboard in real-time. John can also send push notifications to all attendees through the app.

### Scenario for Project 2
John, an event organizer, generates a unique QR code for his tech conference. Attendees use the app to scan the QR code for check-in, updating John's dashboard in real-time. He can also send push notifications to attendees through the app.

## Actors

### Organizer
- Responsible for event organization and control within the app.

### Attendee
- Individuals attending the event.

### Administrator
- Manages the infrastructure of the application.

## User Stories

### Organizer
- **US 01.01.01:** Create a new event and generate a unique QR code for attendee check-ins.
- **US 01.02.01:** View the list of attendees who have checked in to the event.
- **US 01.03.01:** Send notifications to all attendees through the app.
- **US 01.04.01:** Upload an event poster for visual information.
- **US 01.05.01:** Track real-time attendance and receive alerts.
- **US 01.06.01:** Share a generator QR code image to other apps.
- **US 01.07.01:** Generate a unique promotion QR code for events.
- **US 01.08.01:** View attendee check-in locations on a map.
- **US 01.09.01:** Track attendee check-in frequency.
- **US 01.10.01:** View the list of attendees signed up for an event.
- **US 01.11.01:** Optionally limit the number of attendees for an event.

### Attendee
- **US 02.01.01:** Quickly check into an event by scanning the QR code.
- **US 02.02.01:** Upload a profile picture for personalization.
- **US 02.02.02:** Remove profile pictures if necessary.
- **US 02.02.03:** Update profile information.
- **US 02.03.01:** Receive push notifications from organizers.
- **US 02.04.01:** View event details and announcements.
- **US 02.05.01:** Automatically generate a profile picture from the profile name.
- **US 02.06.01:** Access the app without login credentials.
- **US 02.07.01:** Sign up to attend an event.
- **US 02.08.01:** Browse event posters and details.
- **US 02.09.01:** View signed-up events.

### Both
- **US 03.02.01:** Enable or disable geolocation tracking for event verification.

### Administrator
- **US 04.01.01:** Remove events.
- **US 04.02.01:** Remove profiles.
- **US 04.03.01:** Remove images.
- **US 04.04.01:** Browse events.
- **US 04.05.01:** Browse profiles.
- **US 04.06.01:** Browse images.

## Directory Structure

## Directory Structure

The project root directory is `qr-connect-developers/`.

The project is located in `qr-connect-developers/QRConnect/`.

qr-connect-developers/ (Root Directory)
│
├── doc/                (Directory for Documentation)
│   └── [Documentation Files]
│
├── QRConnect/          (Main Project Directory)
│   └── [Project Files]
│
├── LICENSE             (License File)
│
└── README.md           (Readme File)

## Contributors

- [Billyisher](https://github.com/Billyisher) (CCID: qiao6)
- [Luwei-Lin](https://github.com/Luwei-Lin) (CCID: luwei2)
- [kevinfrito](https://github.com/kevinfrito) (CCID: thekkuda)
- [JillFerence](https://github.com/JillFerence) (CCID: jference)
- [TanmayJL](https://github.com/TanmayJL) (CCID: tlad)
- [wchorkaw](https://github.com/wchorkaw) (CCID: wchorkaw)


## Getting Started


## Feedback

We welcome feedback and contributions! Please submit any issues or suggestions via [IssueTracker](https://github.com/CMPUT301W24T39/qr-connect-developers/issues).

