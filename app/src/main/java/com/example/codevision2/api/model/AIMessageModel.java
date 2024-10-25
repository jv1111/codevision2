package com.example.codevision2.api.model;

public class AIMessageModel {
    private String role;
    private String content;
    public AIMessageModel(String role, String content){
        this.role = role;
        this.content = content;
    }
}
