package com.example.codevision2.api.model;

import java.util.List;

public class AI_RequestModel {
    private final List<AI_RequestMessageModel> messages;
    private final boolean web_access;

    public AI_RequestModel(List<AI_RequestMessageModel> messages, Boolean web_access){
        this.messages = messages;
        this.web_access = web_access;
    }

}
