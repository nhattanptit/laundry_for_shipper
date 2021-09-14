package com.laundry.app.dto.orderincompletelist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laundry.app.R;

import static com.laundry.app.constant.Constant.SHIPPER_ACCEPTED_ORDER;
import static com.laundry.app.constant.Constant.SHIPPER_DELIVER_ORDER;
import static com.laundry.app.constant.Constant.SHIPPER_RECEIVED_ORDER;
import static com.laundry.app.constant.Constant.STORE_DONE_ORDER;
import static com.laundry.app.constant.Constant.STORE_RECEIVED_ORDER;

public class OrderListIncompleteCustomerDto {

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("shippingPersonName")
    @Expose
    public String shippingPersonName;

    @SerializedName("shippingPersonPhoneNumber")
    @Expose
    public String shippingPersonPhoneNumber;

    @SerializedName("createdDate")
    @Expose
    public String createdDate;

    /**
     * get icon by status
     *
     * @return icon by status
     */
    public int getIconByStatus() {
        int icon = 0;
        switch (status) {
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
        }
        return icon;
    }

    public String getStatusContent() {
        String statusContent = "";
        switch (status) {
            case SHIPPER_ACCEPTED_ORDER:
                statusContent = "Shipper Accepted Order";
                break;
            case SHIPPER_RECEIVED_ORDER:
                statusContent = "Shipper Received Order";
                break;
            case STORE_RECEIVED_ORDER:
                statusContent = "Store Accepted Order";
                break;
            case STORE_DONE_ORDER:
                statusContent = "Store Done Order";
                break;
            case SHIPPER_DELIVER_ORDER:
                statusContent = "Shipper Deliver Order";
                break;
        }
        return statusContent;
    }
}
