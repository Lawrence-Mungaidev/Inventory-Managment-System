INVENTORY MANAGEMENT SYSTEM
A backend Rest API system built on Spring Boot to help small retail owners to manage products and track stock in real time. The System has two roles, ADMIN and CASHIER, which their roles are clearly defined and has automated notifications.
Live Demo
Swagger UI: https://inventory-managment-system-production.up.railway.app/swagger-ui/index.html

![Swagger UI](images/swagger-ui.png)

ROLES
ADMIN
•	Creates and manages Cashiers, Products, Categories, Suppliers and Stocks
•	Approves or rejects Restock Requests from Cashiers
•	Approves or rejects Stock Adjustment Requests (theft, damage, etc.)
•	Receives low stock notifications and daily/monthly sales reports
CASHIER
•	Sells products via Cash or M-Pesa STK Push
•	Submits Restock Requests which requires Admin approval
•	Submits Stock Adjustment Requests for losses like theft or damage which requires Admin approval
FEATURES
•	JWT Authentication – secure role-based access
•	Stock Management – Full stock flow tracking with approvals
•	Stock Adjustments – Track Theft, Damage and other losses
•	Low Stock Notification – Automatically notifies the admin for low stock
•	 Mpesa STK PUSH – Integrated mpesa for mobile payment
•	Daily Email Report – Automatically sends a daily summary of sales
•	Monthly Report – Profit, Loss and Monthly sales
•	Daily Report – A daily break down of sales
•	Swagger documentation – Full interactive Api documentation
TECH STACK
Technology	Purpose
Java 21	Programming language
SpringBoot	Backend 
MySql	Database
Spring Security and JWT	Authentication and authorization
Mpesa STK PUSH	STK PUSH payments
Java Mail Sender	Email notification
Swagger 	Api documentation
Railway	Deployment

ARCHITECTURE
System Entities 
Entity	Description
User	Admin and Cashiers Account
Category	Product categories
Product	Individual products
Notification	System Notifications
Supplier	Product supplier
Stock	Stock record per product
Stock Adjustments	Theft, Damage and other losses records
Transaction 	Sales records
TransactionItem	Individual items in transactions
Mpesa 	Payment records

Relationships
1.	One Category can have Many Products
2.	One User can create Many Notifications
3.	One User can create many Transactions
4.	One Transaction can have many TransactionItem
5.	One Supplier can bring Many Products
6.	One User can create many StockAdjustments
7.	One Product can be in Many TransactionItem
8.	One Product can be in Stock Many times
API Endpoints
Auth
Method	Endpoint	Access
Post	/api/auth/register	Admin Only
Post	/api/auth/logIn	Everyone

Notification
Method	Endpoint	Access
Post	/api/notifications/markasread/{notificationId}	Authenticated User
Get	/api/notifications	Authenticated User
Get	/api/notifications/{notificationId}		Authenticated User

Stock
Method	Endpoint	Access
Post	/api/stocks/create	Everyone
Patch	/api/stocks/approve/{stockId}	Admin only
Patch	/api/stocks/reject/{stockId}	Admin only
Get	/api/stocks	Admin only
Report
Method	Endpoint	Access
Get	/api/report/today	Everyone
Get	/api/report/thismonth	Admin
Get	/api/report/daterange	Admin

Transactions
Method	Endpoint	Access
Post	/api/Transactions/create   (supports both Cash and Mpesa STK push)	Everyone
Get	/api/Transactions	Admin only
Get	/api/Transactions/{transactionId}	Admin only
Get	/api/Transactions/today	Admin only
Get	/api/Transactions/range	Admin only

Mpesa
Method	End point	Access
Post	/api/mpesa/callback	Server only

Stock Adjustments
Method	End point	Access
Post	/api/stockAdjustments/adjustrequest	Cashier
Patch	/api/stockAdjustments/approve/{adjustmentId}	Admin Only
Patch	/api/stockAdjustments/reject/{adjustmentId}	Admin Only
Get	/api/stockAdjustments	Admin Only

Setup & Installation
Prerequisites
•	Java 21
•	Maven
•	MySQL
Steps
1.	Clone the Repository
Git clone https://github.com/Lawrence-Mungaidev/Inventory-Managment-System
2.	Configure environment variables — create an application.yaml or set the variables below
3.	Run the application
mvn spring-boot:run
4.	Access Swagger UI
http://localhost:8080/swagger-ui/index.html
Environment Variables
Database
Variable	Description
SPRING_DATASOURCE_URL	Database Url
SPRING_DATASOURCE_USERNAME	Database user name
SPRING_DATASOURCE_PASSWORD	Database password

JWT
Variable	Description
JWT_SECRET	Secret key
JWT_EXPIRATION	Expiration time in milliseconds

Mail
Variable	Description
MAIL_USERNAME	Gmail address for sending notifications
MAIL_PASSWORD	Gmail password

Mpesa
Variable	Description
MPESA_CONSUMER_KEY	Daraja consumer key
MPESA_CONSUMER_SECRET	Daraja consumer secret
MPESA_CALLBACK_URL	Call back url for STK push response

Deployment
The application is deployed on Railway with:
•	Spring boot service
•	MySQL service (Railway managed database)
•	Environment variables configured at service level


Author
Lawrence Mungai
Backend Developer| Spring Boot | Java developer
LinkedIn: https://www.linkedin.com/in/lawrence-mungai-266a9130b

