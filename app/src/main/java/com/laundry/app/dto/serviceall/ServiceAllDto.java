package com.laundry.app.dto.serviceall;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ServiceAllDto implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("serviceIcon")
    @Expose
    private String serviceIcon;

    @SerializedName("description")
    @Expose
    private String description;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

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