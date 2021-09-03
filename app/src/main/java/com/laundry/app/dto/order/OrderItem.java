package com.laundry.app.dto.order;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private String orderId;
    private String customerImage;
    private String customerName;
    private String orderCode;
    private String orderName;
    private String pickupDateTime;
    private String pickupAddress;
    private String deliveryDateTime;
    private String deliveryAddress;
    private String phoneNumber;

    public OrderItem(String orderId
            , String customerImage
            , String customerName
            , String orderCode
            , String orderName
            , String pickupDateTime
            , String pickupAddress
            , String deliveryDateTime
            , String deliveryAddress
            , String phoneNumber) {
        this.orderId = orderId;
        this.customerImage = customerImage;
        this.customerName = customerName;
        this.orderCode = orderCode;
        this.orderName = orderName;
        this.pickupDateTime = pickupDateTime;
        this.pickupAddress = pickupAddress;
        this.deliveryDateTime = deliveryDateTime;
        this.deliveryAddress = deliveryAddress;
        this.phoneNumber = phoneNumber;
    }

    public OrderItem(String orderId, String customerImage, String customerName, String orderCode, String orderName, String pickupDateTime, String pickupAddress, String deliveryDateTime, String deliveryAddress) {
        this.orderId = orderId;
        this.customerImage = customerImage;
        this.customerName = customerName;
        this.orderCode = orderCode;
        this.orderName = orderName;
        this.pickupDateTime = pickupDateTime;
        this.pickupAddress = pickupAddress;
        this.deliveryDateTime = deliveryDateTime;
        this.deliveryAddress = deliveryAddress;
    }

    public OrderItem(String deliveryAddress, String phoneNumber) {
        this.deliveryAddress = deliveryAddress;
        this.phoneNumber = phoneNumber;
    }

    public OrderItem() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerImage() {
        return customerImage;
    }

    public void setCustomerImage(String customerImage) {
        this.customerImage = customerImage;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getPickupDateTime() {
        return pickupDateTime;
    }

    public void setPickupDateTime(String pickupDateTime) {
        this.pickupDateTime = pickupDateTime;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getDeliveryDateTime() {
        return deliveryDateTime;
    }

    public void setDeliveryDateTime(String deliveryDateTime) {
        this.deliveryDateTime = deliveryDateTime;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
