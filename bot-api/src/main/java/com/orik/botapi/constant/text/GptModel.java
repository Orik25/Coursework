package com.orik.botapi.constant.text;

public enum GptModel {
    GPT_4("gpt-4-turbo"),
    GPT_3("gpt-3.5-turbo");
    private final String value;

    GptModel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static GptModel fromValue(String value) {
        for (GptModel model : GptModel.values()) {
            if (model.value.equals(value)) {
                return model;
            }
        }
        throw new IllegalArgumentException("Invalid GptModel value: " + value);
    }

    public static boolean isOneOf(String value){
        try{
            fromValue(value);
            return true;
        }catch (IllegalArgumentException ex){
            return false;
        }
    }
}
