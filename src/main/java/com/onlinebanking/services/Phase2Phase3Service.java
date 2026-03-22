package com.onlinebanking.service;

import org.springframework.stereotype.Service;
import java.util.*;
import com.onlinebanking.model.User;
import com.onlinebanking.model.Account;

@Service
public class Phase2Phase3Service {
    private Map<String, User> users = new HashMap<>();
    private Map<String, String> accountOwner = new HashMap<>();

    public Phase2Phase3Service() {
        User demo = new User("user", "pass");
        Account a1 = new Account("1001", "Savings");
        a1.deposit(5000);
        demo.getAccounts().add(a1);
        users.put(demo.getUsername(), demo);
        accountOwner.put("1001", demo.getUsername());
    }

    public boolean register(String username, String password) {
        if (users.containsKey(username)) return false;
        users.put(username, new User(username, password));
        return true;
    }

    public User authenticate(String username, String password) {
        User u = users.get(username);
        if (u != null && u.getPassword().equals(password)) return u;
        return null;
    }

    public User getUser(String username){ return users.get(username); }

    public boolean createAccount(String username, String accNum, String type) {
        if (accountOwner.containsKey(accNum)) return false;
        User u = users.get(username);
        if (u == null) return false;
        Account a = new Account(accNum, type);
        u.getAccounts().add(a);
        accountOwner.put(accNum, username);
        return true;
    }

    public boolean deposit(String accNum, double amount) {
        String owner = accountOwner.get(accNum);
        if (owner == null) return false;
        Account a = users.get(owner).getAccountByNumber(accNum);
        if (a == null) return false;
        a.deposit(amount);
        return true;
    }

    public boolean withdraw(String accNum, double amount) {
        String owner = accountOwner.get(accNum);
        if (owner == null) return false;
        Account a = users.get(owner).getAccountByNumber(accNum);
        if (a == null) return false;
        return a.withdraw(amount);
    }

    public boolean transfer(String fromAcc, String toAcc, double amount) {
        String ownerFrom = accountOwner.get(fromAcc);
        String ownerTo = accountOwner.get(toAcc);
        if (ownerFrom == null || ownerTo == null) return false;
        Account aFrom = users.get(ownerFrom).getAccountByNumber(fromAcc);
        Account aTo = users.get(ownerTo).getAccountByNumber(toAcc);
        if (aFrom == null || aTo == null) return false;
        return aFrom.transferTo(aTo, amount);
    }

    public Collection<User> allUsers() { return users.values(); }
}
