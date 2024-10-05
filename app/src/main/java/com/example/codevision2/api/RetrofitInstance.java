package com.example.codevision2.api;

import com.example.codevision2.Constant;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private final Retrofit retrofit;
    private final OkHttpClient httpClient;

    public RetrofitInstance(String baseUrl){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(Constant.MAX_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constant.MAX_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constant.MAX_TIMEOUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .client(httpClient)
                .build();
    }

    public Retrofit getRetrofit(){
        return retrofit;
    }

}
