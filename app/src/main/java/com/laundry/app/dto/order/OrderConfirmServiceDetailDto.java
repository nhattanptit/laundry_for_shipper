package com.laundry.app.dto.order;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laundry.app.dto.servicelist.INamedStatus;

import java.io.Serializable;

public class OrderConfirmServiceDetailDto implements Serializable {

    @SerializedName("serviceDetailId")
    @Expose
    public int serviceDetailId;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("price")
    @Expose
    public double price;

    @SerializedName("serviceDetailIcon")
    @Expose
    public String serviceDetailIcon;

    @SerializedName("quantity")
    @Expose
    public int quantity = 0;

    public Double totalPrice = 0.0;

    public ServiceDetailType getServiceDetailType() {
        return ServiceDetailType.getType(serviceDetailIcon);
    }

    public enum ServiceDetailType implements INamedStatus {
        T_SHIRT {
            @Override
            public String getStatusName() {
                return "T_SHIRT";
            }
        },
        OUTER_WEAR {
            @Override
            public String getStatusName() {
                return "OUTER_WEAR";
            }
        },
        BOTTOM {
            @Override
            public String getStatusName() {
                return "BOTTOM";
            }
        },
        DRESSES {
            @Override
            public String getStatusName() {
                return "DRESSES";
            }
        },
        HOME {
            @Override
            public String getStatusName() {
                return "HOME";
            }
        },
        WOOLEN_BlANKET {
            @Override
            public String getStatusName() {
                return "WOOLEN_BlANKET";
            }
        },
        DUVET {
            @Override
            public String getStatusName() {
                return "DUVET";
            }
        },
        COMFORTER {
            @Override
            public String getStatusName() {
                return "COMFORTER";
            }
        },
        OTHER {
            @Override
            public String getStatusName() {
                return "OTHER";
            }
        };

        private static final String TAG = "ServiceDetailType";

        static ServiceDetailType getType(String type) {
            Log.d(TAG, "getType: " + type);
            switch (type) {
                case "T_SHIRT":
                    return T_SHIRT;
                case "OUTER_WEAR":
                    return OUTER_WEAR;
                case "BOTTOM":
                    return BOTTOM;
                case "DRESSES":
                    return DRESSES;
                case "HOME":
                    return HOME;
                case "OTHER":
                    return OTHER;
                case "WOOLEN_BlANKET":
                    return WOOLEN_BlANKET;
                case "DUVET":
                    return DUVET;
                case "COMFORTER":
                    return COMFORTER;
                default:
                    return null;
            }
        }
    }
}

