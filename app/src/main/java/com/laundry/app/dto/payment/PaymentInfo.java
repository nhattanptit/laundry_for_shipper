package com.laundry.app.dto.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentInfo {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("partnerCode")
    @Expose
    public String partnerCode;
    @SerializedName("requestId")
    @Expose
    public String requestId;
}
