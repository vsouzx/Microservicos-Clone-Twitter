package br.com.souza.twitterclone.accounts.controller.interactions;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Validated
public interface IUserInteractionsController {

    @Operation(summary = "Follow and unfollow users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<Void> blockToggle(String targetIdentifier) throws Exception;

}
