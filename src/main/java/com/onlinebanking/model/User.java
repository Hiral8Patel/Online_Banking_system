package com.onlinebanking.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String username;
    private String password;
    private List<Account> accounts = new ArrayList<>();

    public User(){}

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){ return username; }
    public String getPassword(){ return password; }
    public List<Account> getAccounts(){ return accounts; }

    public double getTotalBalance(){
        return accounts.stream().mapToDouble(Account::getBalance).sum();
    }

    public Account getAccountByNumber(String accNum){
        return accounts.stream().filter(a -> a.getAccountNumber().equals(accNum)).findFirst().orElse(null);
    }
}
