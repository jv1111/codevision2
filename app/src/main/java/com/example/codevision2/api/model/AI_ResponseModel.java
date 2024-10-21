package com.example.codevision2.api.model;

public class AI_ResponseModel {
    private final String result;
    private final Boolean status;
    public AI_ResponseModel(String result, Boolean status){
        this.result = result;
        this.status = status;
    }
    public String getResult(){
        return  result;
    }
    public Boolean getStatus(){
        return status;
    }}
