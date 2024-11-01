package com.example.codevision2.api.services;

import com.example.codevision2.ENV;
import com.example.codevision2.api.model.AIModel;
import com.example.codevision2.api.model.CAIReqModel;
import com.example.codevision2.api.model.CAIResModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ServiceAI {

    @Headers({
            "Content-Type: application/json",
            "x-rapidapi-host: " + ENV.CAI_API_HOST,
            "x-rapidapi-key: " + ENV.RAPID_API_KEY
    })
    @POST("/v1/chat/completions")
    Call<CAIResModel> cAISend(@Body CAIReqModel data);

}
