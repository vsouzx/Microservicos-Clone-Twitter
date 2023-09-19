package br.com.souza.twitterclone.notifications.configuration.openfeign;

import br.com.souza.twitterclone.notifications.handler.exceptions.ServerSideErrorException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class CustomErrorDecode implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        String requestUrl = response.request().url();
        HttpStatus responseStatus = HttpStatus.valueOf(response.status());

        if (responseStatus.value() >= 500) {
            return new ServerSideErrorException(requestUrl);
        }else{
            return new Exception("Exception: " +  responseStatus);
        }
    }

}

