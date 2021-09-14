package com.laundry.app.dto.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PersonalInfoDto {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("isSocialUser")
    @Expose
    public Boolean isSocialUser;
    @SerializedName("phoneNumber")
    @Expose
    public String phoneNumber;
    @SerializedName("email")
    @Expose
    public String email;
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
}
