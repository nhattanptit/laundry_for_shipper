package com.laundry.app.data;

public final class APIConstant {

    public static final String STATUS_CODE_SUCCESS = "200";
    public static final String STATUS_CODE_EMAIL_NOT_EXIST = "L10";
    public static final String STATUS_CODE_EMAIL_EXIST = "L11";

    public static final String BASE_URL = "http://192.168.1.61:8080";
    public static final String URL_LOGIN = "/user/auth/signin";
    public static final String URL_SOCIAL_LOGIN_SECOND_TIME = "/user/auth/social-signin";
    public static final String URL_SOCIAL_LOGIN_FIRST_TIME = "/user/auth/social-first-signin";
    public static final String URL_SIGNUP = "/user/auth/signup";
    public static final String URL_SHIPPER_LOGIN = "/shipper/auth/signin";
    public static final String URL_USER_INFO = "/user/user-info";
    public static final String URL_SERVICES_ALL = "/services/all";
    public static final String URL_SERVICES_DETAILS = "/services/details";
    public static final String URL_ORDERS_CONFIRM = "/orders/services-bill";
    public static final String URL_ORDERS_CREATE = "/orders/create";
    public static final String URL_ORDER_SHIPPING_FEE = "/orders/shipping-fee";
    public static final String URL_ORDERS_CANCEL = "/orders/cancel";
    public static final String URL_ORDERS_DETAILS = "/orders/detail";
    public static final String URL_ORDERS_DETAILS_SHIPPER = "/shipper/orders/detail";
    public static final String URL_ORDERS_PAYMENT = "/orders/payment-done";
    public static final String URL_ADDRESS = "/addresses";
    public static final String URL_ADDRESS_ALL = "/addresses/all";
    public static final String URL_ADDRESS_NEW = "/addresses/new";
    public static final String URL_ADDRESS_UPDATE = "/addresses/update";
    public static final String URL_ADDRESS_DELETE = "/addresses/delete/";
    public static final String URL_ORDER_LIST_CUSTOMER = "/orders/list/";
    public static final String URL_ORDER_INCOMPLETE_LIST_CUSTOMER = "/orders/incomplete-list/";
    public static final String URL_ORDER_LIST_SHIPPER = "/shipper/orders/list/";
    public static final String URL_ORDER_LIST_NEW_SHIPPER = "/shipper/orders/available-order";
    public static final String URL_ORDER_ACCEPT_ORDER = "/shipper/orders/accept-order";
    public static final String URL_ORDER_RECEIVE_ORDER = "/shipper/orders/receive-order";
    public static final String URL_ORDER_DELIVER_ORDER = "/shipper/orders/deliver-order";
    public static final String URL_ORDER_COMPLETE_ORDER = "/shipper/orders/complete-order";
    public static final String URL_ORDER_SHIPPER_CANCEL = "/shipper/orders/cancel-order";


    public static final String BASE_URL_MAP_BOX = "https://api.mapbox.com";
    public static final String URL_DIRECTION_API = "/directions/v5/mapbox/cycling/{coordinate}";
    public static final String MAPBOX_ACCESS_TOKEN = "pk.eyJ1Ijoid29ybXBob3RvIiwiYSI6ImNrdDZzeDl1aTBsczkybnF3ZHA2MnV5anMifQ.wRJ7E1r6BVtV5rG8Bj-gJw";

    public static ApiService getService(String url) {
        return new RetrofitClient().getClient(url).create(ApiService.class);
    }
}
