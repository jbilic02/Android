package com.example.mcqapp.Model;

//za parsiranje podataka koje dobijemo iz baze
public class Category {
    private String Name;
    private String Image;

    //prazan konstruktor, koji je potreban za Firebase-ovo automatsko mapiranje podataka
    public Category() {
    }

    public Category(String name, String image) {
        Name = name;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
