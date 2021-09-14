package com.laundry.app.control;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.IntRange;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.laundry.app.constant.Constant;
import com.laundry.app.data.APIConstant;
import com.laundry.app.data.ApiService;
import com.laundry.app.dto.BaseResponse;
import com.laundry.app.dto.Role;
import com.laundry.app.dto.UserInfo;
import com.laundry.app.dto.addressall.AddressListResponse;
import com.laundry.app.dto.addressdelete.AddressDeleteResponse;
import com.laundry.app.dto.addressnew.AddressAddRequest;
import com.laundry.app.dto.addressnew.AddressAddResponse;
import com.laundry.app.dto.addressupdate.AddressUpdateRequest;
import com.laundry.app.dto.addressupdate.AddressUpdateResponse;
import com.laundry.app.dto.authentication.LoginRequest;
import com.laundry.app.dto.authentication.LoginResponseDto;
import com.laundry.app.dto.authentication.RegisterRequest;
import com.laundry.app.dto.authentication.RegisterResponse;
import com.laundry.app.dto.authentication.SocialLoginRequest;
import com.laundry.app.dto.authentication.SocialLoginRequestLite;
import com.laundry.app.dto.maps.MapDirectionResponse;
import com.laundry.app.dto.order.OrderConfirmResponseDto;
import com.laundry.app.dto.ordercreate.OrderRequest;
import com.laundry.app.dto.ordercreate.OrderResponseDto;
import com.laundry.app.dto.ordercreate.OrderServiceDetailForm;
import com.laundry.app.dto.orderincompletelist.OrderListIncompleteCustomerResponse;
import com.laundry.app.dto.orderlistcustomer.OrderListCustomerResponse;
import com.laundry.app.dto.orderlistshipper.OrderListShipperResponse;
import com.laundry.app.dto.payment.PaymentRequest;
import com.laundry.app.dto.payment.PaymentResponseDto;
import com.laundry.app.dto.servicelist.ServiceListResponse;
import com.laundry.app.dto.sevicedetail.ServicesDetailResponse;
import com.laundry.app.dto.shippingfee.ShippingFeeResponseDto;
import com.laundry.app.dto.user.PersonalInfoResponse;
import com.laundry.app.utils.SharePreferenceManager;

import java.util.List;

import retrofit2.Call;

public class DataController {

    private final ApiService service = APIConstant.getService(APIConstant.BASE_URL);
    private final ApiService serviceMap = APIConstant.getService(APIConstant.BASE_URL_MAP_BOX);

    public void register(RegisterRequest registerRequest,
                         ApiServiceOperator.OnResponseListener<RegisterResponse> listener) {

        Call<RegisterResponse> call = service.signup(registerRequest);
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    public void login(Context context, LoginRequest loginRequest,
                      ApiServiceOperator.OnResponseListener<LoginResponseDto> listener) {
        String mode = SharePreferenceManager.getMode(context);
        Call<LoginResponseDto> call = TextUtils.equals(Role.CUSTOMER.role(), mode) ? service.signin(loginRequest) : service.signinShipper(loginRequest);
        call.enqueue(new ApiServiceOperator<>(listener));

    }

    public void socialLoginFirst(Context context, SocialLoginRequest loginRequest,
                      ApiServiceOperator.OnResponseListener<LoginResponseDto> listener) {
        Call<LoginResponseDto> call = service.signinSocialFirstTime(loginRequest);
        call.enqueue(new ApiServiceOperator<>(listener));

    }

    public void socialLogin(SocialLoginRequestLite body,
                            ApiServiceOperator.OnResponseListener<LoginResponseDto> listener) {
        Call<LoginResponseDto> call = service.signinSocialSecordTime(body);
        call.enqueue(new ApiServiceOperator<>(listener));

    }

    public void getServicesAll(ApiServiceOperator.OnResponseListener<ServiceListResponse> listener) {
        Call<ServiceListResponse> call = service.getServicesAll();
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    public void getServicesDetail(@IntRange(from = 16, to = 19) int id, ApiServiceOperator.OnResponseListener<ServicesDetailResponse> listener) {
        Call<ServicesDetailResponse> call = service.getServicesDetail(id);
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    public void createOrder(Context context,
                            OrderRequest request,
                            ApiServiceOperator.OnResponseListener<OrderResponseDto> listener) {
        Call<OrderResponseDto> call = service.createOrder(UserInfo.getInstance().getToken(context), request);
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    /**
     * Get direction map
     *
     * @param listener
     */
    public void getDirectionMaps(String coordinate,
                                 String geometries,
                                 String accessToken,
                                 ApiServiceOperator.OnResponseListener<MapDirectionResponse> listener) {
        Call<MapDirectionResponse> call = serviceMap.getDirectionMap(coordinate, geometries, accessToken);
        call.enqueue(new ApiServiceOperator<MapDirectionResponse>(listener));
    }

    public void getAddress(Context context, ApiServiceOperator.OnResponseListener<AddressListResponse> listener) {
        Call<AddressListResponse> call = service.getAddress(UserInfo.getInstance().getToken(context));
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    public void oderConfirm(Context context,
                            List<OrderServiceDetailForm> orderServiceDetailForms,
                            ApiServiceOperator.OnResponseListener<OrderConfirmResponseDto> listener) {
        Call<OrderConfirmResponseDto> call = service.orderConfirm(UserInfo.getInstance().getToken(context), orderServiceDetailForms);
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    public void getShippingFee(Context context,
                               String distance,
                               ApiServiceOperator.OnResponseListener<ShippingFeeResponseDto> listener) {
        Call<ShippingFeeResponseDto> call = service.getShippingFee(UserInfo.getInstance().getToken(context), distance);
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    public void addAddress(Context context, AddressAddRequest addRequest,
                           ApiServiceOperator.OnResponseListener<AddressAddResponse> listener) {
        Call<AddressAddResponse> call = service.addAddress(UserInfo.getInstance().getToken(context), addRequest);
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    /**
     * Cancel order
     *
     * @param context  Context
     * @param orderId  Order Id
     * @param listener Call back listener
     */
    public void cancelOrder(Context context, String orderId, ApiServiceOperator.OnResponseListener<BaseResponse> listener) {
        Call<BaseResponse> call = service.cancelOrder(UserInfo.getInstance().getToken(context), orderId);
        call.enqueue(new ApiServiceOperator<>(listener));
    }

//    /**
//     * Cancel order
//     * @param context Context
//     * @param orderId Order Id
//     * @param listener Call back listener
//     */
//    public void cancelOrder(Context context, String orderId, ApiServiceOperator.OnResponseListener<BaseResponse> listener) {
//        Call<BaseResponse> call = service.cancelOrder(UserInfo.getInstance().getToken(context), orderId);
//        call.enqueue(new ApiServiceOperator<>(listener));
//    }

    public void deleteAddress(Context context, int id, ApiServiceOperator.OnResponseListener<AddressDeleteResponse> listener) {
        Call<AddressDeleteResponse> call = service.deleteAddress(UserInfo.getInstance().getToken(context), id);
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    public void updateAddress(Context context, int id, AddressUpdateRequest updateAddressRequest, ApiServiceOperator.OnResponseListener<AddressUpdateResponse> listener) {
        Call<AddressUpdateResponse> call = service.updateAddress(UserInfo.getInstance().getToken(context), id, updateAddressRequest);
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    /**
     * Get order detail
     *
     * @param id       orderId
     * @param listener callback listener
     */
    public void getOrderDetail(Context context, int id, ApiServiceOperator.OnResponseListener<OrderResponseDto> listener) {
        Call<OrderResponseDto> call = service.getOrderDetail(UserInfo.getInstance().getToken(context), id);
        call.enqueue(new ApiServiceOperator<>(listener));
    }


    /**
     * Get order detail Shipper
     *
     * @param id       orderId
     * @param listener callback listener
     */
    public void getOrderDetailShipper(Context context, int id, ApiServiceOperator.OnResponseListener<OrderResponseDto> listener) {
        Call<OrderResponseDto> call = service.getOrderDetailShipper(UserInfo.getInstance().getToken(context), id);
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    public void getOrderListCustomer(Context context, int page, int size, ApiServiceOperator.OnResponseListener<OrderListCustomerResponse> listener) {
        Call<OrderListCustomerResponse> call = service.getOrderListCustomer(UserInfo.getInstance().getToken(context), page, size);
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    public void getOrderListIncompleteCustomer(Context context, int page, int size, ApiServiceOperator.OnResponseListener<OrderListIncompleteCustomerResponse> listener) {
        Call<OrderListIncompleteCustomerResponse> call = service.getOrderIncompleteListCustomer(UserInfo.getInstance().getToken(context), page, size);
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    /**
     * Get order list shipper by status order
     *
     * @param context     Context
     * @param orderStatus Orderstatus
     * @param page        page start
     * @param size        Size
     * @param listener    Listener callback
     */
    public void getOrderListShipper(Context context, String orderStatus, int page, int size, ApiServiceOperator.OnResponseListener<OrderListShipperResponse> listener) {
        Call<OrderListShipperResponse> call = service.getOrderListShipper(UserInfo.getInstance().getToken(context), orderStatus, page, size);
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    /**
     * Get order list shipper new order
     *
     * @param context  Context
     * @param page     page start
     * @param size     Size
     * @param listener Listener callback
     */
    public void getOrderListNewShipper(Context context, int page, int size, ApiServiceOperator.OnResponseListener<OrderListShipperResponse> listener) {
        Call<OrderListShipperResponse> call = service.getOrderListNewShipper(UserInfo.getInstance().getToken(context), page, size);
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    /**
     * Accept order
     *
     * @param context  Context
     * @param orderId  Order id
     * @param listener listener callback
     */
    public void acceptOrder(Context context, String orderId, ApiServiceOperator.OnResponseListener<BaseResponse> listener) {
        Call<BaseResponse> call = service.acceptOrder(UserInfo.getInstance().getToken(context), orderId);
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    public void paymentFinished(Context context, PaymentRequest request, ApiServiceOperator.OnResponseListener<PaymentResponseDto> listener) {
        Call<PaymentResponseDto> call = service.paymentFinished(UserInfo.getInstance().getToken(context), request);
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    public void shipperCancelOrder(Context context, String orderId, ApiServiceOperator.OnResponseListener<BaseResponse> listener) {
        Call<BaseResponse> call = service.shipperCancelOrder(UserInfo.getInstance().getToken(context), orderId);
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    /**
     * update status
     *
     * @param context  Context
     * @param orderId  Order id
     * @param listener listener callback
     */
    public void updateStatusOrder(Context context, String statusUpdate, String orderId, ApiServiceOperator.OnResponseListener<BaseResponse> listener) {
        Call<BaseResponse> call = null;
        if (TextUtils.equals(Constant.NEW, statusUpdate)) {
            call = service.acceptOrder(UserInfo.getInstance().getToken(context), orderId);
        } else if (TextUtils.equals(Constant.SHIPPER_ACCEPTED_ORDER, statusUpdate)) {
            call = service.receiveOrder(UserInfo.getInstance().getToken(context), orderId);
        } else if (TextUtils.equals(Constant.STORE_DONE_ORDER, statusUpdate)) {
            call = service.deliverOrder(UserInfo.getInstance().getToken(context), orderId);
        } else if (TextUtils.equals(Constant.SHIPPER_DELIVER_ORDER, statusUpdate)) {
            call = service.completeOrder(UserInfo.getInstance().getToken(context), orderId);
        }
        if (call != null) {
            call.enqueue(new ApiServiceOperator<>(listener));
        }
    }

    /**
     * Get account infomation api
     */
    public void getAccountInfomation(Context context, ApiServiceOperator.OnResponseListener<PersonalInfoResponse> listener) {
        Call<PersonalInfoResponse> call = service.getAccountInfomation(UserInfo.getInstance().getToken(context));
        call.enqueue(new ApiServiceOperator<>(listener));
    }
}
