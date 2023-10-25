package br.com.souza.twitterclone.gateway.service;

import br.com.souza.twitterclone.gateway.exception.CustomErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ErrorReturnTransferImpl {

    private static final String GENERIC_ERROR = "500.000";
    private static final String GENERIC_ERROR_MESSAGE = "Untracked internal error.";

    public byte[] process(ObjectMapper objectMapper, String texto) {
        CustomErrorResponse custom = null;

        try {
            custom = objectMapper.readValue(texto, CustomErrorResponse.class);
        } catch (Exception ex) {
            log.error("[ErrorReturnTransferHelper] Erro ao realizar o parse do texto [{}].", texto, ex);
        }

        String strResponse = "{\n" +
                "    \"error\": \"%s\",\n" +
                "    \"message\": \"%s\"\n" +
                "}";

        if (custom == null) {
            strResponse = String.format(strResponse, GENERIC_ERROR, GENERIC_ERROR_MESSAGE);
            return strResponse.getBytes(StandardCharsets.UTF_8);
        } else {
            strResponse = String.format(strResponse, custom.getCode(), custom.getError());
            return strResponse.getBytes(StandardCharsets.UTF_8);
        }
    }
}

