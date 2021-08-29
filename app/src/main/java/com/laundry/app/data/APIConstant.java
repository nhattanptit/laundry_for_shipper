package com.laundry.app.data;

public final class APIConstant {

    private static final String BASE_URL  ="";

    public ApiService getService() {
        return new RetrofitClient().getClient(BASE_URL).create(ApiService.class);
    }
}
