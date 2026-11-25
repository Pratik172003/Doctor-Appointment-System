package com.example.doctorappointment.Domain;

import java.io.Serializable;

public class DoctorsModel implements Serializable {

    private String Address;
    private String Biography;
    private int Id;
    private String Name;
    private String Picture;
    private String Special;
    private int Experiense;
    private String Cost;
    private String Data;
    private String Time;
    private String Location;
    private String Mobile;
    private String Patiens;
    private double Rating;
    private String Site;
    private String doctorUid;

    public DoctorsModel() {
    }

    public int getExperiense() {
        return Experiense;
    }

    public void setExperiense(int experiense) {
        Experiense = experiense;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getBiography() {
        return Biography;
    }

    public void setBiography(String biography) {
        Biography = biography;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    public String getSpecial() {
        return Special;
    }

    public void setSpecial(String special) {
        Special = special;
    }

    public String getCost() {
        return Cost;
    }

    public void setCost(String cost) {
        Cost = cost;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getPatiens() {
        return Patiens;
    }

    public void setPatiens(String patiens) {
        Patiens = patiens;
    }

    public double getRating() {
        return Rating;
    }

    public void setRating(double rating) {
        Rating = rating;
    }

    public String getDoctorUid() { return doctorUid; }

    public void setDoctorUid(String doctorUid) { this.doctorUid = doctorUid; }

    public String getSite() {
        return Site;
    }

    public void setSite(String site) {
        Site = site;
    }
}