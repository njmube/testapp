package com.app.luis.androidapp.api.factory;

import com.app.luis.androidapp.api.models.ErrorResponse;

/**
 * Created by Luis on 05/03/2016.
 */
abstract public class AbstractFactoryResponse {

    protected abstract ErrorResponse createHttpResponse(int status_code);

}
