package br.com.souza.twitterclone.accounts.controller.search;

import br.com.souza.twitterclone.accounts.dto.user.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    ResponseEntity<List<UserDetailsByIdentifierResponse>> getUsersByUsername(String targetUserIdentifier,
                                                                             @Parameter(description = "Numero da pagina") Integer page,
                                                                             @Parameter(description = "Tamanho da pagina") Integer size,
                                                                             String authorization) throws Exception;

    @Operation(summary = "Retorna uma lista de usuários de acordo com o username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma lista"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<List<UserDetailsByIdentifierResponse>> getUserFollowsDetails(@Parameter(description = "Id do usuário") String targetUserIdentifier,
                                                                                @Parameter(description = "Tipo da busca: 'following', 'followers', 'know_followers', 'verified_followers'") String type,
                                                                                @Parameter(description = "Numero da pagina") Integer page,
                                                                                @Parameter(description = "Tamanho da pagina") Integer size,
                                                                                String auth) throws Exception;

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

    @Operation(summary = "Lista usuários aleatórios para seguir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma lista"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<List<UserDetailsByIdentifierResponse>> getWhoToFollow(@Parameter(description = "Numero da pagina") Integer page,
                                                                         @Parameter(description = "Tamanho da pagina") Integer size,
                                                                         @Parameter(description = "Usuário que não é para aparecer") String userOnScreen,
                                                                         @Parameter(description = "Retornar apenas verificados") Boolean isVerified,
                                                                         String authorization) throws Exception;

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
    ResponseEntity<List<String>> getAlertedUsers() throws Exception;

    @Operation(summary = "Lista os seguidores em comum entre usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma lista"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<FollowsAndFollowersResponse> getFollowsAndFollowers(@Parameter(description = "Identificador do target useer") String targetUserIdentifier) throws Exception;
}
