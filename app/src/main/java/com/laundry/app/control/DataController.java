package com.laundry.app.control;

import com.laundry.app.data.APIConstant;
import com.laundry.app.data.ApiService;
import com.laundry.app.dto.authentication.LoginRequest;
import com.laundry.app.dto.authentication.LoginResponseDto;
import com.laundry.app.dto.authentication.RegisterRequest;
import com.laundry.app.dto.authentication.RegisterResponse;

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


}
