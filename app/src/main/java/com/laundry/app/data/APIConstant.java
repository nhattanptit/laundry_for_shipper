package com.laundry.app.data;

public final class APIConstant {

    public static final String STATUS_CODE_SUCCESS = "200";

    public static final String BASE_URL = "https://laundry-online.herokuapp.com";
    public static final String URL_LOGIN = "/user/auth/signin";
    public static final String URL_SIGNUP = "/user/auth/signup";
    public static final String URL_SERVICES_ALL = "/services/all";
    public static final String URL_SERVICES_DETAILS = "/services/details";
    public static final String URL_ORDERS_CONFIRM ="/orders/services-bill";
    public static final String URL_ORDERS_CREATE = "/orders/create";
    public static final String URL_ORDER_SHIPPING_FEE = "/orders/shipping-fee";
    public static final String URL_ORDERS_CANCEL = "/orders/cancel";
    public static final String URL_ADDRESS = "/addresses";
    public static final String URL_ADDRESS_ALL = "/addresses/all";
    public static final String URL_ADDRESS_NEW = "/addresses/new";
    public static final String URL_ADDRESS_UPDATE = "/addresses/update";
    public static final String URL_ADDRESS_DELETE = "/addresses/delete";

    public static final String BASE_URL_MAP_BOX = "https://api.mapbox.com";
    public static final String URL_DIRECTION_API = "/directions/v5/mapbox/cycling/{coordinate}";
    public static final String MAPBOX_ACCESS_TOKEN = "pk.eyJ1Ijoid29ybXBob3RvIiwiYSI6ImNrdDZzeDl1aTBsczkybnF3ZHA2MnV5anMifQ.wRJ7E1r6BVtV5rG8Bj-gJw";

    public static ApiService getService(String url) {
        return new RetrofitClient().getClient(url).create(ApiService.class);
    }
}
