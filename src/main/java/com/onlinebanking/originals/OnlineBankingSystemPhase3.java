import java.io.*;
import java.util.*;

// Account class: represents a single bank account (Savings, Checking, etc.)
class Account implements Serializable {
    private String accountNumber;
    private String type;
    private double balance;
    private List<String> transactions;

    public Account(String accountNumber, String type) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
    }

    public void deposit(double amount) {
        balance += amount;
        transactions.add("Deposited: " + amount);
    }

    public boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            transactions.add("Withdrew: " + amount);
            return true;
        }
        return false;
    }

    public void printTransactions() {
        System.out.println("Transactions for " + type + " account (" + accountNumber + "):");
        if (transactions.isEmpty()) {
            System.out.println("  No transactions yet.");
        } else {
            for (String t : transactions) {
                System.out.println("  " + t);
            }
        }
    }

    public double getBalance() { return balance; }
    public String getType() { return type; }
    public String getAccountNumber() { return accountNumber; }
}

// User class: can have multiple accounts
class User implements Serializable {
    private String username;
    private String password;
    private List<Account> accounts;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.accounts = new ArrayList<>();
        // Add a default Savings account
        this.accounts.add(new Account(generateAccountNumber(), "Savings"));
    }

    // Simple account number generator (in real system, use UUID or DB)
    private String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis() % 1000000;
    }

    public void addAccount(String type) {
        accounts.add(new Account(generateAccountNumber(), type));
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}

// BankSystem: manages users and persistence
class BankSystem {
    private HashMap<String, User> users;
    private static final String FILE_NAME = "users_phase3.dat";

    public BankSystem() {
        users = loadUsers();
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, User> loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (HashMap<String, User>) ois.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register(String username, String password) {
        if (users.containsKey(username)) {
            System.out.println("❌ User already exists!");
        } else {
            users.put(username, new User(username, password));
            saveUsers();
            System.out.println("✅ Registration successful! Default Savings account created.");
        }
    }

    public User login(String username, String password) {
        if (users.containsKey(username)) {
            User user = users.get(username);
            if (user.checkPassword(password)) {
                return user;
            }
        }
        return null;
    }

    public void saveData() {
        saveUsers();
    }
}

// Main Application
public class OnlineBankingSystemPhase3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankSystem bank = new BankSystem();

        while (true) {
            System.out.println("\n🏦 --- Online Banking System (Phase 3: Multi-Account) ---");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");

            int choice = getIntInput(sc);
            if (choice == 1) {
                System.out.print("Enter username: ");
                String user = sc.nextLine().trim();
                System.out.print("Enter password: ");
                String pass = sc.nextLine();
                bank.register(user, pass);
            } else if (choice == 2) {
                System.out.print("Enter username: ");
                String user = sc.nextLine().trim();
                System.out.print("Enter password: ");
                String pass = sc.nextLine();
                User u = bank.login(user, pass);
                if (u != null) {
                    System.out.println("Login successful! Welcome, " + u.getUsername());
                    handleUserSession(sc, u, bank);
                } else {
                    System.out.println("Invalid username or password!");
                }
            } else if (choice == 3) {
                System.out.println("Exiting... Goodbye!");
                break;
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
        sc.close();
    }

    private static void handleUserSession(Scanner sc, User user, BankSystem bank) {
        while (true) {
            System.out.println("\nYour Accounts:");
            List<Account> accounts = user.getAccounts();
            for (int i = 0; i < accounts.size(); i++) {
                Account acc = accounts.get(i);
                System.out.printf("%d. %s Account (%s) - Balance: %.2f\n",
    i + 1, acc.getType(), acc.getAccountNumber(), acc.getBalance());
            }

            System.out.println("\n Account Actions:");
            System.out.println("1. Add New Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. View Transactions");
            System.out.println("5. Logout");
            System.out.print("Choose option: ");

            int opt = getIntInput(sc);
            switch (opt) {
                case 1:
                    System.out.print("Enter account type (e.g., Checking, Business): ");
                    String type = sc.nextLine();
                    user.addAccount(type);
                    bank.saveData();
                    System.out.println("✅ New " + type + " account created!");
                    break;

                case 2: // Deposit
                    Account depAcc = selectAccount(sc, accounts);
                    if (depAcc != null) {
                        System.out.print("Enter deposit amount: ");
                        double amount = getDoubleInput(sc);
                        if (amount > 0) {
                            depAcc.deposit(amount);
                            bank.saveData();
                            System.out.printf("Deposited %.2f into %s account.\n", amount, depAcc.getType());
                        } else {
                            System.out.println("Invalid amount.");
                        }
                    }
                    break;

                case 3: // Withdraw
                    Account withAcc = selectAccount(sc, accounts);
                    if (withAcc != null) {
                        System.out.print("Enter withdrawal amount: ");
                        double amount = getDoubleInput(sc);
                        if (amount > 0) {
                            if (withAcc.withdraw(amount)) {
                                bank.saveData();
                                System.out.printf("Withdrew %.2f from %s account.\n", amount, withAcc.getType());
                            } else {
                                System.out.println("Insufficient balance!");
                            }
                        } else {
                            System.out.println("Invalid amount.");
                        }
                    }
                    break;

                case 4: // View Transactions
                    Account transAcc = selectAccount(sc, accounts);
                    if (transAcc != null) {
                        transAcc.printTransactions();
                    }
                    break;

                case 5:
                    bank.saveData();
                    System.out.println("Logged out successfully.");
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static Account selectAccount(Scanner sc, List<Account> accounts) {
        if (accounts.isEmpty()) {
            System.out.println("No accounts available.");
            return null;
        }
        System.out.println("\nSelect an account:");
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            System.out.printf("%d. %s (%s)\n", i + 1, acc.getType(), acc.getAccountNumber());
        }
        System.out.print("Enter choice (1-" + accounts.size() + "): ");
        int choice = getIntInput(sc);
        if (choice >= 1 && choice <= accounts.size()) {
            return accounts.get(choice - 1);
        } else {
            System.out.println("Invalid selection.");
            return null;
        }
    }

    // Helper: safely read integer
    private static int getIntInput(Scanner sc) {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Helper: safely read double
    private static double getDoubleInput(Scanner sc) {
        try {
            return Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}