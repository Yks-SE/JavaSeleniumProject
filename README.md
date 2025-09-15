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

# 1. configure git (if needed)
git config --global user.name "YuvaKiran"
git config --global user.email "youremail@example.com"

# 2. init and push
cd C:\Users\bhanu\Yuva\Projects\Java_Selenium_Project
git init
git checkout -b main
git add .
git commit -m "Initial commit: Java Selenium Project with TestNG and CSV-driven tests"
git remote add origin https://github.com/<your-username>/Java_Selenium_Project.git
git push -u origin main

# 3. add workflow
# (create .github/workflows/maven.yml as shown, then:)
git add .github/workflows/maven.yml
git commit -m "Add GitHub Actions CI workflow"
git push

# 4. fix BaseTest, commit & push
# (apply BaseTest code above)
git add src/test/java/tests/BaseTest.java
git commit -m "Make BaseTest CI-friendly (headless on CI)"
git push
