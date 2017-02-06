/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "users")
public class User {

    @Id
    private String userId;
    private String firstName;
    private String lastName;
    @Indexed(unique = true)
    private String email;
    private Date dob;
    private Character gender;
    private GeoLocation location;
    private List<String> favouriteSalads;
    private List<String> customSalads;
    @Transient
    private String password;
    @JsonIgnore
    private byte[] hashedPwd;

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public GeoLocation getLocation() {
        return location;
    }

    public void setLocation(GeoLocation location) {
        this.location = location;
    }

    public List<String> getCustomSalads() {
        return customSalads;
    }

    public void setCustomSalads(List<String> customSalads) {
        this.customSalads = customSalads;
    }

    public List<String> getFavouriteSalads() {
        return favouriteSalads;
    }

    public void setFavouriteSalads(List<String> favouriteSalads) {
        this.favouriteSalads = favouriteSalads;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getHashedPwd() {
        return hashedPwd;
    }

    public void setHashedPwd(byte[] hashedPwd) {
        this.hashedPwd = hashedPwd;
    }

    public void updateUser(User user) {
        this.lastName = user.getLastName();
        this.dob = user.getDob();
        this.email = user.getEmail();
        this.gender = user.getGender();
        this.customSalads = user.getCustomSalads();
        this.favouriteSalads = user.getFavouriteSalads();
        this.password = user.getPassword();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userId != null ? !userId.equals(user.userId) : user.userId != null) return false;
        if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) return false;
        if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (dob != null ? !dob.equals(user.dob) : user.dob != null) return false;
        if (gender != null ? !gender.equals(user.gender) : user.gender != null) return false;
        if (favouriteSalads != null ? !favouriteSalads.equals(user.favouriteSalads) : user.favouriteSalads != null)
            return false;
        if (customSalads != null ? !customSalads.equals(user.customSalads) : user.customSalads != null) return false;
        return password != null ? password.equals(user.password) : user.password == null;

    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (dob != null ? dob.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (favouriteSalads != null ? favouriteSalads.hashCode() : 0);
        result = 31 * result + (customSalads != null ? customSalads.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
