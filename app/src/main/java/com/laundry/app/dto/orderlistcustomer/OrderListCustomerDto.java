package com.laundry.app.dto.orderlistcustomer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laundry.app.dto.servicelist.INamedStatus;

public class OrderListCustomerDto {

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

    public OrderListCustomerType getOrderListIcon() {
        return OrderListCustomerType.getType(status);
    }

    public enum OrderListCustomerType implements INamedStatus {
        NEW {
            @Override
            public String getStatusName() {
                return "NEW";
            }
        },
        SHIPPER_ACCEPTED_ORDER {
            @Override
            public String getStatusName() {
                return "SHIPPER_ACCEPTED_ORDER";
            }
        },
        SHIPPER_RECEIVED_ORDER {
            @Override
            public String getStatusName() {
                return "SHIPPER_RECEIVED_ORDER";
            }
        },
        STORE_RECEIVED_ORDER {
            @Override
            public String getStatusName() {
                return "STORE_RECEIVED_ORDER";
            }
        },
        STORE_DONE_ORDER {
            @Override
            public String getStatusName() {
                return "STORE_DONE_ORDER";
            }
        },
        SHIPPER_DELIVER_ORDER {
            @Override
            public String getStatusName() {
                return "SHIPPER_DELIVER_ORDER";
            }
        },
        COMPLETE_ORDER {
            @Override
            public String getStatusName() {
                return "COMPLETE_ORDER";
            }
        },
        CANCEL {
            @Override
            public String getStatusName() {
                return "CANCEL";
            }
        };

        static OrderListCustomerType getType(String type) {
            switch (type) {
                case "NEW":
                    return NEW;
                case "SHIPPER_ACCEPTED_ORDER":
                    return SHIPPER_ACCEPTED_ORDER;
                case "SHIPPER_RECEIVED_ORDER":
                    return SHIPPER_RECEIVED_ORDER;
                case "STORE_RECEIVED_ORDER":
                    return STORE_RECEIVED_ORDER;
                case "STORE_DONE_ORDER":
                    return STORE_DONE_ORDER;
                case "SHIPPER_DELIVER_ORDER":
                    return SHIPPER_DELIVER_ORDER;
                case "COMPLETE_ORDER":
                    return COMPLETE_ORDER;
                case "CANCEL":
                    return CANCEL;
                default:
                    return null;
            }
        }
    }
}
