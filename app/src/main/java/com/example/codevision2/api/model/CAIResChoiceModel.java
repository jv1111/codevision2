package com.example.codevision2.api.model;

public class CAIResChoiceModel {
    private AIMessageModel message;

    public CAIResChoiceModel(AIMessageModel message){
        this.message = message;
    }

    public AIMessageModel getMessage(){ return message; }

}
