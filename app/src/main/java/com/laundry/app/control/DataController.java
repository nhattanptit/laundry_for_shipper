package com.laundry.app.control;

import com.laundry.app.data.APIConstant;
import com.laundry.app.data.ApiService;
import com.laundry.app.dto.authentication.RegisterRequest;
import com.laundry.app.dto.authentication.RegisterResponse;
import com.laundry.app.dto.test.NoteRequest;
import com.laundry.app.dto.test.NoteResponse;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataController {

    private ApiService service = APIConstant.getService();

    public void register(String username,
                         String password,
                         String name,
                         String email,
                         String phoneNumber,
                         String address,
                         ControllerListener<RegisterResponse> callback) {

        RegisterRequest request = new RegisterRequest();
        request.setUsername(username);
        request.setPassword(password);
        request.setPassword(name);
        request.setEmail(email);
        request.setPhoneNumber(phoneNumber);
        request.setAddress(address);

        service.saveInfoRegister(request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NotNull Call<RegisterResponse> call, @NotNull Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(@NotNull Call<RegisterResponse> call, @NotNull Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public void test(ControllerListener<NoteResponse> callback) {
        service.test(new NoteRequest()).enqueue(new Callback<NoteResponse>() {
            @Override
            public void onResponse(Call<NoteResponse> call, Response<NoteResponse> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<NoteResponse> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }
}
