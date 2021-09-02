package com.laundry.app.dto.ordercreate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrderDto implements Serializable {

    @SerializedName("distance")
    @Expose
    private String distance;

    @SerializedName("orderServiceDetailForms")
    @Expose
    private List<OrderServiceDetailForm> orderServiceDetailForms;

    @SerializedName("serviceId")
    @Expose
    private int serviceId;

    @SerializedName("shippingAddress")
    @Expose
    private String shippingAddress;

    public OrderDto() {
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setOrderServiceDetailForms(List<OrderServiceDetailForm> orderServiceDetailForms) {
        this.orderServiceDetailForms = orderServiceDetailForms;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
