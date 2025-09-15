# Java & Selenium Project
**Author:** Yuva Kiran Seerapu

## 📌 Project Title
Automated Login Form Testing Using Selenium

## 📖 Description
This project automates the testing of a login form on the demo website:  
[https://the-internet.herokuapp.com/login](https://the-internet.herokuapp.com/login)

The objective is to create **end-to-end automated test cases** that simulate different login scenarios and validate the results using **Selenium WebDriver in Java** with **TestNG**.

The framework also supports **data-driven testing** using a CSV file for credentials and can be integrated with **GitHub Actions** for CI/CD.

---

## ✅ Test Cases Implemented

1. **Valid Login**
    - Username: `tomsmith`
    - Password: `SuperSecretPassword!`
    - Asserts successful login message.

2. **Invalid Login**
    - Username: `wrongusername`
    - Password: `wrongpassword`
    - Asserts error message: `Your username is invalid!`

3. **Empty Fields**
    - No input in username & password fields.
    - Asserts error message (invalid username).

4. **Data-driven Testing with CSV**
    - Reads credentials from `src/test/resources/data/credentials.csv`
    - Executes multiple login attempts with different datasets.

---

## 🛠 Tools & Technologies

- **Language:** Java 21+
- **Build Tool:** Maven 3+
- **Test Framework:** TestNG
- **Automation Tool:** Selenium WebDriver
- **Driver Management:** WebDriverManager
- **Browser:** Chrome (latest version)
- **Data-driven Source:** CSV (via Apache Commons CSV / OpenCSV)
- **Reports:** TestNG HTML reports & Surefire reports
- **CI/CD:** GitHub Actions

---

## 📂 Project Structure  

Java_Selenium_Project/
│── pom.xml
│── testng.xml
│── README.md
│── Java_Selenium_Project.pdf
│
├── src
│ └── test
│ ├── java
│ │ └── tests
│ │ └── LoginTest.java
│ │
│ └── resources
│ └── data
│ └── credentials.csv
│
├── target
│ ├── surefire-reports
│ └── test-classes


---

## ⚙️ Setup Instructions

1. Install **Java JDK (21 or higher)**.
2. Install **Maven** and ensure it is added to your system `PATH`.
   ```bash
   mvn -v
