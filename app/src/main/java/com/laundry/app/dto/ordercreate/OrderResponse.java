package com.laundry.app.dto.ordercreate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laundry.app.dto.BaseResponse;

public class OrderResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private OrderDto data;

}
