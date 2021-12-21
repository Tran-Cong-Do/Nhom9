package com.nhom9.orderfoodserver.Model;

//package model nay nó là để khai báo cho các thuộc tính như là món ăn hay loại món ăn, đặt hàng,....

public class Category {
    private String Name;
    private String Image;

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

    public Category() {
    }
}
