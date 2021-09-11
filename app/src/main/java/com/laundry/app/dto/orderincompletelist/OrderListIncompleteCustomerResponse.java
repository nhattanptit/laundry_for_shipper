package com.laundry.app.dto.orderincompletelist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laundry.app.dto.BaseResponse;

import java.util.List;

public class OrderListIncompleteCustomerResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    public List<OrderListIncompleteCustomerDto> data;
}
