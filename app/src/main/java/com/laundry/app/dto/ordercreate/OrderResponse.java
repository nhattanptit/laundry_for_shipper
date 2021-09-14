package com.laundry.app.dto.ordercreate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laundry.app.R;
import com.laundry.app.dto.BaseResponse;
import com.laundry.app.dto.payment.PaymentInfo;
import com.laundry.app.dto.sevicedetail.ServiceDetailDto;

import java.util.List;

import static com.laundry.app.constant.Constant.CANCEL;
import static com.laundry.app.constant.Constant.COMPLETE_ORDER;
import static com.laundry.app.constant.Constant.NEW;
import static com.laundry.app.constant.Constant.SHIPPER_ACCEPTED_ORDER;
import static com.laundry.app.constant.Constant.SHIPPER_DELIVER_ORDER;
import static com.laundry.app.constant.Constant.SHIPPER_RECEIVED_ORDER;
import static com.laundry.app.constant.Constant.STORE_DONE_ORDER;
import static com.laundry.app.constant.Constant.STORE_RECEIVED_ORDER;

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

    @SerializedName("pickUpWard")
    @Expose
    public String pickUpWard;

    @SerializedName("pickUpDistrict")
    @Expose
    public String pickUpDistrict;

    @SerializedName("pickUpCity")
    @Expose
    public String pickUpCity;

    @SerializedName("isPaid")
    @Expose
    public boolean isPaid;

    @SerializedName("serviceDetails")
    @Expose
    public List<ServiceDetailDto> serviceDetails;

    @SerializedName("longShipping")
    @Expose
    public double longitude = 0;

    @SerializedName("latShipping")
    @Expose
    public double latitude = 0;

    @SerializedName("shipper")
    @Expose
    public ShipperDto shipperDto;

    @SerializedName("isCashPay")
    @Expose
    public boolean isCashPay;

    @SerializedName("paymentInfo")
    @Expose
    public PaymentInfo paymentInfo;

    @SerializedName("createdDate")
    @Expose
    public String createdDate;

    @SerializedName("lastUpdatedDate")
    @Expose
    public String lastUpdatedDate;



    /**
     * get icon by status
     * @return icon by status
     */
    public int getIconByStatus() {
        int icon = 0;
        switch (status) {
            case NEW:
                icon = R.drawable.new_order_icon;
                break;
            case SHIPPER_ACCEPTED_ORDER:
                icon = R.drawable.shipper_accepted_order_icon;
                break;
            case SHIPPER_RECEIVED_ORDER:
                icon = R.drawable.shipper_receiverd_order_icon;
                break;
            case STORE_RECEIVED_ORDER:
                icon = R.drawable.store_receivered_order_icon;
                break;
            case STORE_DONE_ORDER:
                icon = R.drawable.store_done_order_icon;
                break;
            case SHIPPER_DELIVER_ORDER:
                icon = R.drawable.shipper_delivering_order_icon;
                break;
            case COMPLETE_ORDER:
                icon = R.drawable.delivered_order_icon;
                break;
            case CANCEL:
                icon = R.drawable.order_cancelled_icon;
                break;
        }
        return icon;
    }


    /**
     * Get content status order
     *
     * @return Content status order
     */
    public String getStatusContent() {
        String statusContent = "";
        switch (status) {
            case NEW:
                statusContent = "New order - waiting a few minutes";
                break;
            case SHIPPER_ACCEPTED_ORDER:
                statusContent = "Shipper accepted order";
                break;
            case SHIPPER_RECEIVED_ORDER:
                statusContent = "Shipper received order";
                break;
            case STORE_RECEIVED_ORDER:
                statusContent = "Store accepted order";
                break;
            case STORE_DONE_ORDER:
                statusContent = "Store done order";
                break;
            case SHIPPER_DELIVER_ORDER:
                statusContent = "Shipper deliver order";
                break;
            case COMPLETE_ORDER:
                statusContent = "Delivered!";
                break;
            case CANCEL:
                statusContent = "Order has been cancelled!";
                break;
        }
        return statusContent;
    }
}
