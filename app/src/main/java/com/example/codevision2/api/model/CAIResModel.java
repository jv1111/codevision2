package com.example.codevision2.api.model;

import java.util.List;

public class CAIResModel {
    private List<CAIResChoiceModel> choices;

    public CAIResModel(List<CAIResChoiceModel> choices){
        this.choices = choices;
    }

    public String getFirstMessage(){
        return choices.get(0).getMessage();
    }

}
