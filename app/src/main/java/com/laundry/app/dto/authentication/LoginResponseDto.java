package com.laundry.app.dto.authentication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laundry.app.dto.BaseResponse;

public class LoginResponseDto extends BaseResponse {
    @SerializedName("data")
    @Expose
    public LoginDto data;
}
