package com.orik.botapi.constant.image;

public enum ImageSize {
    _256x256("256x256"),
    _512x512("512x512"),
    _1024x1024("1024x1024"),
    _1792x1024("1792x1024"),
    _1024x1792("1024x1792");
    private final String value;

    ImageSize(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ImageSize fromValue(String value) {
        for (ImageSize model : ImageSize.values()) {
            if (model.value.equals(value)) {
                return model;
            }
        }
        throw new IllegalArgumentException("Invalid ImageSize value: " + value);
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
