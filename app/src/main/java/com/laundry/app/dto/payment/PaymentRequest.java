package com.laundry.app.dto.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaymentRequest implements Serializable {
    @SerializedName("orderId")
    @Expose
    public int orderId;

    @SerializedName("partnerCode")
    @Expose
    public String partnerCode;

    @SerializedName("requestId")
    @Expose
    public String requestId;

}
