package com.example.codevision2.api.services;

import com.example.codevision2.api.model.CompilerModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ServiceCompiler {
    @Headers("Content-Type: application/json")
    @POST("/compileAndRun")
    Call<CompilerModel> compileAndRun(@Body CompilerModel data);
}
