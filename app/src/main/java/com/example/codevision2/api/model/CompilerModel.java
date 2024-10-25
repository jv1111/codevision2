package com.example.codevision2.api.model;

public class CompilerModel {
    String code;
    String output;
    public CompilerModel(String code){
        this.code = code;
    }
    public String getOutput(){return output;}
    public String getCode(){return code;}
}
