package com.laundry.app.dto.orderlistcustomer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laundry.app.dto.BaseResponse;

import java.util.List;

public class OrderListCustomerResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    public List<OrderListCustomerDto> orderListCustomerDtoes;
}
