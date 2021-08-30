package com.laundry.app.dto.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NoteResponse {

    @SerializedName("id")
    @Expose
    String id = "";

    @SerializedName("title")
    @Expose
    String title = "";

    @SerializedName("isDone")
    @Expose
    String isDone = "";

}
