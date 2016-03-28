package com.app.luis.androidapp.api.models;

/**
 * Created by Luis on 05/03/2016.
 */
public class ErrorResponse500 extends ErrorResponse {

    private int code;

    @Override
    public String getUserMessage() {
        return (getMessage() == null) ? getError() : getMessage();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
