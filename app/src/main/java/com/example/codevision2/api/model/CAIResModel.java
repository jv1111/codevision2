package com.example.codevision2.api.model;

import java.util.List;

public class CAIResModel {
    private List<CAIResChoiceModel> choices;
    private String message;

    public CAIResModel(List<CAIResChoiceModel> choices, String message){
        this.choices = choices;
        this.message = message;
    }

    public String getFirstMessage(){
        return choices.get(0).getMessage().getContent();
    }
    public String getMessage() {
        return message;
    }
}
