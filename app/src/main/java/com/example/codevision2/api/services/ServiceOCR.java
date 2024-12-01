package com.example.codevision2.api.services;

import com.example.codevision2.ENV;
import com.example.codevision2.api.model.OCR2Model;
import com.example.codevision2.api.model.OCRResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ServiceOCR {

    @Headers({
            "x-rapidapi-host: " + ENV.OCR_API_HOST,
            "x-rapidapi-key: " + ENV.RAPID_API_KEY
    })
    @GET("/ocr")
    Call<OCRResponseModel> getTextFromImage(@Query("url") String imageUrl);

    @Headers({
            "x-rapidapi-host: " + ENV.OCR2_API_HOST,
            "x-rapidapi-key: " + ENV.RAPID_API_KEY
    })
    @GET("/ocr")
    Call<OCR2Model> getTextFromImage2(@Query("url") String imageUrl, @Query("language") String language);

}
