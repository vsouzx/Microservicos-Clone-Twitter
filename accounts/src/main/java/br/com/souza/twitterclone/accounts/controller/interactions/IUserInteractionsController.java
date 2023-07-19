package br.com.souza.twitterclone.accounts.controller.interactions;

import br.com.souza.twitterclone.accounts.dto.user.UserPendingFollowRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Validated
public interface IUserInteractionsController {

    @Operation(summary = "Block and unblock users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bloqueado/desbloqueado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<Void> blockToggle(String targetIdentifier) throws Exception;

    @Operation(summary = "Follow and unfollow users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Seguido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<Void> followToggle(String targetIdentifier) throws Exception;

    @Operation(summary = "Accept/decline pendings follows.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Aceito/recusado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<Void> pendingFollowAcceptDecline(String targetIdentifier, @Valid UserPendingFollowRequest request) throws Exception;

}
