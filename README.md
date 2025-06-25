# Hospital Management System

This repository contains the Hospital Management System built using Java (Spring Boot) with MariaDB as the database.

## Prerequisites

- MariaDB installed and running
- IntelliJ IDEA (or any other Java IDE)
- Java 21
- Maven

## Database Setup
1. Source the provided database file in your mariadb shell:
   ```sh
   source path/to/hospital_db.sql
   ```

## Running the Application

1. Open IntelliJ IDEA and load the project.
2. Ensure the database connection details in `application.properties` match your MariaDB configuration and ensure to change your password.
3. Open the `Main` class and run the application.

The application should now be running and accessible as per the configured endpoints.

## Additional Notes

- Ensure all required dependencies are installed via Maven.
- If any database migrations are required, update the SQL file accordingly.

---
