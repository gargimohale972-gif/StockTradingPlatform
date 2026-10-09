import java.util.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Stock {
    private String symbol;
    private String companyName;
    private double price;

    public Stock(String symbol, String companyName, double price) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price > 0) {
            this.price = price;
        }
    }

    public void display() {
        System.out.printf("%-10s %-25s Rs. %.2f%n",
                symbol, companyName, price);
    }
}

class Transaction {
    private String type;
    private String symbol;
    private int quantity;
    private double price;
    private String date;

    public Transaction(String type, String symbol,
                       int quantity, double price) {
        this.type = type;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.date = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    public String toFileString() {
        return type + "|" + symbol + "|" + quantity + "|"
                + price + "|" + date;
    }

    public void display() {
        System.out.printf("%-8s %-10s %-8d Rs. %-10.2f %s%n",
                type, symbol, quantity, price, date);
    }
}

class User {
    private String name;
    private double cash;
    private Map<String, Integer> portfolio;
    private List<Transaction> transactions;

    public User(String name, double initialCash) {
        this.name = name;
        this.cash = initialCash;
        this.portfolio = new HashMap<>();
        this.transactions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public double getCash() {
        return cash;
    }

    public Map<String, Integer> getPortfolio() {
        return portfolio;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public boolean buyStock(Stock stock, int quantity) {
        if (quantity <= 0) {
            System.out.println("Quantity must be positive.");
            return false;
        }

        double cost = stock.getPrice() * quantity;

        if (cost > cash) {
            System.out.println("Insufficient balance!");
            return false;
        }

        cash -= cost;

        portfolio.put(
                stock.getSymbol(),
                portfolio.getOrDefault(stock.getSymbol(), 0)
                        + quantity
        );

        transactions.add(new Transaction(
                "BUY", stock.getSymbol(), quantity, stock.getPrice()
        ));

        System.out.println("Stock purchased successfully!");
        return true;
    }

    public boolean sellStock(Stock stock, int quantity) {
        if (quantity <= 0) {
            System.out.println("Quantity must be positive.");
            return false;
        }

        int owned = portfolio.getOrDefault(stock.getSymbol(), 0);

        if (owned < quantity) {
            System.out.println("You do not own enough shares!");
            return false;
        }

        cash += stock.getPrice() * quantity;
        int remaining = owned - quantity;

        if (remaining == 0) {
            portfolio.remove(stock.getSymbol());
        } else {
            portfolio.put(stock.getSymbol(), remaining);
        }

        transactions.add(new Transaction(
                "SELL", stock.getSymbol(), quantity, stock.getPrice()
        ));

        System.out.println("Stock sold successfully!");
        return true;
    }

    public double getPortfolioValue(Map<String, Stock> market) {
        double value = 0;

        for (String symbol : portfolio.keySet()) {
            Stock stock = market.get(symbol);

            if (stock != null) {
                value += stock.getPrice() * portfolio.get(symbol);
            }
        }

        return value;
    }

    public void displayPortfolio(Map<String, Stock> market) {
        System.out.println("\n========== MY PORTFOLIO ==========");

        System.out.printf("Available Cash: Rs. %.2f%n", cash);

        double investedValue = 0;

        if (portfolio.isEmpty()) {
            System.out.println("No stocks owned.");
        } else {
            System.out.printf("%-10s %-25s %-10s %-15s%n",
                    "Symbol", "Company", "Quantity", "Market Value");

            for (String symbol : portfolio.keySet()) {
                Stock stock = market.get(symbol);
                int quantity = portfolio.get(symbol);

                if (stock != null) {
                    double value = stock.getPrice() * quantity;
                    investedValue += value;

                    System.out.printf(
                            "%-10s %-25s %-10d Rs. %-12.2f%n",
                            symbol,
                            stock.getCompanyName(),
                            quantity,
                            value
                    );
                }
            }
        }

        System.out.printf("Stock Market Value: Rs. %.2f%n",
                investedValue);
        System.out.printf("Total Portfolio Value: Rs. %.2f%n",
                cash + investedValue);

        System.out.println("==================================");
    }

    public void displayTransactions() {
        System.out.println("\n========== TRANSACTION HISTORY ==========");

        if (transactions.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }

        System.out.printf("%-8s %-10s %-8s %-15s %s%n",
                "Type", "Symbol", "Quantity", "Price", "Date");

        for (Transaction transaction : transactions) {
            transaction.display();
        }
    }
}

public class StockTradingPlatform {
    private static final String PORTFOLIO_FILE = "portfolio.txt";
    private static final String TRANSACTION_FILE = "transactions.txt";

    private static final Scanner sc = new Scanner(System.in);

    private static final Map<String, Stock> market =
            new LinkedHashMap<>();

    private static User user;

    public static void main(String[] args) {
        initializeMarket();

        System.out.println("====================================");
        System.out.println("      STOCK TRADING PLATFORM");
        System.out.println("====================================");

        System.out.print("Enter your name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            name = "Investor";
        }

        user = new User(name, 100000.00);

        loadData();

        System.out.println("\nWelcome, " + user.getName() + "!");

        int choice;

        do {
            displayMenu();
            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    displayMarket();
                    break;

                case 2:
                    buyStock();
                    break;

                case 3:
                    sellStock();
                    break;

                case 4:
                    user.displayPortfolio(market);
                    break;

                case 5:
                    updateStockPrices();
                    break;

                case 6:
                    user.displayTransactions();
                    break;

                case 7:
                    saveData();
                    break;

                case 8:
                    saveData();
                    System.out.println("Thank you for using the platform!");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 8);

        sc.close();
    }

    private static void initializeMarket() {
        market.put("TCS",
                new Stock("TCS", "Tata Consultancy Services", 3500));
        market.put("INFY",
                new Stock("INFY", "Infosys", 1500));
        market.put("RELIANCE",
                new Stock("RELIANCE", "Reliance Industries", 1400));
        market.put("HDFCBANK",
                new Stock("HDFCBANK", "HDFC Bank", 1700));
        market.put("WIPRO",
                new Stock("WIPRO", "Wipro", 500));
    }

    private static void displayMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. Display Market Data");
        System.out.println("2. Buy Stocks");
        System.out.println("3. Sell Stocks");
        System.out.println("4. View Portfolio");
        System.out.println("5. Update Stock Prices");
        System.out.println("6. View Transaction History");
        System.out.println("7. Save Data");
        System.out.println("8. Exit");
        System.out.println("===============================");
    }

    private static void displayMarket() {
        System.out.println("\n========== STOCK MARKET ==========");
        System.out.printf("%-10s %-25s %s%n",
                "Symbol", "Company", "Price");

        for (Stock stock : market.values()) {
            stock.display();
        }
    }

    private static void buyStock() {
        displayMarket();

        System.out.print("Enter stock symbol to buy: ");
        String symbol = sc.nextLine().trim().toUpperCase();

        Stock stock = market.get(symbol);

        if (stock == null) {
            System.out.println("Stock not found!");
            return;
        }

        int quantity = readInt("Enter quantity: ");

        if (user.buyStock(stock, quantity)) {
            saveData();
        }
    }

    private static void sellStock() {
        user.displayPortfolio(market);

        System.out.print("Enter stock symbol to sell: ");
        String symbol = sc.nextLine().trim().toUpperCase();

        Stock stock = market.get(symbol);

        if (stock == null) {
            System.out.println("Stock not found!");
            return;
        }

        int quantity = readInt("Enter quantity: ");

        if (user.sellStock(stock, quantity)) {
            saveData();
        }
    }

    private static void updateStockPrices() {
        System.out.println("\n--- Update Stock Prices ---");
        System.out.println("Enter new prices for simulation.");

        for (Stock stock : market.values()) {
            System.out.printf("%s current price: Rs. %.2f%n",
                    stock.getSymbol(), stock.getPrice());

            double newPrice = readDouble(
                    "Enter new price (0 to keep unchanged): ");

            if (newPrice > 0) {
                stock.setPrice(newPrice);
            } else if (newPrice < 0) {
                System.out.println("Invalid price; unchanged.");
            }
        }

        saveData();
        System.out.println("Market prices updated!");
        user.displayPortfolio(market);
    }

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);

            try {
                int value = Integer.parseInt(sc.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private static double readDouble(String message) {
        while (true) {
            System.out.print(message);

            try {
                double value = Double.parseDouble(sc.nextLine().trim());

                if (Double.isFinite(value)) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // Handle invalid input below.
            }

            System.out.println("Please enter a valid number.");
        }
    }

    private static void saveData() {
        try (PrintWriter writer =
                     new PrintWriter(new FileWriter(PORTFOLIO_FILE))) {

            writer.println(user.getName());
            writer.println(user.getCash());

            for (Map.Entry<String, Integer> entry :
                    user.getPortfolio().entrySet()) {
                writer.println(entry.getKey() + "|" + entry.getValue());
            }

            System.out.println("Portfolio saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving portfolio: " + e.getMessage());
        }

        try (PrintWriter writer =
                     new PrintWriter(new FileWriter(TRANSACTION_FILE))) {

            for (Transaction transaction : user.getTransactions()) {
                writer.println(transaction.toFileString());
            }

        } catch (IOException e) {
            System.out.println("Error saving transactions: "
                    + e.getMessage());
        }
    }

    private static void loadData() {
        File file = new File(PORTFOLIO_FILE);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {

            String savedName = reader.readLine();
            String savedCash = reader.readLine();

            if (savedName == null || savedCash == null) {
                System.out.println("Saved portfolio is incomplete.");
                return;
            }

            double cash = Double.parseDouble(savedCash);

            if (!Double.isFinite(cash) || cash < 0) {
                System.out.println("Saved balance is invalid.");
                return;
            }

            Map<String, Integer> savedPortfolio = new HashMap<>();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                if (parts.length != 2) {
                    continue;
                }

                String symbol = parts[0];
                int quantity = Integer.parseInt(parts[1]);

                if (market.containsKey(symbol) && quantity > 0) {
                    savedPortfolio.put(symbol, quantity);
                }
            }

            user = new User(savedName, cash);
            user.getPortfolio().putAll(savedPortfolio);

            loadTransactions();

            System.out.println("Previous portfolio loaded successfully.");

        } catch (IOException | NumberFormatException e) {
            System.out.println("Could not load saved portfolio: "
                    + e.getMessage());
        }
    }

    private static void loadTransactions() {
        File file = new File(TRANSACTION_FILE);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 5);

                if (parts.length != 5) {
                    continue;
                }

                try {
                    String type = parts[0];
                    String symbol = parts[1];
                    int quantity = Integer.parseInt(parts[2]);
                    double price = Double.parseDouble(parts[3]);

                    if ((type.equals("BUY") || type.equals("SELL"))
                            && market.containsKey(symbol)
                            && quantity > 0
                            && Double.isFinite(price)
                            && price > 0) {

                        user.getTransactions().add(
                                new Transaction(type, symbol,
                                        quantity, price) {
                                    @Override
                                    public void display() {
                                        System.out.printf(
                                            "%-8s %-10s %-8d Rs. %-10.2f %s%n",
                                            type, symbol, quantity,
                                            price, parts[4]);
                                    }
                                }
                        );
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid transaction.");
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading transactions.");
        }
    }
}