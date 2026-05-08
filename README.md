# Campus Lost & Found Live Desk

JavaFX GUI project for **Advanced Programming Applications - CS244**.

## Project Idea

Campus Lost & Found Live Desk is a JavaFX desktop application that helps students report lost and found items on campus. The application uses real TCP socket networking: multiple student clients can connect to a live admin server, submit reports, browse reports, and receive admin announcements.

## Why this project matches the assignment

| Assignment topic | Implementation in this project |
|---|---|
| Encapsulation | Model fields are private and accessed through getters/setters. |
| Polymorphism | `LostItemReport` and `FoundItemReport` override `getReportType()`. |
| Exceptions | `ValidationException` is used for invalid input and edge cases. |
| JavaFX components/layouts | FXML screens use BorderPane, VBox, GridPane, TableView, ListView, ComboBox, TextArea, Buttons, and Labels. |
| GUI events | Controllers handle login, registration, navigation, submit, refresh, broadcast, and mark returned actions. |
| Multithreading | Server uses a separate thread for each client; client listens on a background thread. |
| Networking | Client and server communicate through TCP sockets using `ObjectInputStream` and `ObjectOutputStream`. |

## Main Features

- Register and login screens.
- Student dashboard.
- Report lost or found item screen.
- Browse all reports in a TableView.
- Admin server screen.
- Real TCP client/server communication.
- Multiple clients can connect at the same time.
- Admin can broadcast announcements to all connected clients.
- Admin can mark selected reports as returned.
- Input validation with clear error messages.

## Required Screens

1. Login screen
2. Register screen
3. Student dashboard screen
4. Report item screen
5. Browse reports screen
6. Admin server screen

## How to Run

### Requirements

- JDK 17 or newer
- Maven
- Internet connection for the first Maven run so Maven can download JavaFX dependencies

### 1. Run the admin server first

Open a terminal in the project folder and run:

```bash
mvn clean javafx:run -Djavafx.mainClass=com.campuslostfound.server.ServerApp
```

Click **Start Server**. The default port is `5555`.

### 2. Run a client

Open a second terminal in the project folder and run:

```bash
mvn javafx:run -Djavafx.mainClass=com.campuslostfound.client.ClientApp
```

You can register a new user, or use the default demo user:

- Username: `demo`
- Password: `demo123`

### 3. Test multiple clients

Open a third terminal and run the client command again. Log in as another user and connect to the same server.

## Suggested Demo Steps

1. Start the admin server.
2. Open Client 1 and login/register.
3. Connect Client 1 to `localhost:5555`.
4. Submit a lost item report.
5. Open Client 2 and connect to the same server.
6. Submit a found item report.
7. Check that both clients can refresh and see the reports.
8. Use the admin screen to broadcast a message.
9. Mark one selected report as returned.

## Main Classes

- `User`
- `ItemReport`
- `LostItemReport`
- `FoundItemReport`
- `ReportManager`
- `ReportValidator`
- `LocalUserStore`
- `Message`
- `LostFoundServer`
- `ClientHandler`
- `ClientNetworkService`

## Networking Design

The admin server starts a `ServerSocket`. Every connecting client gets its own `ClientHandler` thread. Clients send serialized `Message` objects to the server. When a student submits a report, the server stores it in `ReportManager` and broadcasts the updated report list to all connected clients.

## Edge Cases Handled

- Empty username/password during login.
- Duplicate username during registration.
- Empty item name, category, location, or description.
- Invalid port number.
- Trying to submit a report while disconnected.
- Server disconnects while the client is running.

## GitHub Collaboration Notes

To satisfy the collaboration part of the rubric, push this project to a public GitHub repository and make commits regularly. Suggested feature branches:

- `feature/server-networking`
- `feature/client-screens`
- `feature/report-table`
- `feature/validation`

Use meaningful commit messages such as:

- `Add JavaFX login and register screens`
- `Implement TCP server and client handler threads`
- `Add report form validation`
- `Connect TableView to live report list`

## Important Note

This project stores users and reports in memory to keep the project focused on JavaFX, OOP, sockets, and threading. Restarting the server clears the report list.
