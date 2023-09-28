package br.com.souza.twitterclone.accounts.controller.search;

import br.com.souza.twitterclone.accounts.dto.user.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;

import org.springframework.context.annotation.Profile;
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
    ResponseEntity<UserDetailsResponse> getUserInfos(String authorization) throws Exception;

    @Operation(summary = "Retorna as informações de um usuário pesquisado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o response"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<UserDetailsByIdentifierResponse> getUserInfosByIdentifier(String targetUserIdentifier,
                                                                             String authorization) throws Exception;

    @Operation(summary = "Retorna uma lista de usuários de acordo com o username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma lista"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<List<UserPreviewResponse>> getUsersByUsername(String targetUserIdentifier, Integer page, Integer size) throws Exception;

    @Operation(summary = "Retorna uma lista de usuários de acordo com o username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma lista"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<List<UserPreviewResponse>> getUserFollowers(String targetUserIdentifier, Integer page, Integer size) throws Exception;

    @Operation(summary = "Retorna uma lista quem o usuário segue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma lista"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<List<UserPreviewResponse>> getUserFollows(String targetUserIdentifier, Integer page, Integer size) throws Exception;

    @Operation(summary = "Retorna uma lista quem o usuário segue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma lista"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<List<UserPreviewResponse>> getUserPendingFollowers(Integer page, Integer size) throws Exception;

    @Operation(summary = "Verifica se o e-mail existe ou não")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma lista"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<ValidEmailResponse> isValidEmail(@Parameter(description = "Email a ser validado") String email) throws Exception;

    @Operation(summary = "Verifica se o username (@) existe ou não")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma lista"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<ValidUsernameResponse> isValidUsername(@Parameter(description = "Email a ser validado") String username) throws Exception;

    @Operation(summary = "Verifica se o username (@) ou email existe ou não")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma lista"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<ValidUserResponse> isValidUser(@Parameter(description = "User a ser validado") String username) throws Exception;

    @Operation(summary = "Lista os usuários verificados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma lista"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<List<UserPreviewResponse>> getVerified() throws Exception;

    @Operation(summary = "Retorna os dados da foto de perfil do user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma lista"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<ProfilePhotoResponse> loadProfilePhoto(String userIdentifier) throws Exception;

    @Operation(summary = "Retorna os dados da foto de perfil do user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma lista"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<List<String>> getAlertedUsers() throws Exception;
}
