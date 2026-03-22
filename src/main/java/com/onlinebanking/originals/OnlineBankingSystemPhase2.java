
import java.io.*;
import java.util.*;
class User implements Serializable {
 private String username;
 private String password;
 private double balance;
 private List<String> transactions;
 public User(String username, String password) {
 this.username = username;
 this.password = password;
 this.balance = 0.0;
 this.transactions = new ArrayList<>();
 }
 public String getUsername() { return username; }
 public boolean checkPassword(String password) { return this.password.equals(password); }
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
 System.out.println("Transaction history for " + username + ":");
 for (String t : transactions) {
 System.out.println(t);
 }
 }
 public double getBalance() { return balance; }
}
class BankSystem {
 private HashMap<String, User> users;
 private static final String FILE_NAME = "users_phase2.dat";
 public BankSystem() {
 users = loadUsers();
 }
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
 System.out.println("User already exists!");
 } else {
 users.put(username, new User(username, password));
 saveUsers();
 System.out.println("Registration successful!");
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
 public void saveData() { saveUsers(); }
}
public class OnlineBankingSystemPhase2 {
 public static void main(String[] args) {
 Scanner sc = new Scanner(System.in);
 BankSystem bank = new BankSystem();
 while (true) {
 System.out.println("\n--- Online Banking System (Phase 2) ---");
 System.out.println("1. Register");
 System.out.println("2. Login");
 System.out.println("3. Exit");
 System.out.print("Choose option: ");
 int choice = sc.nextInt();
 sc.nextLine();
 if (choice == 1) {
 System.out.print("Enter username: ");
 String user = sc.nextLine();
 System.out.print("Enter password: ");
 String pass = sc.nextLine();
 bank.register(user, pass);
 } else if (choice == 2) {
 System.out.print("Enter username: ");
 String user = sc.nextLine();
 System.out.print("Enter password: ");
 String pass = sc.nextLine();
 User u = bank.login(user, pass);
 if (u != null) {
 System.out.println("Login successful!");
 boolean loggedIn = true;
 while (loggedIn) {
 System.out.println("\n1. Deposit");
 System.out.println("2. Withdraw");
 System.out.println("3. Balance");
 System.out.println("4. Transactions");
 System.out.println("5. Logout");
 System.out.print("Choose option: ");
 int opt = sc.nextInt();
 switch (opt) {
 case 1:
 System.out.print("Enter amount: ");
 double dep = sc.nextDouble();
 u.deposit(dep);
 System.out.println("Deposited " + dep);
 break;
 case 2:
 System.out.print("Enter amount: ");
 double with = sc.nextDouble();
 if (u.withdraw(with)) {
 System.out.println("Withdrew " + with);
 } else {
 System.out.println("Insufficient balance!");
 }
 break;
 case 3:
 System.out.println("Balance: " + u.getBalance());
 break;
 case 4:
 u.printTransactions();
 break;
 case 5:
 loggedIn = false;
 bank.saveData();
 System.out.println("Logged out!");
 break;
 }
 }
 } else {
 System.out.println("Invalid username or password!");
 }
 } else {
 System.out.println("Exiting...");
 break;
 }
 }
 sc.close();
 }
}