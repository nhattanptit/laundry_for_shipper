package com.laundry.app.dto.ordercreate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderServiceDetailForm {

    @SerializedName("quantity")
    @Expose
    private int quantity;

    @SerializedName("serviceDetailId")
    @Expose
    private int serviceDetailId;

    public OrderServiceDetailForm(int quantity, int serviceDetailId) {
        this.quantity = quantity;
        this.serviceDetailId = serviceDetailId;
    }
}
