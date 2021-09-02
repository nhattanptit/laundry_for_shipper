package com.laundry.app.dto.authentication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("loginId")
    @Expose
    public String loginId;

    @SerializedName("password")
    @Expose
    public String password;
}
