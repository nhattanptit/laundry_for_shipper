package com.laundry.app.dto.sevicedetail;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laundry.app.dto.servicelist.INamedStatus;

public class ServiceDetailDto {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("price")
    @Expose
    private Double price;

    @SerializedName("serviceDetailIcon")
    @Expose
    private String serviceDetailIcon;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

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

