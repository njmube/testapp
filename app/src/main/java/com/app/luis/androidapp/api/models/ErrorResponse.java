package com.app.luis.androidapp.api.models;

/**
 * Created by Luis on 05/03/2016.
 */
abstract public class ErrorResponse {

    private String message;
    private String error;
    private int status_code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    abstract public String getUserMessage();
}
