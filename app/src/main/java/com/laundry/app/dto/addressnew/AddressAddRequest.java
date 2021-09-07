package com.laundry.app.dto.addressnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressAddRequest {

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
