package com.laundry.app.dto;

import java.io.Serializable;

public enum Role implements Serializable {
    CUSTOMER("1"),
    SHIPPER("2");

    private String role;

    Role(String role) {
        this.role = role;
    }

    public String role() {
        return role;
    }
}
