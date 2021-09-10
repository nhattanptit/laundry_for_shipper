package com.laundry.app.dto.ordercreate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShipperDto {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("phoneNumber")
    @Expose
    public String phoneNumber;
    @SerializedName("vehicleType")
    @Expose
    public String vehicleType;
    @SerializedName("licensePlate")
    @Expose
    public String licensePlate;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("role")
    @Expose
    public String role;

}
