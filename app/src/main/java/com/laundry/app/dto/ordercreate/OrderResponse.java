package com.laundry.app.dto.ordercreate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laundry.app.dto.BaseResponse;
import com.laundry.app.dto.sevicedetail.ServiceDetailDto;

import java.util.List;

public class OrderResponse extends BaseResponse {

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("serviceId")
    @Expose
    public int serviceId;

    @SerializedName("serviceName")
    @Expose
    public String serviceName;

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("distance")
    @Expose
    public double distance;

    @SerializedName("totalShipFee")
    @Expose
    public double totalShipFee;

    @SerializedName("totalServiceFee")
    @Expose
    public double totalServiceFee;

    @SerializedName("totalBill")
    @Expose
    public double totalBill;

    @SerializedName("shippingAddress")
    @Expose
    public String shippingAddress;

    @SerializedName("shippingPersonName")
    @Expose
    public String shippingPersonName;

    @SerializedName("shippingPersonPhoneNumber")
    @Expose
    public String shippingPersonPhoneNumber;

    @SerializedName("pickUpPersonName")
    @Expose
    public String pickUpPersonName;

    @SerializedName("pickUpPersonPhoneNumber")
    @Expose
    public String pickUpPersonPhoneNumber;

    @SerializedName("pickUpAddress")
    @Expose
    public String pickUpAddress;

    @SerializedName("isPaid")
    @Expose
    public boolean isPaid;

    @SerializedName("serviceDetails")
    @Expose
    public List<ServiceDetailDto> serviceDetails;

    public double longitude = 0;
    public double latitude = 0;

}
