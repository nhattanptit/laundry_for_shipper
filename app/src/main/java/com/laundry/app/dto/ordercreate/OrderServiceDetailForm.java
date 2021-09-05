package com.laundry.app.dto.ordercreate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderServiceDetailForm {

    @SerializedName("serviceDetailId")
    @Expose
    public int serviceDetailId;

    @SerializedName("quantity")
    @Expose
    public int quantity;

    public OrderServiceDetailForm(int serviceDetailId, int quantity) {
        this.serviceDetailId = serviceDetailId;
        this.quantity = quantity;
    }
}
