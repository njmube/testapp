package com.app.luis.androidapp.api.models;

/**
 * Created by Luis on 05/03/2016.
 */
public class ErrorResponse404 extends ErrorResponse {
    @Override
    public String getUserMessage() {
        return getMessage();
    }
}
