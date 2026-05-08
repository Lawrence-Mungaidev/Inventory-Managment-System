## INVENTORY MANAGEMENT SYSTEM
A backend Rest API system built on Spring Boot to help small retail owners to manage products and track stock in real time. The System has two roles, ADMIN and CASHIER, which their roles are clearly defined and has automated notifications.

Live Demo

Swagger UI: https://inventory-managment-system-production.up.railway.app/swagger-ui/index.html

![Swagger UI](Images/swaggerUi.png)

## ROLES

## ADMIN

- Creates and manages Cashiers, Products, Categories, Suppliers and Stocks
- Approves or rejects Restock Requests from Cashiers
- Approves or rejects Stock Adjustment Requests (theft, damage, etc.)
- Receives low stock notifications and daily/monthly sales reports

## CASHIER

- Sells products via Cash or M-Pesa STK Push
- Submits Restock Requests which requires Admin approval
- Submits Stock Adjustment Requests for losses like theft or damage which requires Admin approval

## Features

- JWT Authentication – Secure role-based access
- Stock Management – Full stock flow tracking with approvals
- Stock Adjustments – Track theft, damage and other losses
- Low Stock Notification – Automatically notifies the admin for low stock
- M-Pesa STK Push – Integrated M-Pesa for mobile payments
- Daily Email Report – Automatically sends a daily summary of sales
- Monthly Report – Profit, loss and monthly sales
- Daily Report – A daily breakdown of sales
- Swagger Documentation – Full interactive API documentation
## TECH STACK
| Technology | Purpose |
|------------|---------|
| Java 21 | Programming language |
| Spring Boot | Backend framework |
| MySQL | Database |
| Spring Security & JWT | Authentication and authorization |
| M-Pesa STK Push | STK Push payments |
| Java Mail Sender | Email notifications |
| Swagger | API documentation |
| Railway | Deployment |

## ARCHITECTURE
## System Entities 
| Entity | Description |
|--------|-------------|
| User | Admin and Cashier accounts |
| Category | Product categories |
| Product | Individual products |
| Notification | System notifications |
| Supplier | Product suppliers |
| Stock | Stock record per product |
| Stock Adjustments | Theft, damage, and other loss records |
| Transaction | Sales records |
| TransactionItem | Individual items in a transaction |
| Mpesa | Payment records |

## Relationships
1.	One Category can have Many Products
2.	One User can create Many Notifications
3.	One User can create many Transactions
4.	One Transaction can have many TransactionItem
5.	One Supplier can bring Many Products
6.	One User can create many StockAdjustments
7.	One Product can be in Many TransactionItem
8.	One Product can be in Stock Many times

   
## API Endpoints
## Auth

| Method | Endpoint | Access |
|--------|----------|--------|
| POST | /api/auth/register | Admin Only |
| POST | /api/auth/logIn | Everyone |

## Notification

| Method | Endpoint | Access |
|--------|----------|--------|
| POST | /api/notifications/markasread/{notificationId} | Authenticated User |
| GET | /api/notifications | Authenticated User |
| GET | /api/notifications/{notificationId} | Authenticated User |

## Stock

| Method | Endpoint | Access |
|--------|----------|--------|
| POST | /api/stocks/create | Everyone |
| PATCH | /api/stocks/approve/{stockId} | Admin Only |
| PATCH | /api/stocks/reject/{stockId} | Admin Only |
| GET | /api/stocks | Admin Only |

## Report

| Method | Endpoint | Access |
|--------|----------|--------|
| GET | /api/report/today | Everyone |
| GET | /api/report/thismonth | Admin |
| GET | /api/report/daterange | Admin |

## Transactions

| Method | Endpoint | Access |
|--------|----------|--------|
| POST | /api/Transactions/create (supports both Cash and Mpesa STK push) | Everyone |
| GET | /api/Transactions | Admin Only |
| GET | /api/Transactions/{transactionId} | Admin Only |
| GET | /api/Transactions/today | Admin Only |
| GET | /api/Transactions/range | Admin Only |

## Mpesa

| Method | Endpoint | Access |
|--------|----------|--------|
| POST | /api/mpesa/callback | Server Only |

## Stock Adjustments

| Method | Endpoint | Access |
|--------|----------|--------|
| POST | /api/stockAdjustments/adjustrequest | Cashier |
| PATCH | /api/stockAdjustments/approve/{adjustmentId} | Admin Only |
| PATCH | /api/stockAdjustments/reject/{adjustmentId} | Admin Only |
| GET | /api/stockAdjustments | Admin Only |

## Setup & Installation
## Requirements
-	Java 21
-	Maven
- MySQL
## Steps
1.	Clone the Repository
Git clone https://github.com/Lawrence-Mungaidev/Inventory-Managment-System
2.	Configure environment variables — create an application.yaml or set the variables below
3.	Run the application
mvn spring-boot:run
4.	Access Swagger UI
http://localhost:8080/swagger-ui/index.html

## Environment Variables
## Database

| Variable | Description |
|----------|-------------|
| SPRING_DATASOURCE_URL | Database URL |
| SPRING_DATASOURCE_USERNAME | Database username |
| SPRING_DATASOURCE_PASSWORD | Database password |

## JWT

| Variable | Description |
|----------|-------------|
| JWT_SECRET | Secret key |
| JWT_EXPIRATION | Expiration time in milliseconds |

## Mail

| Variable | Description |
|----------|-------------|
| MAIL_USERNAME | Gmail address for sending notifications |
| MAIL_PASSWORD | Gmail password |

## Mpesa

| Variable | Description |
|----------|-------------|
| MPESA_CONSUMER_KEY | Daraja consumer key |
| MPESA_CONSUMER_SECRET | Daraja consumer secret |
| MPESA_CALLBACK_URL | Callback URL for STK push response |

## Deployment

The application is deployed on Railway with:

- Spring Boot service
- MySQL service (Railway managed database)
- Environment variables configured at service level


## Author
Lawrence Mungai

Backend Developer| Spring Boot | Java developer

LinkedIn: https://www.linkedin.com/in/lawrence-mungai-266a9130b

