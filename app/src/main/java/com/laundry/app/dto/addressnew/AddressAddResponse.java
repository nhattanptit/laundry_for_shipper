package com.laundry.app.dto.addressnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laundry.app.dto.BaseResponse;

public class AddressAddResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    public Object data;
}
