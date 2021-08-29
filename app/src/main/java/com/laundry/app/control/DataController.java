package com.laundry.app.control;

import com.laundry.app.data.APIConstant;
import com.laundry.app.data.ApiService;

public class DataController {

    private ApiService service = new APIConstant().getService();

    public void getData() {
        service.getData();

    }

}
