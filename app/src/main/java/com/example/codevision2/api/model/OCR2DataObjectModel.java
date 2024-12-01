package com.example.codevision2.api.model;

public class OCR2DataObjectModel {
    private String text;
    public OCR2DataObjectModel(String text){
        this.text = text;
    }
    public String getText(){
        return this.text;
    }
}
