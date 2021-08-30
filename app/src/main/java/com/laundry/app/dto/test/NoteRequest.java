package com.laundry.app.dto.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NoteRequest {

    @SerializedName("id")
    @Expose
    String id = "100";

    @SerializedName("title")
    @Expose
    String title = "test";

    @SerializedName("isDone")
    @Expose
    String isDone = "true";
}
