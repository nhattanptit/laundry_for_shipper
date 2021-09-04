package com.laundry.app.dto.authentication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterRequest {

    @SerializedName("username")
    @Expose
    public String username;

    @SerializedName("password")
    @Expose
    public String password;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("email")
    @Expose
    public String email;

    @SerializedName("phoneNumber")
    @Expose
    public String phoneNumber;

    @SerializedName("city")
    @Expose
    public String city;

    @SerializedName("district")
    @Expose
    public String district;

    @SerializedName("ward")
    @Expose
    public String ward;

    @SerializedName("address")
    @Expose
    public String address;

    public RegisterRequest() {
    }

}
