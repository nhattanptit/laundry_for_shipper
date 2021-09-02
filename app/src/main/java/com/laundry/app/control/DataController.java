package com.laundry.app.control;

import androidx.annotation.IntRange;

import com.laundry.app.data.APIConstant;
import com.laundry.app.data.ApiService;
import com.laundry.app.dto.authentication.RegisterRequest;
import com.laundry.app.dto.authentication.RegisterResponse;
import com.laundry.app.dto.ordercreate.OrderDto;
import com.laundry.app.dto.ordercreate.OrderResponse;
import com.laundry.app.dto.ordercreate.OrderServiceDetailForm;
import com.laundry.app.dto.serviceall.ServiceAllResponse;
import com.laundry.app.dto.sevicedetail.ServicesDetailResponse;

import java.util.List;

import retrofit2.Call;

public class DataController {

    private final ApiService service = APIConstant.getService();

    public void register(String username,
                         String password,
                         String name,
                         String email,
                         String phoneNumber,
                         String address,
                         ApiServiceOperator.OnResponseListener<RegisterResponse> listener) {

        RegisterRequest request = new RegisterRequest();
        request.setUsername(username);
        request.setPassword(password);
        request.setName(name);
        request.setEmail(email);
        request.setPhoneNumber(phoneNumber);
        request.setAddress(address);

        Call<RegisterResponse> call = service.signup(request);
        call.enqueue(new ApiServiceOperator<>(listener));

    }

    public void getServicesAll(ApiServiceOperator.OnResponseListener<ServiceAllResponse> listener) {
        Call<ServiceAllResponse> call = service.getServicesAll();
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    public void getServicesDetail(@IntRange(from = 16, to = 19) int id, ApiServiceOperator.OnResponseListener<ServicesDetailResponse> listener) {
        Call<ServicesDetailResponse> call = service.getServicesDetail(id);
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    public void createOrder(String distance,
                            List<OrderServiceDetailForm> orderServiceDetailForms,
                            int serviceID,
                            String address,
                            ApiServiceOperator.OnResponseListener<OrderResponse> listener) {
        OrderDto request = new OrderDto();
        request.setDistance(distance);
        request.setOrderServiceDetailForms(orderServiceDetailForms);
        request.setServiceId(serviceID);
        request.setShippingAddress(address);
        Call<OrderResponse> call = service.createOrder(request);
        call.enqueue(new ApiServiceOperator<>(listener));
    }
}
