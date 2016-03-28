package com.app.luis.androidapp.api.models;

/**
 * Created by Luis Macias on 24/03/2016.
 */
public class ErrorResponse401 extends ErrorResponse {
    @Override
    public String getUserMessage() {
        return (getMessage() == null) ? getError() : getMessage();
    }
}
