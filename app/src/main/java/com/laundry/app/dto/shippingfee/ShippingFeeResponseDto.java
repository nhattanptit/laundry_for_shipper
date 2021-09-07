package com.laundry.app.dto.shippingfee;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laundry.app.dto.BaseResponse;
import com.laundry.app.dto.order.OrderConfirmDto;

import java.io.Serializable;

public class ShippingFeeResponseDto extends BaseResponse implements Serializable {
    @SerializedName("data")
    @Expose
    public double data;
}
