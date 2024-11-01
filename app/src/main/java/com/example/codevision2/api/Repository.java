package com.example.codevision2.api;


import android.util.Log;

import com.example.codevision2.Constant;
import com.example.codevision2.ENV;
import com.example.codevision2.api.model.AIMessageModel;
import com.example.codevision2.api.model.AIModel;
import com.example.codevision2.api.model.CAIReqModel;
import com.example.codevision2.api.model.CAIResModel;
import com.example.codevision2.api.model.CompilerModel;
import com.example.codevision2.api.model.OCRResponseModel;
import com.example.codevision2.api.services.ServiceAI;
import com.example.codevision2.api.services.ServiceCompiler;
import com.example.codevision2.api.services.ServiceOCR;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {

    public interface RepoCallback<T>{
        void onSuccess(T data);
        void onFailed(String errorMessage);
    }

    private final RetrofitInstance retrofitInstanceOCR = new RetrofitInstance(ENV.OCR_API_URL);
    private final RetrofitInstance retrofitInstanceCAI = new RetrofitInstance(ENV.CAI_API_HOST);

    private final ServiceOCR ocrService = retrofitInstanceOCR.getRetrofit().create(ServiceOCR.class);
    private final ServiceAI aiService = retrofitInstanceCAI.getRetrofit().create(ServiceAI.class);

    private final RetrofitInstance retrofitInstanceCompiler = new RetrofitInstance(ENV.COMPILER_API_URL);
    private final ServiceCompiler compilerService = retrofitInstanceCompiler.getRetrofit().create(ServiceCompiler.class);

    public void analyzeCode(String code, String output, int mode, RepoCallback<String> cb){
        String script;

        if(mode == Constant.AI_ANALYZE){
            script = ENV.AI_START_ANALYZING + code;
        }else{
            script = ENV.AI_EXPLAIN_CODE + code + ENV.AI_EXPLAIN_OUTPUT + output;
        }

        Log.i("myTag script", script);

        /*
        List<AIMessageModel> messages = new ArrayList<>();
        messages.add(new AIMessageModel(ENV.AI_USER, script));
        CAIReqModel reqModel = new CAIReqModel(messages);

        Call<CAIResModel> call = aiService.cAISend(reqModel);
        call.enqueue(new Callback<CAIResModel>() {
            @Override
            public void onResponse(Call<CAIResModel> call, Response<CAIResModel> response) {
                cb.onSuccess(response.body().getFirstMessage());
            }

            @Override
            public void onFailure(Call<CAIResModel> call, Throwable t) {
                cb.onFailed(t.getMessage());
            }
        });
         */
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
                        if(!responseModel.getStatus()){
                            cb.onFailed(responseModel.getErrorMessage());
                        }else{
                            cb.onSuccess(response.body().getText());
                        }
                    }else{
                        cb.onFailed("Something went wrong.");
                    }
                }
            }

            @Override
            public void onFailure(Call<OCRResponseModel> call, Throwable t) {
                cb.onFailed("Something went wrong.");
            }
        });
    }

    public void runAndCompile(String code, RepoCallback<String> cb){
        CompilerModel data = new CompilerModel(code);
        Call<CompilerModel> call = compilerService.compileAndRun(data);
        call.enqueue(new Callback<CompilerModel>() {
            @Override
            public void onResponse(Call<CompilerModel> call, Response<CompilerModel> response) {
                cb.onSuccess(code);
            }

            @Override
            public void onFailure(Call<CompilerModel> call, Throwable t) {
                cb.onFailed(t.getMessage());
            }
        });
    }

}
