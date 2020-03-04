package com.imn.firestoreexampleproject;

import java.io.Serializable;

public class User implements Serializable {
    String firstName;
    String lastName;
    String userID;
    String key;


    public User(String firstName, String lastName, String userID, String key) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userID = userID;
        this.key = key;
    }

    public User() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
