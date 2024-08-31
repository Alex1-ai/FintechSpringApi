
# FintechSpringApi

![Java](https://img.shields.io/badge/Java-11+-brightgreen)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.5.4-brightgreen)
![Maven](https://img.shields.io/badge/Maven-3.6.3-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-12+-blue)
![JPA](https://img.shields.io/badge/JPA-Spring%20Data%20JPA-orange)
![Spring Security](https://img.shields.io/badge/Spring%20Security-Enabled-red)
![License](https://img.shields.io/badge/License-MIT-blue)

![bank](https://github.com/user-attachments/assets/fda51c82-1efa-4bb6-a741-1886054b2964)

![bank2](https://github.com/user-attachments/assets/aae13e86-da3e-4f13-bdb3-bb38a8887f70)

## Overview

FintechSpringApi is a robust RESTful API built with Spring Boot that simulates core banking operations such as debit, credit, and account transfers. The API also provides functionalities for generating and sending balance statements to customers via email notifications. Additionally, it supports the creation of users and accounts, ensuring a seamless banking experience.

## Features

- **Debit Operations:** Withdraw money from a customer's account.
- **Credit Operations:** Deposit money into a customer's account.
- **Account Transfers:** Transfer funds between accounts.
- **Balance Statements:** Generate and send detailed balance statements to customers via email.
- **User & Account Management:** Create and manage user accounts.
- **Email Notifications:** Integrated email service for sending transaction alerts and balance statements.
- **Security:** Spring Security implemented for securing API endpoints.
- **Database Management:** Utilizes PostgreSQL with JPA for database interactions.

## Technologies Used

- **Java 11+**: The programming language used for building the API.
- **Spring Boot**: Framework used for creating the RESTful API.
- **Spring Data JPA**: For interacting with the PostgreSQL database.
- **Spring Security**: For securing the API endpoints.
- **Maven**: Dependency management.
- **PostgreSQL**: Database for storing user and transaction data.
- **iTextPDF**: For generating PDF statements.
- **Spring Boot Mail**: For sending email notifications to customers.

## Installation

### Prerequisites

- Java 11 or higher
- Maven 3.6.3 or higher
- PostgreSQL database
- An email SMTP server for sending notifications

### Clone the Repository

```bash
git clone https://github.com/Alex1-ai/FintechSpringApi.git
cd FintechSpringApi
```

### Configure Database

Update the `application.properties` or `application.yml` file with your PostgreSQL configuration:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### Configure Email SMTP Server

Update the `application.properties` or `application.yml` file with your SMTP configuration:

```properties
spring.mail.host=smtp.your-email-provider.com
spring.mail.port=587
spring.mail.username=your_email@example.com
spring.mail.password=your_email_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### Build and Run the Application

```bash
mvn clean install
mvn spring-boot:run
```

## API Endpoints

### User Management

- **POST /api/v1/users** - Create a new user.
- **GET /api/v1/users/{id}** - Retrieve a user by ID.
- **PUT /api/v1/users/{id}** - Update user details.
- **DELETE /api/v1/users/{id}** - Delete a user.

### Account Management

- **POST /api/v1/accounts** - Create a new account.
- **GET /api/v1/accounts/{accountNumber}** - Retrieve account details.

### Transaction Management

- **POST /api/v1/accounts/{accountNumber}/debit** - Debit an account.
- **POST /api/v1/accounts/{accountNumber}/credit** - Credit an account.
- **POST /api/v1/accounts/transfer** - Transfer funds between accounts.

### Balance Statement

- **POST /api/v1/accounts/{accountNumber}/statement** - Generate and send a balance statement to the account holder.

## Security

- **Spring Security**: All sensitive API endpoints are secured using Spring Security. Ensure proper authentication is provided when accessing these endpoints.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request.

1. Fork the repository
2. Create a feature branch (`git checkout -b feature-branch`)
3. Commit your changes (`git commit -m 'Add new feature'`)
4. Push to the branch (`git push origin feature-branch`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

For any inquiries or issues, please contact me at [alexanderemmanuel1719@gmail.com](mailto:alexanderemmanuel1719@gmail.com).

---

### Visit the GitHub Repository: [FintechSpringApi](https://github.com/Alex1-ai/FintechSpringApi.git)
```
