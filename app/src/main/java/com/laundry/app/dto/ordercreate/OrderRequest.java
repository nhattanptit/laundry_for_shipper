package com.laundry.app.dto.ordercreate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrderRequest implements Serializable {

    @SerializedName("distance")
    @Expose
    public double distance;

    @SerializedName("serviceId")
    @Expose
    public int serviceId;

    @SerializedName("totalShipFee")
    @Expose
    public double totalShipFee;

    @SerializedName("totalServiceFee")
    @Expose
    public double totalServiceFee;

    @SerializedName("orderServiceDetails")
    @Expose
    public List<OrderServiceDetailForm> orderServiceDetails;

    @SerializedName("shippingAddress")
    @Expose
    public String shippingAddress;

    @SerializedName("shippingPersonName")
    @Expose
    public String shippingPersonName;

    @SerializedName("shippingPersonPhoneNumber")
    @Expose
    public String shippingPersonPhoneNumber;

    @SerializedName("longShipping")
    @Expose
    public double longShipping;

    @SerializedName("latShipping")
    @Expose
    public double latShipping;

    @SerializedName("isCashPay")
    @Expose
    public boolean isCashPay;
}
