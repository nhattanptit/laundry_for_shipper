package com.laundry.app.dto.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laundry.app.dto.sevicedetail.ServiceDetailDto;

import java.io.Serializable;
import java.util.List;

public class OrderConfirmDto implements Serializable {

    @SerializedName("totalServicesFee")
    @Expose
    public double totalServicesFee;

    @SerializedName("serviceDetails")
    @Expose
    public List<ServiceDetailDto> products;

    public int serviceParentId;
}
