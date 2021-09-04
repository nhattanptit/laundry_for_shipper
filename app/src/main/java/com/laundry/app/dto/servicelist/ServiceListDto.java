package com.laundry.app.dto.servicelist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ServiceListDto implements Serializable {

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("serviceIcon")
    @Expose
    public String serviceIcon;

    @SerializedName("description")
    @Expose
    public String description;

    public ServiceAllType getServiceType() {
        return ServiceAllType.getType(serviceIcon);
    }

    public enum ServiceAllType implements INamedStatus {
        CLEAN {
            @Override
            public String getStatusName() {
                return "CLEAN";
            }
        },
        DRY_CLEAN {
            @Override
            public String getStatusName() {
                return "DRY_CLEAN";
            }
        },
        DRY_CLEAN_AND_IRON {
            @Override
            public String getStatusName() {
                return "DRY_CLEAN_AND_IRON";
            }
        },
        BLANKET {
            @Override
            public String getStatusName() {
                return "BLANKET";
            }
        };

        static ServiceAllType getType(String type) {
            switch (type) {
                case "CLEAN":
                    return CLEAN;
                case "DRY_CLEAN":
                    return DRY_CLEAN;
                case "DRY_CLEAN_AND_IRON":
                    return DRY_CLEAN_AND_IRON;
                case "BLANKET":
                    return BLANKET;
                default:
                    return null;
            }
        }
    }
}