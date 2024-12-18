package com.example.codevision2.api.model;

import java.util.List;

public class CAIReqModel {
    private List<AIMessageModel> messages;
    private String model;
    private int max_tokens;
    private double temperature;

    public CAIReqModel(List<AIMessageModel> messages) {
        this.messages = messages;
        this.model = "gpt-4o";
        this.max_tokens = 1000;
        this.temperature = 0.9;
    }
}
