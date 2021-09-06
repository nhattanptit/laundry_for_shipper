package com.laundry.app.control;

import android.content.Context;

import androidx.annotation.IntRange;

import com.laundry.app.data.APIConstant;
import com.laundry.app.data.ApiService;
import com.laundry.app.dto.UserInfo;
import com.laundry.app.dto.addressaccount.AddressRegisteredResponse;
import com.laundry.app.dto.authentication.LoginRequest;
import com.laundry.app.dto.authentication.LoginResponseDto;
import com.laundry.app.dto.authentication.RegisterRequest;
import com.laundry.app.dto.authentication.RegisterResponse;
import com.laundry.app.dto.maps.MapDirectionResponse;
import com.laundry.app.dto.ordercreate.OrderRequest;
import com.laundry.app.dto.ordercreate.OrderResponse;
import com.laundry.app.dto.ordercreate.OrderServiceDetailForm;
import com.laundry.app.dto.servicelist.ServiceListResponse;
import com.laundry.app.dto.sevicedetail.ServicesDetailResponse;

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

    public void login(LoginRequest loginRequest,
                      ApiServiceOperator.OnResponseListener<LoginResponseDto> listener) {
        Call<LoginResponseDto> call = service.signin(loginRequest);
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
                            int distance,
                            int serviceID,
                            List<OrderServiceDetailForm> orderServiceDetailForms,
                            String address,
                            ApiServiceOperator.OnResponseListener<OrderResponse> listener) {
        OrderRequest request = new OrderRequest();
        request.distance = distance;
        request.orderServiceDetailForms = orderServiceDetailForms;
        request.serviceId = serviceID;
        request.shippingAddress = address;
        Call<OrderResponse> call = service.createOrder(UserInfo.getInstance().getToken(context), request);
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    /**
     * Get direction map
     * @param listener
     */
    public void getDirectionMaps(String coordinate,
                                 String geometries,
                                String accessToken,
            ApiServiceOperator.OnResponseListener<MapDirectionResponse> listener) {
        Call<MapDirectionResponse> call = serviceMap.getDirectionMap(coordinate, geometries, accessToken);
        call.enqueue(new ApiServiceOperator<MapDirectionResponse>(listener));
    }

    public void getAddress(Context context, ApiServiceOperator.OnResponseListener<AddressRegisteredResponse> listener) {
        Call<AddressRegisteredResponse> call = service.getAddress(UserInfo.getInstance().getToken(context));
        call.enqueue(new ApiServiceOperator<>(listener));
    }

}
