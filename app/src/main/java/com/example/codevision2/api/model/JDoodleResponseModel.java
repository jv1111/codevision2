package com.example.codevision2.api.model;

public class JDoodleResponseModel {
    private String output;

    public JDoodleResponseModel(String output, String error){
        this.output = output;
    }

    public String getOutput(){
        return output;
    }

}
