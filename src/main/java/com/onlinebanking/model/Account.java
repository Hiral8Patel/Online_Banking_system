package com.onlinebanking.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable {
    private String accountNumber;
    private String type;
    private double balance;
    private List<String> transactions = new ArrayList<>();

    public Account(){}

    public Account(String accountNumber, String type){
        this.accountNumber = accountNumber;
        this.type = type;
        this.balance = 0.0;
    }

    public String getAccountNumber(){ return accountNumber; }
    public String getType(){ return type; }
    public double getBalance(){ return balance; }
    public List<String> getTransactions(){ return transactions; }

    public void deposit(double amount){
        balance += amount;
        transactions.add("Deposited: " + amount);
    }

    public boolean withdraw(double amount){
        if (amount <= 0) return false;
        if (balance >= amount) {
            balance -= amount;
            transactions.add("Withdrew: " + amount);
            return true;
        }
        return false;
    }

    public boolean transferTo(Account other, double amount){
        if (withdraw(amount)){
            other.deposit(amount);
            transactions.add("Transferred " + amount + " to " + other.getAccountNumber());
            return true;
        }
        return false;
    }

    public String toString(){ return accountNumber + " (" + type + ") : " + balance; }
}
