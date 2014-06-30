package com.evenup.sample.rest.accounts;

import java.io.File;

import jersey.repackaged.com.google.common.collect.ImmutableList;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;

/**
 * Wraps an underlying database and stores {@link Account}s.
 * <br><br>
 * Copyright 2014 EvenUp, Inc.
 * <br><br>
 * THIS CODE IS INTENDED AS AN EXAMPLE ONLY.  IT HAS NOT BEEN TESTED AND 
 * SHOULD NOT BE USED IN A PRODUCTION ENVIRONMENT.
 * <br><br>
 * THE  CODE IS  PROVIDED "AS  IS",  WITHOUT WARRANTY  OF ANY  KIND, EXPRESS  
 * OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE WARRANTIES  OF 
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *
 * @author Kevin G. McManus
 *
 */
public class AccountCollection {
    
    private DB db;
    private HTreeMap<String, Account> accountMap;

    public AccountCollection(String dbPath) {
        db = DBMaker.newFileDB(new File(dbPath))
                .closeOnJvmShutdown()
                .encryptionEnable("password")
                .make();
        accountMap = db.getHashMap("accounts");

    }

    public synchronized void add(Account account) {
        Account account2 = accountMap.get(account.getAccountNumber());
        if (account2 != null) {
            System.out.println("REPLACING EXISTING ACCOUNT: " + account.getAccountNumber());
        }
        accountMap.put(account.getAccountNumber(), account);
        db.commit();
    }
    
    public void close() {
        db.close();
    }
    
    public java.util.List<String> getAccountNumbers() {
        return ImmutableList.copyOf(accountMap.keySet());
    }
    
    public Account getForNumber(String accountNumber) {
        return accountMap.get(accountNumber);
    }
    
    public static void main(String[] args) {
        
        Account account = new Account("ACCOUNT_GUID", "12345", "token", "Joe", "Schmoe", null, null, null);
        AccountCollection accountCollection = new AccountCollection("/tmp/accountDB");
        accountCollection.add(account);
        accountCollection.close();
       
    }
    
}
