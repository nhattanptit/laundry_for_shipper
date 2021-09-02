package com.laundry.app.control;

import com.laundry.app.data.APIConstant;
import com.laundry.app.data.ApiService;
import com.laundry.app.dto.serviceall.ServiceAllBody;
import com.laundry.app.dto.authentication.RegisterRequest;
import com.laundry.app.dto.authentication.RegisterResponse;

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

    public void getServicesAll(ApiServiceOperator.OnResponseListener<ServiceAllBody> listener) {
        Call<ServiceAllBody> call = service.getServicesAll();
        call.enqueue(new ApiServiceOperator<>(listener));
    }
}
