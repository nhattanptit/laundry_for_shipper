package com.laundry.app.model;

import java.io.Serializable;

public class Product implements Serializable {

    private int productImage;
    private String productName;

    public int getProductImage() {
        return productImage;
    }

    public void setProductImage(int productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Product(int productImage, String productName) {
        this.productImage = productImage;
        this.productName = productName;
    }
}
