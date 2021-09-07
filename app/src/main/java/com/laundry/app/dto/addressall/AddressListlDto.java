package com.laundry.app.dto.addressall;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddressListlDto implements Serializable {

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("isDefaultAddress")
    @Expose
    public boolean isDefaultAddress;

    @SerializedName("receiverName")
    @Expose
    public String receiverName;

    @SerializedName("receiverPhoneNumber")
    @Expose
    public String receiverPhoneNumber;

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

    @SerializedName("user")
    @Expose
    public User user;
}
