package com.example.codevision2.api.model;

public class JDoodleRequestModel {
    private final String clientId;
    private final String clientSecret;
    private final String script;
    private final String stdin;
    private final String language;
    private final String versionIndex;
    private final Boolean compileOnly;

    public JDoodleRequestModel(String clientId, String clientSecret, String script, String language){
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.script = script;
        this.language = language;
        this.stdin = "";
        this.versionIndex = "3";
        compileOnly = false;
    }
}
