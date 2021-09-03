package com.laundry.app.control;

import androidx.annotation.IntRange;

import com.laundry.app.data.APIConstant;
import com.laundry.app.data.ApiService;
import com.laundry.app.dto.authentication.LoginRequest;
import com.laundry.app.dto.authentication.LoginResponseDto;
import com.laundry.app.dto.authentication.RegisterRequest;
import com.laundry.app.dto.authentication.RegisterResponse;
import com.laundry.app.dto.ordercreate.OrderRequest;
import com.laundry.app.dto.ordercreate.OrderResponse;
import com.laundry.app.dto.ordercreate.OrderServiceDetailForm;
import com.laundry.app.dto.servicelist.ServiceListResponse;
import com.laundry.app.dto.sevicedetail.ServicesDetailResponse;

import java.util.List;

import retrofit2.Call;

public class DataController {

    private final ApiService service = APIConstant.getService();

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

    public void createOrder(int distance,
                            List<OrderServiceDetailForm> orderServiceDetailForms,
                            int serviceID,
                            String address,
                            ApiServiceOperator.OnResponseListener<OrderResponse> listener) {
        OrderRequest request = new OrderRequest();
        request.distance = distance;
        request.orderServiceDetailForms = orderServiceDetailForms;
        request.serviceId = serviceID;
        request.shippingAddress = address;
        Call<OrderResponse> call = service.createOrder(request);
        call.enqueue(new ApiServiceOperator<>(listener));
    }
}
