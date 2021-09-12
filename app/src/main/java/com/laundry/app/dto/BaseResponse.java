package com.laundry.app.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BaseResponse implements Serializable {
    @SerializedName("statusCd")
    @Expose
    public String statusCd;

    @SerializedName("message")
    @Expose
    public String message;
}
