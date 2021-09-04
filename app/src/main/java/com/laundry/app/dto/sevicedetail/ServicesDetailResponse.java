package com.laundry.app.dto.sevicedetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laundry.app.dto.BaseResponse;

import java.util.List;

public class ServicesDetailResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    public List<ServiceDetailDto> servicesDetails;
}
