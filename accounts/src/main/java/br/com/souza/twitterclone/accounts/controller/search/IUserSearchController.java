package br.com.souza.twitterclone.accounts.controller.search;

import br.com.souza.twitterclone.accounts.dto.pagination.CustomPage;
import br.com.souza.twitterclone.accounts.dto.user.UserDetailsByIdentifierResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserDetailsResponse;
import br.com.souza.twitterclone.accounts.dto.user.UserPreviewResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Validated
public interface IUserSearchController {

    @Operation(summary = "Retorna as informações do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o response"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<UserDetailsResponse> getUserInfos() throws Exception;

    @Operation(summary = "Retorna as informações de um usuário pesquisado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o response"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<UserDetailsByIdentifierResponse> getUserInfosByIdentifier(String targetUserIdentifier) throws Exception;

    @Operation(summary = "Retorna uma lista de usuários de acordo com o username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma lista"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<CustomPage<UserPreviewResponse>> getUsersByUsername(String targetUserIdentifier, Integer page, Integer size) throws Exception;
}
