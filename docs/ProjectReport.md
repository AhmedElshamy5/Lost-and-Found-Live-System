# Campus Lost & Found Live Desk - PDF Report

## Architecture Summary

The project contains two JavaFX programs: a student client and an admin server. The admin server opens a TCP ServerSocket on port 5555. Each connected student client is handled by a separate ClientHandler thread. Clients send serialized Message objects to the server. When a report is submitted, the server validates it, stores it using ReportManager, and broadcasts a report snapshot to all connected clients.

## Main Screens

- Login screen
- Register screen
- Student dashboard screen
- Report lost/found item screen
- Browse reports screen
- Admin server console screen

## Reflection

The hardest part was coordinating JavaFX with background networking threads without freezing the GUI. The solution was to keep socket listening in background threads and update UI data through JavaFX-safe controller logic. If improved later, the project could add database persistence and image upload for item photos.

## Partner Contribution Template

- Partner 1: Client UI, login/register, report form, README.
- Partner 2: Server networking, client handler threads, admin console, UML/report.
