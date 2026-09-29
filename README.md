# eventually!

A local web-based todo application that helps users plan tasks, track deadlines, and monitor progress. Users can create private accounts, manage their own task lists, and save task information in a persistent local database.

## Features

### Required features

- Create a todo task with a description and deadline
- View tasks with their descriptions, deadlines, and completion statuses
- Mark pending tasks as completed
- Delete tasks that are no longer needed

### Additional features

- User registration and login
- Private task lists separated by authenticated user account
- Edit task descriptions and deadlines
- Search tasks by keyword
- Filter tasks by All, Pending, Completed, and Overdue status
- Sort tasks by deadline
- Optional task reminder date/time
- In-app upcoming-reminders banner
- Dashboard progress indicator

## Tech Stack

- **Java 21** - programming language and runtime
- **Spring Boot** - application framework and embedded server
- **Spring MVC** - web request handling and controller layer
- **Spring Security** - user registration, login, logout, password protection, and authorization
- **Spring Data JPA / Hibernate** - object-relational mapping and data persistence
- **H2 Database** - file-based relational database for local persistent storage
- **Thymeleaf** - server-side HTML template engine
- **HTML/CSS** - user interface and styling
- **Maven** - dependency management and build tool

## Architecture

The application follows a client–server architecture.

```text
Web Browser
    |
    v
Spring MVC Controllers
    |
    v
Service Layer
    |
    v
Spring Data JPA Repositories
    |
    v
H2 File-Based Database
```

- The browser renders Thymeleaf-generated HTML pages and submits forms to the Spring Boot server.
- Controllers receive HTTP requests and return the appropriate views.
- Services contain application business logic, including task operations and user-related behavior.
- Spring Data JPA repositories read and write application entities.
- The H2 database persists user accounts and tasks on the local machine.
- Spring Security authenticates users and ensures a user can access only their own tasks.

## Data Persistence

The application uses a file-based H2 database rather than an in-memory database.

The database connection is configured with a URL similar to:

```properties
jdbc:h2:file:./data/eventuallydb
```

This creates local database files in the project's `data` directory. User accounts and tasks remain available after the Spring Boot application is stopped and restarted.

Passwords are not stored as readable text. They are hashed with BCrypt before being saved.

## Prerequisites

Install the following before running the application:

- Java Development Kit (JDK) 21
- A web browser
- Windows PowerShell or another terminal

Verify Java is installed:

```powershell
java -version
```

The output should show Java 21.

## Run Locally

1. Open PowerShell in the top-level `hw1-task` folder.

2. Start the application:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

3. Wait until the console reports that Spring Boot has started.

4. Open the application in a browser:

   ```text
   http://localhost:8080
   ```

5. Select **Create an account**, register with a username and password, and then sign in.

6. Use the dashboard to add, edit, search, filter, sort, complete, and delete tasks.

7. Stop the local server when finished by pressing:

   ```text
   Ctrl + C
   ```

   If PowerShell displays `Terminate batch job (Y/N)?`, type `Y` and press Enter.

## Manual Testing

The application was manually tested locally using two separate user accounts.

| Feature | Test performed | Result |
|---|---|---|
| Create task | Created a task with a description and deadline | Passed |
| View task list | Verified descriptions, deadlines, and statuses display correctly | Passed |
| Mark complete | Marked a pending task complete and verified status and progress update | Passed |
| Delete task | Deleted a task and verified it was removed from the list | Passed |
| Edit task | Updated a task description and deadline | Passed |
| Search | Searched for a unique task keyword | Passed |
| Filter | Tested All, Pending, Completed, and Overdue filters | Passed |
| Sort | Tested deadline sorting options | Passed |
| Reminders | Created tasks with upcoming reminders and verified the dashboard banner | Passed |
| Account isolation | Created two accounts and verified each account saw only its own tasks | Passed |
| Persistence | Restarted the application and verified accounts and tasks remained | Passed |

## Notes

This project is designed to run locally. It does not require deployment, an external database server, an email address, or a phone number. The H2 database runs locally with the Spring Boot application.
