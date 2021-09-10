package com.laundry.app.dto.orderlistshipper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laundry.app.dto.BaseResponse;

import java.util.List;

public class OrderListShipperResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    public List<OrderListShipperDto> orderListShipperDtos;
}
