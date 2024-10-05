package com.example.codevision2.api.services;

import com.example.codevision2.api.model.OCRResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ServiceOCR {

    @Headers({
            "x-rapidapi-host: ocr-extract-text.p.rapidapi.com",
            "x-rapidapi-key: 4c20b4aa43msh7817566f25101dbp133e79jsnbfdf89c5fbed"
    })
    @GET("/ocr")
    Call<OCRResponseModel> getTextFromImage(@Query("url") String imageUrl);

}
