package com.example.codevision2.api.model;

public class OCRResponseModel {
    private String text;
    private Boolean status;
    private String errorMessage;

    public Boolean getStatus(){ return status; }
    public String getText(){ return text; }
    public String getErrorMessage(){ return errorMessage; }
}
