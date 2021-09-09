package com.laundry.app.dto.addressupdate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddressUpdateRequest implements Serializable {

    @Expose
    @SerializedName("receiverName")
    public String receiverName;

    @Expose
    @SerializedName("receiverPhoneNumber")
    public String receiverPhoneNumber;

    @Expose
    @SerializedName("city")
    public String city;

    @Expose
    @SerializedName("district")
    public String district;

    @Expose
    @SerializedName("ward")
    public String ward;

    @Expose
    @SerializedName("address")
    public String address;
}
