
# Reward Points Calculation Service

This Spring Boot application calculates monthly and total reward points for customers based on their transaction amounts. It provides RESTful APIs to fetch rewards and add new transactions.

## Problem Statement

Retailers want to reward customers based on their purchase amounts:

- For every dollar spent **over $50**, the customer receives **1 point**.
- For every dollar spent **over $100**, the customer receives an **additional 1 point** (i.e., 2 points for every dollar over $100).

## 🎯 Example Calculation

If a customer makes a transaction of **$120**:
- The first $50 gets **0 points**.
- The next $50 (from $51 to $100) gets **1 point per dollar → 50 points**.
- The last $20 (from $101 to $120) gets **2 points per dollar → 40 points**.

**Total = 50 + 40 = 90 points**

---
##💻 Tech Stack

- Java 17
- Spring Boot 3.5.3
- Maven
- JUnit 5 (unit & integration testing)
- Mockito
- RESTful Web Services
- Git

---

## 🚀 How to Run

1. Clone the repository:
   ```bash
     https://github.com/Naveenarunkumar/JavaAssignment.git
     cd reward-points-service
2. Build the project:
   ```bash
    mvn clean install
3. Run the application:
   ```bash
   mvn spring-boot:run
4. Access the APIs:
   ```bash
   http://localhost:8080/api/rewards
API Endpoints
1. GET /api/rewards
- Description: Fetch total and monthly reward points for all customers.
```bash
Response:
[
  {
    "customerId": "cust1",
    "monthlyRewards": {
      "March": 90,
      "February": 30,
      "January": 90
    },
    "totalRewards": 210
  },
  {
    "customerId": "cust2",
    "monthlyRewards": {
      "March": 30,
      "February": 90,
      "January": 30
    },
    "totalRewards": 150
  }
]
```
2. GET /api/rewards/{customerId}
- Description: Fetch total and monthly reward points for a specific customer.
Example:
```bash
GET /api/rewards/cust1
```
Response:
```bash
{
  "customerId": "cust1",
  "monthlyRewards": {
    "March": 90,
    "February": 30,
    "January": 90
  },
  "totalRewards": 210
}
```
```


##Tests
This project includes:

-Unit tests using Mockito and JUnit
-Tests for controllers and services
-Tests covers reward calculation logic for customer and Tests for REST API end points 
& HTTP Requests

To run tests:
```bash
mvn test
```


🧠 What’s Tested

🔸 RewardServiceTest
- ✅ Calculates rewards for all customers
- ✅ Calculates rewards for a specific customer
- ✅ Handles unknown or null customer IDs gracefully
- ✅ Ensures no rewards are returned for invalid input


🔸 RewardControllerTest
- ✅ Returns 200 OK with rewards for all customers
- ✅ Returns 204 No Content when no rewards exist
- ✅ Returns 200 OK for valid customer ID
- ✅ Returns 204 No Content for unknown customer ID
- ✅ Verifies interaction with the service layer using mock



## 📂 Project Structure
```bash
com.rewards.rewards_points_service
├── controller
│   └── RewardController.java
├── model
│   ├── Transaction.java
│   └── RewardResponse.java
├── service
│   └── RewardService.java
├── exception
│   ├── InvalidTransactionException.java
│   └── GlobalExceptionHandler.java
└── test
    ├── RewardController.java
    ├── RewardService.java
```
Error Handling
- InvalidTransactionException: Thrown for negative transaction amounts.
- Global exception handler maps this to a 400 Bad Request.
  
##👨‍💻 Author

S. Naveenarunkumar


Java Backend Developer












