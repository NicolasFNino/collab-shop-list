package edu.uga.cs.finalproject;


public class Item {
    private String id = null;
    private String name = null;
    private String price = null;
    private boolean checked;

    public Item() {
        id = null;
        name = null;
        price = null;
        checked = true;
    }
    public Item( String name, String id) {
        this.id = id;
        this.name = name;
        this.price = price;
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
    public boolean getIsChecked() {
        return checked;
    }

    public void setIsChecked(boolean checked) {
        this.checked = checked;
    }

    public String toString() {
        return "Item: " + name;
    }
}

