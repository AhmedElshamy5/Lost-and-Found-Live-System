package com.campuslostfound.model;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fullName;
    private String username;
    private String password;
    private String contact;

    public User(String fullName, String username, String password, String contact) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.contact = contact;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDisplayName() {
        if (fullName == null || fullName.isBlank()) {
            return username;
        }
        return fullName + " (@" + username + ")";
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof User user)) {
            return false;
        }
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
