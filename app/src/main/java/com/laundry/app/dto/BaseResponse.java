package com.laundry.app.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BaseResponse implements Serializable {
    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }
}
