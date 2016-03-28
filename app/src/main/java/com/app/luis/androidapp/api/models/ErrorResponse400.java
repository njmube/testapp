package com.app.luis.androidapp.api.models;

/**
 * Created by Luis Macias on 23/03/2016.
 */
public class ErrorResponse400 extends ErrorResponse {
    @Override
    public String getUserMessage() {
        return (getMessage() == null) ? getError() : getMessage();
    }
}
