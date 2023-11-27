package com.example.boardgameapp.modul;

public class User {

    private String userName;
    private String passwort;
    private String surname;
    private String name;
    private String residence;
    private String street;
    private String streetNumber;

    public User(){}

    public User(String userName, String passwort, String surname, String name, String residence, String street, String streetNumber) {
        this.userName = userName;
        this.passwort = passwort;
        this.surname = surname;
        this.name = name;
        this.residence = residence;
        this.street = street;
        this.streetNumber = streetNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

}
