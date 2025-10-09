
---

# 📚 Library Management System

A simple **Java + PostgreSQL** project built with **Gradle**.  
It manages books and members in a small library — supports adding, borrowing, and returning books.

---

## ⚙️ Features
- ➕ Add / remove / update books  
- 👤 Register / delete members  
- 📖 Borrow and return books  
- 🔄 Tracks book availability  
- 💾 Uses PostgreSQL with transactions (JDBC)

---

## 🧱 Project Structure
```

src/main/java/org/example/
├── model/          → Book.java, Member.java
├── repository/     → BookRepository, MemberRepository, BorrowRepository
├── service/        → LibraryService.java
├── DatabaseUtils.java  → Handles DB connection
├── Library.java        → Wrapper for service layer
└── App.java            → Main entry point

````

---

## 🗄️ Database Setup

1. **Create database**
   ```sql
   CREATE DATABASE mylibrary;


2. **Create tables**

   ```sql
   CREATE TABLE books (
       id SERIAL PRIMARY KEY,
       title VARCHAR(255),
       author VARCHAR(255),
       is_available BOOLEAN DEFAULT TRUE
   );

   CREATE TABLE members (
       id SERIAL PRIMARY KEY,
       name VARCHAR(255)
   );

   CREATE TABLE borrows (
       id SERIAL PRIMARY KEY,
       member_id INT REFERENCES members(id),
       book_id INT REFERENCES books(id),
       borrow_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       return_date TIMESTAMP
   );
   ````

3. **Update connection info** in `DatabaseUtils.java`

   ```java
   String url = "jdbc:postgresql://localhost:5432/mylibrary";
   String name = "myuser";
   String password = "mypassword";
   ````

---

## ▶️ Run the App

```bash
./gradlew run
```

---

## 🧩 Example Output

```
Added book: "The Hobbit"
Registered member: "Alice"
Alice borrowed "The Hobbit"
Alice returned "The Hobbit"
```

---

## 🧑‍💻 About

Small student project for learning **JDBC, SQL, and layered architecture** in Java using **Gradle**.

