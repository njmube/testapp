package com.app.luis.androidapp.api.factory;

import com.app.luis.androidapp.api.models.ErrorResponse;
import com.app.luis.androidapp.api.models.ErrorResponse404;
import com.app.luis.androidapp.api.models.ErrorResponse442;
import com.app.luis.androidapp.api.models.ErrorResponse500;

/**
 * Created by Luis on 05/03/2016.
 */
public class FactoryResponse extends AbstractFactoryResponse {

    private ErrorResponse response;

    @Override
    public ErrorResponse createHttpResponse(int status_code) {

        switch (status_code) {
            case 404:
                response = new ErrorResponse404();
                break;
            case 422:
                response = new ErrorResponse442();
                break;
            case 500:
                response = new ErrorResponse500();
                break;
        }

        return response;
    }
}
