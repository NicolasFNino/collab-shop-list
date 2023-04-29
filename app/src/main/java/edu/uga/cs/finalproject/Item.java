package edu.uga.cs.finalproject;


import androidx.annotation.NonNull;

public class Item {
    private String id = null;
    private String name = null;
    private String price = null;

    private String user;
    private boolean checked;

    public Item() {
        id = null;
        name = null;
        price = null;
        user = null;
        checked = true;
    }
    public Item( String name, String id) {
        this.id = id;
        this.name = name;
    }

    public String getItemId() {
        return id;
    }

    public void setItemId(String id) {
        this.id = id;
    }
    public String getItemName() {
        return name;
    }

    public void setItemName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setUser(String u) {this.user = u;}
    public String getUser() {return this.user;}
    public boolean getIsChecked() {
        return checked;
    }

    public void setIsChecked(boolean checked) {
        this.checked = checked;
    }

    @NonNull
    public String toString() {
        return "Item: " + name;
    }
}

