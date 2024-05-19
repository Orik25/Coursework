package com.orik.adminapi.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TelegramResponse {
    @JsonProperty("ok")
    private Boolean ok;

    public Boolean isOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

}
