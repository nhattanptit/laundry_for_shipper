package com.laundry.app.data;

public final class APIConstant {

    public static final String BASE_URL  ="https://laundry-online.herokuapp.com";
    public static final String URL_LOGIN  ="/user/auth/signin";
    public static final String URL_SIGNUP  ="/user/auth/signup";
    public static final String URL_SERVICES_ALL ="/services/all";
    public static final String URL_SERVICES_DETAILS ="/services/details";
    public static final String URL_ORDERS_CONFIRM ="/orders/services-bill";
    public static final String URL_ORDERS_CREATE ="/orders/create";

    public static ApiService getService() {
        return new RetrofitClient().getClient(BASE_URL).create(ApiService.class);
    }
}
