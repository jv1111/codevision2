package com.example.codevision2.api.model;

public class OCR2Model {
    private OCR2DataObjectModel data;

    public OCR2Model(OCR2DataObjectModel data){
        this.data = data;
    }

    public OCR2DataObjectModel getData(){
        return data;
    }
}
