package com.laundry.app.dto.ordercreate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrderRequest implements Serializable {

    @SerializedName("distance")
    @Expose
    public int distance;

    @SerializedName("serviceId")
    @Expose
    public int serviceId;

    @SerializedName("orderServiceDetailForms")
    @Expose
    public List<OrderServiceDetailForm> orderServiceDetailForms;

    @SerializedName("shippingAddress")
    @Expose
    public String shippingAddress;
}
