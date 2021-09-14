package com.laundry.app.dto.orderlistshipper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laundry.app.R;

import static com.laundry.app.constant.Constant.CANCEL;
import static com.laundry.app.constant.Constant.COMPLETE_ORDER;
import static com.laundry.app.constant.Constant.NEW;
import static com.laundry.app.constant.Constant.SHIPPER_ACCEPTED_ORDER;
import static com.laundry.app.constant.Constant.SHIPPER_DELIVER_ORDER;
import static com.laundry.app.constant.Constant.SHIPPER_RECEIVED_ORDER;
import static com.laundry.app.constant.Constant.STORE_DONE_ORDER;
import static com.laundry.app.constant.Constant.STORE_RECEIVED_ORDER;

public class OrderListShipperDto {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("serviceId")
    @Expose
    public Integer serviceId;
    @SerializedName("serviceName")
    @Expose
    public String serviceName;
    @SerializedName("distance")
    @Expose
    public Double distance;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("totalShipFee")
    @Expose
    public Integer totalShipFee;
    @SerializedName("totalServiceFee")
    @Expose
    public Integer totalServiceFee;
    @SerializedName("totalBill")
    @Expose
    public Double totalBill;
    @SerializedName("deliverAddress")
    @Expose
    public String deliverAddress;
    @SerializedName("pickUpAddress")
    @Expose
    public String pickUpAddress;
    @SerializedName("isPaid")
    @Expose
    public Boolean isPaid;
    @SerializedName("userId")
    @Expose
    public Integer userId;
    @SerializedName("shipperUserId")
    @Expose
    public Integer shipperUserId;
    @SerializedName("createdDate")
    @Expose
    public String createdDate;
    @SerializedName("lastUpdatedDate")
    @Expose
    public String lastUpdatedDate;
    @SerializedName("pickUpDateTime")
    @Expose
    public String pickUpDateTime;
    @SerializedName("deliveryDateTime")
    @Expose
    public String deliveryDateTime;
    @SerializedName("userName")
    @Expose
    public String shippingNamePerson;
    @SerializedName("userPhoneNumber")
    @Expose
    public String shippingPhoneNumber;
    @SerializedName("shippingAddress")
    @Expose
    public String shippingAddress;
    @SerializedName("pickUpWard")
    @Expose
    public String pickUpWard;
    @SerializedName("pickUpDistrict")
    @Expose
    public String pickUpDistrict;
    @Expose
    public String pickUpCity;
    @SerializedName("pickUpCity")

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
                statusContent = "Shipper accepted order";
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
