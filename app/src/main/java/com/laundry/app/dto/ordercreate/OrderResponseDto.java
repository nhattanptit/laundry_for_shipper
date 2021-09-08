package com.laundry.app.dto.ordercreate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laundry.app.dto.BaseResponse;
import com.laundry.app.dto.sevicedetail.ServiceDetailDto;

import java.util.List;

public class OrderResponseDto extends BaseResponse {

    @SerializedName("data")
    @Expose
    public OrderResponse data;
}
