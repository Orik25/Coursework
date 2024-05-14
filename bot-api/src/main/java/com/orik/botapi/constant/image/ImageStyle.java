package com.orik.botapi.constant.image;

public enum ImageStyle {
    VIVID("vivid"),
    NATURAL("natural");
    private final String value;

    ImageStyle(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ImageStyle fromValue(String value) {
        for (ImageStyle model : ImageStyle.values()) {
            if (model.value.equals(value)) {
                return model;
            }
        }
        throw new IllegalArgumentException("Invalid ImageStyle value: " + value);
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
