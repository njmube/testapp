package com.app.luis.androidapp.api.factory;

import com.app.luis.androidapp.api.models.*;

/**
 * Created by Luis on 05/03/2016.
 */
public class FactoryResponse extends AbstractFactoryResponse {

    private ErrorResponse response;

    @Override
    public ErrorResponse createHttpResponse(int status_code) {

        switch (status_code) {
            case 400:
                response = new ErrorResponse400();
                break;
            case 401:
                response = new ErrorResponse401();
                break;
            case 404:
                response = new ErrorResponse404();
                break;
            case 405:
                response = new ErrorResponse405();
                break;
            case 422:
                response = new ErrorResponse422();
                break;
            case 500:
                response = new ErrorResponse500();
                break;
        }

        return response;
    }
}
