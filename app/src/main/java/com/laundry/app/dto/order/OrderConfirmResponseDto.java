package com.laundry.app.dto.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laundry.app.dto.BaseResponse;

import java.io.Serializable;

public class OrderConfirmResponseDto extends BaseResponse implements Serializable {
    @SerializedName("data")
    @Expose
    public OrderConfirmDto data;
}
