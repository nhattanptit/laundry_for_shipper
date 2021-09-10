package com.laundry.app.dto.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laundry.app.dto.BaseResponse;

public class PaymentResponseDto extends BaseResponse {

    @SerializedName("data")
    @Expose
    public Object data;
}
