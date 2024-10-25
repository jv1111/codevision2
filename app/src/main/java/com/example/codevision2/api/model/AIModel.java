package com.example.codevision2.api.model;

import java.util.List;

public class AIModel {
    private List<AIMessageModel> messages;
    private boolean web_access;

    private String result;

    public AIModel(List<AIMessageModel> messages, boolean web_access){
        this.messages = messages;
        this.web_access = web_access;
    }

    public String getResult(){return result;}

}
