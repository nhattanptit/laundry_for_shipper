package com.laundry.app.dto.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laundry.app.dto.BaseResponse;

public class PersonalInfoResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    public PersonalInfoDto personalInfoDto;
}
