package com.example.codevision2.api;

import android.util.Log;

import com.example.codevision2.Constant;
import com.example.codevision2.ENV;
import com.example.codevision2.api.model.JDoodleRequestModel;
import com.example.codevision2.api.model.JDoodleResponseModel;
import com.example.codevision2.api.model.OCRResponseModel;
import com.example.codevision2.api.services.Service;
import com.example.codevision2.api.services.ServiceOCR;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {

    public interface RepoCallback<T>{
        void onSuccess(T data);
        void onFailed(String errorMessage);
    }

    private RetrofitInstance retrofitInstance = new RetrofitInstance(Constant.BASE_URL);
    private RetrofitInstance retrofitInstanceOCR = new RetrofitInstance(ENV.OCR_API_URL);

    private Service apiService = retrofitInstance.getRetrofit().create(Service.class);
    private ServiceOCR ocrService = retrofitInstanceOCR.getRetrofit().create(ServiceOCR.class);

    public void submitCode(String code,RepoCallback<JDoodleResponseModel> cb){
        JDoodleRequestModel data = new JDoodleRequestModel(
                ENV.JDOODLE_CID,
                ENV.JDOODLE_SECRET,
                code,
                "java"
        );

        Call<JDoodleResponseModel> call = apiService.execute(data);
        call.enqueue(new Callback<JDoodleResponseModel>() {
            @Override
            public void onResponse(Call<JDoodleResponseModel> call, Response<JDoodleResponseModel> response) {
                cb.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<JDoodleResponseModel> call, Throwable t) {
                Log.e("myTag retro failed", t.getMessage());
                cb.onFailed("Something went wrong.");
            }
        });
    }

    public void getTextFromImage(String param, RepoCallback<String> cb){
        Call<OCRResponseModel> call = ocrService.getTextFromImage(param);
        call.enqueue(new Callback<OCRResponseModel>() {
            @Override
            public void onResponse(Call<OCRResponseModel> call, Response<OCRResponseModel> response) {
                OCRResponseModel responseModel = response.body();
                if(responseModel == null){
                    cb.onFailed("Null response");
                }else{
                    if(response.isSuccessful()){
                        Log.i("myTag", "ocr response code");
                        if(!responseModel.getStatus()){
                            cb.onFailed(responseModel.getErrorMessage());
                        }else{
                            Log.i("myTag", response.body().getText());
                            cb.onSuccess(response.body().getText());
                        }
                    }else{
                        Log.i("myTag", "error response");
                        cb.onFailed("Something went wrong.");
                    }
                }
            }

            @Override
            public void onFailure(Call<OCRResponseModel> call, Throwable t) {

            }
        });
    }
}
