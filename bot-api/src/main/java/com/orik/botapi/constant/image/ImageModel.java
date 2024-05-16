package com.orik.botapi.constant.image;


public enum ImageModel {
    DALL_E_2("dall-e-2"),
    DALL_E_3("dall-e-3");
    private final String value;

    ImageModel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ImageModel fromValue(String value) {
        for (ImageModel model : ImageModel.values()) {
            if (model.value.equals(value)) {
                return model;
            }
        }
        throw new IllegalArgumentException("Invalid ImageModel value: " + value);
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
