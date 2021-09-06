package com.laundry.app.dto.addressaccount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laundry.app.dto.BaseResponse;

import java.util.List;

public class AddressRegisteredResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    public List<AddressRegisteredDto> addressRegisters;
}
