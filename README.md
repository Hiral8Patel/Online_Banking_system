# Online_Banking_system

## 📌 Overview

The **Online Banking System** is a secure and scalable web application developed using **Spring Boot**. It simulates core banking functionalities such as account management, transactions, and user authentication, providing a real-world banking experience through a digital platform.

---

## ✨ Features

* 👤 **User Registration & Authentication**
  Secure login and signup functionality with validation.

* 🏦 **Account Management**
  Create and manage bank accounts with detailed information.

* 💸 **Fund Transfer**
  Transfer money between accounts with transaction tracking.

* 📜 **Transaction History**
  View detailed records of all past transactions.

* 🔐 **Security & Validation**
  Input validation and secure handling of user data.

* 📊 **Dashboard View**
  Overview of account balance and recent activities.

---

## 🛠️ Tech Stack

* **Backend:** Spring Boot
* **Frontend:** HTML, CSS, JavaScript (Thymeleaf templates)
* **Database:** MySQL
* **ORM:** Hibernate (JPA)
* **Build Tool:** Maven

---

## ⚙️ Installation & Setup

1. Clone the repository:

```
git clone https://github.com/your-username/online-banking-system.git
```

2. Navigate to the project folder:

```
cd online-banking-system
```

3. Configure the database in `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bank_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

4. Build and run the project:

```
mvn spring-boot:run
```

5. Open in browser:

```
http://localhost:8080
```

---

## 📂 Project Structure

```
/src/main/java        → Controllers, Services, Repositories
/src/main/resources   → application.properties, templates, static files
/templates            → Thymeleaf HTML pages
/static               → CSS, JS, images
```

---

## 🚀 Future Enhancements

* Two-factor authentication (2FA)
* Integration with payment gateways
* REST API support for mobile apps
* Role-based access (Admin/User)
* Advanced fraud detection system

---

## 👩‍💻 About Me

Hi, I’m **Hiral Patel**, a second-year engineering student specializing in **Artificial Intelligence & Data Science**.
I enjoy building backend systems, exploring real-world applications of software engineering, and working on impactful projects.
