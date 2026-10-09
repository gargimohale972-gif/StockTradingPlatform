Stock Trading Platform

About the Project

The Stock Trading Platform is a Java-based console application that simulates a basic stock market environment. It allows users to view stock prices, buy and sell stocks, manage their portfolios, and track transactions.

The project uses Object-Oriented Programming (OOP) concepts and file handling to store portfolio information and transaction history.

Features

- Market Data Display: View available stocks and their prices.
- Buy Stocks: Purchase stocks using the available cash balance.
- Sell Stocks: Sell owned stocks and receive money.
- Portfolio Management: View owned stocks, quantities, available cash, and total portfolio value.
- Stock Price Updates: Manually update stock prices to simulate market changes.
- Transaction History: View records of stock purchases and sales.
- File Handling: Save and load portfolio data and transaction history.
- Input Validation: Handle invalid inputs and insufficient balances.

Technologies Used

- Java
- Object-Oriented Programming (OOP)
- Java Collections Framework
- File Handling (File I/O)
- Exception Handling

Stock Market Simulation

The application includes sample stocks such as:

- TCS – Tata Consultancy Services
- INFY – Infosys
- RELIANCE – Reliance Industries
- HDFCBANK – HDFC Bank
- WIPRO – Wipro

The initial cash balance is ₹1,00,000.

How to Run the Project

Prerequisites

- Java Development Kit (JDK)
- Visual Studio Code or any Java-supported IDE

Steps

1. Clone or download this repository.

2. Open the project folder in VS Code.

3. Open the terminal in the project folder.

4. Compile the Java program:
   
   javac StockTradingPlatform.java

5. Run the application:
   
   java StockTradingPlatform

6. Follow the menu displayed in the terminal to perform stock trading operations.

Project Structure

StockTradingPlatform/
├── StockTradingPlatform.java
├── README.md
├── portfolio.txt
└── transactions.txt

Note: The portfolio and transaction files are created when the application saves data.

OOP Concepts Used

- Classes and Objects: Represent stocks, users, and transactions.
- Encapsulation: Protect data using private variables and public methods.
- Collections: Manage stock details, portfolio holdings, and transaction records.
- Exception Handling: Handle invalid input and file-related errors.

Limitations

- This application uses simulated stock prices and does not connect to a real stock exchange.
- Stock prices are updated manually.
- It does not process real payments or actual stock market orders.

Future Enhancements

- Integrate live stock market data using an API.
- Add user registration and login.
- Use a database such as MySQL or Oracle.
- Add graphical charts for stock price history.
- Implement more detailed profit and loss tracking.

Author

Developed as a Java programming project to demonstrate Object-Oriented Programming, stock trading simulation, and file handling.
