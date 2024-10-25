package com.example.codevision2.api.services;

import com.example.codevision2.ENV;
import com.example.codevision2.api.model.AIModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ServiceAI {

    @Headers({
            "Content-Type: application/json",
            "x-rapidapi-host: " + ENV.AI_API_HOST,
            "x-rapidapi-key: " + ENV.RAPID_API_KEY
    })
    @POST("/chatgpt")
    Call<AIModel> send(@Body AIModel data);

}
