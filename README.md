<div align="center">

# Synapse Search 🧠✨

**Search Smarter, Not Harder. Your Intelligent Gateway to the Web's Knowledge.**

[![Java](https://img.shields.io/badge/Java-17-orange?logo=java\&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-Database-blue?logo=mysql\&logoColor=white)](https://www.mysql.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Contributions Welcome](https://img.shields.io/badge/Contributions-Welcome-success.svg)](../../issues)

</div>

---

## 🌟 Introduction

Welcome to **Synapse Search**!
This isn't just another search engine — it's a **smart research assistant** I built to cut through the noise of the internet.

Instead of dumping a list of links, Synapse Search **reads them for you** and provides a concise **summary powered by Google’s Gemini AI**.

This project is also a highlight of my **personal portfolio**, showcasing a full-stack application built with **Java & Spring Boot**.

---

## 🚀 Key Features

* **🧠 AI-Powered Summaries** → Clear, easy-to-understand answers instead of just links.
* **🔍 Real-Time Web Search** → Always up-to-date results with Google Search API.
* **💡 Smart Suggestions** → Autocomplete-like query suggestions.
* **🌐 Transparent Sources** → See exactly which sites were used.
* **❓ Follow-Up Questions** → Get AI-suggested related queries.
* **🗣️ Multi-Language Support** → Instantly translate summaries into **Sinhala** or **Tamil**.
* **🔒 Secure Google Login** → Authentication powered by Google OAuth2.
* **📖 Search History** → Review and revisit past searches.
* **👤 Account Management** → Delete history or remove account easily.

---

## 🛠️ Tech Stack

| **Component**   | **Technology Used**                                        |
| --------------- | ---------------------------------------------------------- |
| **Backend**     | Java 17, Spring Boot 3.x                                   |
| **Database**    | MySQL                                                      |
| **Security**    | Spring Security (Google OAuth2)                            |
| **Build Tool**  | Gradle                                                     |
| **Frontend**    | HTML, CSS, Vanilla JavaScript, Thymeleaf                   |
| **AI & Search** | Google Gemini API, Google Custom Search API                |
| **Key Deps**    | spring-data-jpa, spring-security, mysql-connector-j, jsoup |

---

## ⚙️ Getting Started

Follow these steps to run **Synapse Search** locally.

### ✅ Prerequisites

* JDK **17+**
* Gradle **7.0+**
* MySQL Server (XAMPP/WAMP/Standalone)

---

### 🔧 Installation & Setup

1. **Clone the Repository**

   ```sh
   git clone https://github.com/hashan-7/ai-search-app.git
   cd ai-search-app
   ```

2. **Database Setup**

   ```sql
   CREATE DATABASE ai_search;
   ```

3. **Configure `application.properties`**
   ⚠️ This file is excluded via `.gitignore` to protect secrets. Create it manually at
   `src/main/resources/application.properties`:

   ```properties
   # Server Port
   server.port=8080

   # Google OAuth2 Login
   spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
   spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
   spring.security.oauth2.client.registration.google.scope=openid,profile,email

   # API Keys
   google.search.api.key=YOUR_GOOGLE_API_KEY
   google.search.engine.id=YOUR_SEARCH_ENGINE_ID
   gemini.api.key=YOUR_GEMINI_API_KEY

   # Database
   spring.datasource.url=jdbc:mysql://localhost:3306/ai_search?createDatabaseIfNotExist=true
   spring.datasource.username=root
   spring.datasource.password=your_mysql_password

   # Hibernate
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
   spring.jpa.show-sql=true
   ```

4. **Build & Run**

   ```sh
   ./gradlew bootRun
   ```

   App will be available at 👉 [http://localhost:8080](http://localhost:8080)

---

## 🤝 Contributing

Contributions, feature requests, and bug reports are always welcome!
Check out the issues page: `https://github.com/hashan-7/ai-search-app/issues`.

---

## 📄 License

This project is licensed under the **MIT License**.
See the `LICENSE` file for details.

---

<div align="center">
  Made with ❤️ by h7
</div>
