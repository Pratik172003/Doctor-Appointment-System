package com.example.doctorappointment.Domain;

public class CategoryModel {
    private int id;
    private String Name;
    private String Picture;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    public CategoryModel() {
    }
}
