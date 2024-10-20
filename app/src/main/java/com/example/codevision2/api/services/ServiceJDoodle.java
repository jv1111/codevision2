package com.example.codevision2.api.services;

import com.example.codevision2.api.model.JDoodleRequestModel;
import com.example.codevision2.api.model.JDoodleResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ServiceJDoodle {
    @Headers("Content-Type: application/json")
    @POST("/v1/execute")
    Call<JDoodleResponseModel> execute(@Body JDoodleRequestModel data);
}
