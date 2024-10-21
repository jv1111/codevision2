package com.example.codevision2.api.model;

public class AI_RequestMessageModel {
    private final String role;
    private final String content;

    public AI_RequestMessageModel(String role, String content){
        this.role = role;
        this.content = content;
    }
}
