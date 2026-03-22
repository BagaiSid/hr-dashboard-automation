# HR Dashboard - Selenium Automation Tests

Automated UI testing project for the HR Dashboard application using **Selenium WebDriver**, **TestNG**, and **Maven**.

## Project Structure

```
hr-dashboard-automation/
├── pom.xml
├── testng.xml
├── src/
│   ├── main/java/com/hrdashboard/
│   │   ├── config/
│   │   │   └── ConfigReader.java          # Reads config.properties
│   │   ├── driver/
│   │   │   └── DriverManager.java         # Thread-safe WebDriver management
│   │   ├── pages/
│   │   │   ├── BasePage.java              # Base class for all page objects
│   │   │   ├── LoginPage.java             # Login page actions & elements
│   │   │   ├── DashboardPage.java         # Dashboard page actions & elements
│   │   │   ├── EmployeePage.java          # Employee management page
│   │   │   └── LeavePage.java             # Leave management page
│   │   └── utils/
│   │       ├── ScreenshotUtil.java        # Screenshot capture on failure
│   │       └── WaitUtil.java              # Explicit wait helpers
│   └── main/resources/
│       └── config.properties              # Test configuration
└── src/test/java/com/hrdashboard/
    ├── base/
    │   └── BaseTest.java                  # Setup/teardown for all tests
    └── tests/
        ├── LoginTest.java                 # Login feature tests
        ├── DashboardTest.java             # Dashboard feature tests
        ├── EmployeeTest.java              # Employee CRUD tests
        └── LeaveTest.java                 # Leave management tests
```

## Prerequisites

- **Java 17+**
- **Maven 3.8+**
- **Chrome / Firefox / Edge** browser installed

## Configuration

Edit `src/main/resources/config.properties` to update:

| Property            | Description                       | Default                              |
|---------------------|-----------------------------------|--------------------------------------|
| `base.url`          | HR Dashboard URL                  | `http://localhost:8080`              |
| `browser`           | Browser to use                    | `chrome`                             |
| `implicit.wait`     | Implicit wait in seconds          | `10`                                 |
| `explicit.wait`     | Explicit wait in seconds          | `15`                                 |
| `admin.username`    | Admin login username              | `admin`                              |
| `admin.password`    | Admin login password              | `password`                           |

You can also override any property via system properties:
```bash
mvn test -Dbrowser=firefox -Dbase.url=http://staging.example.com
```

## Running Tests

**Run all tests:**
```bash
mvn clean test
```

**Run a specific test class:**
```bash
mvn test -Dtest=LoginTest
```

**Run with a specific TestNG suite:**
```bash
mvn test -DsuiteXmlFile=testng.xml
```

## Test Coverage

| Module     | Tests                                                        |
|------------|--------------------------------------------------------------|
| Login      | Valid login, invalid credentials, empty fields, data-driven  |
| Dashboard  | Widgets, navigation, welcome message, logout                 |
| Employee   | List, search, add, edit, delete, validation                  |
| Leave      | Apply annual/sick leave, balance display, request status      |

## Design Patterns

- **Page Object Model (POM)** — each page has a dedicated class encapsulating elements and actions
- **Thread-safe WebDriver** — supports parallel test execution via `ThreadLocal`
- **Data-driven testing** — TestNG `@DataProvider` for parameterized tests
- **Automatic screenshots** — captured on test failure

## Reports

After running tests, find reports in:
- `target/surefire-reports/` — TestNG default reports
- `target/screenshots/` — Failure screenshots
