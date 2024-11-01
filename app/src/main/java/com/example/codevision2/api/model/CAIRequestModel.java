package com.example.codevision2.api.model;

import java.util.List;

public class CAIRequestModel {
    private List<CAIMessageModel> messages;
    private String model;
    private int maxTokens;
    private double temperature;

    public CAIRequestModel(List<CAIMessageModel> messages) {
        this.messages = messages;
        this.model = "gpt-4o";
        this.maxTokens = 100;
        this.temperature = 0.9;
    }

}
