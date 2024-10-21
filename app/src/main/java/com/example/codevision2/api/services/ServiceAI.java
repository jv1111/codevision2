package com.example.codevision2.api.services;

import com.example.codevision2.ENV;
import com.example.codevision2.api.model.AI_RequestModel;
import com.example.codevision2.api.model.AI_ResponseModel;
import com.example.codevision2.api.model.JDoodleRequestModel;
import com.example.codevision2.api.model.JDoodleResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ServiceAI {
    @Headers(
            {
                    "Content-Type: application/json",
                    "x-rapidapi-host: " + ENV.API_API_HOST,
                    "x-rapidapi-key: " + ENV.AI_API_KEY
            }
    )
    @POST("/gpt4")
    Call<AI_ResponseModel> send(@Body AI_RequestModel data);
}
