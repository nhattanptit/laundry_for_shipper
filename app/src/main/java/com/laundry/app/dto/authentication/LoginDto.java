package com.laundry.app.dto.authentication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginDto {
    @SerializedName("token")
    @Expose
    public String token;

    @SerializedName("type")
    @Expose
    public String type;

    @SerializedName("username")
    @Expose
    public String username;

    @SerializedName("name")
    @Expose
    public String socialName;

    @SerializedName("email")
    @Expose
    public String email;
}
