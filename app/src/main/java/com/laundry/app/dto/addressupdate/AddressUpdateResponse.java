package com.laundry.app.dto.addressupdate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laundry.app.dto.BaseResponse;

public class AddressUpdateResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private Object data;
}
