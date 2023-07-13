package br.com.souza.twitterclone.authentication.controller;

import br.com.souza.twitterclone.authentication.dto.auth.LoginRequest;
import br.com.souza.twitterclone.authentication.dto.auth.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

@Validated
public interface IAuthenticationController {

    @Operation(summary = "Endpoint de autenticação dos usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o token de autenticação"),
            @ApiResponse(responseCode = "400", description = "Se houve erro na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Privilégio insuficiente", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<TokenResponse> authenticate(@Valid @RequestBody LoginRequest request) throws Exception;

}
