package com.example.aditya.shopit.Model;

public class Item {
    private String Image,Name,Description,Price,Discount,MenuID,ItemId, AvailabilityFlag;

    public Item() {
    }

    public Item(String image, String name, String description, String price, String discount, String menuID, String itemId, String availabilityFlag) {
        Image = image;
        Name = name;
        Description = description;
        Price = price;
        Discount = discount;
        MenuID = menuID;
        ItemId = itemId;
        AvailabilityFlag = availabilityFlag;
    }

    public String getImage() {
        return Image;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getMenuID() {
        return MenuID;
    }

    public void setMenuID(String menuID) {
        MenuID = menuID;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getAvailabilityFlag() {
        return AvailabilityFlag;
    }

    public void setAvailabilityFlag(String availabilityFlag) {
        AvailabilityFlag = availabilityFlag;
    }
}
