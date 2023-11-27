package br.comsouza.twitterclone.feed.controller.posts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@Validated
public interface IPostsController {

    @Operation(summary = "Creates a new tweet.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<Void> postNewTweet(String request, String flag, List<MultipartFile> attachments, String authorization) throws Exception;

    @Operation(summary = "Creates a new tweet's retweet.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<Void> retweetToggle(String originalTweet, String request, List<MultipartFile> attachment, String authorization) throws Exception;

    @Operation(summary = "Creates a new tweet's comment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<Void> commentToggle(String originalTweet, String request, List<MultipartFile> attachment, String identifier) throws Exception;

    @Operation(summary = "Like/unlike tweets.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sucesso"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<Void> likeToggle(String tweet, String authoriaztion) throws Exception;

    @Operation(summary = "Fav/unfav tweets.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sucesso"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<Void> favToggle(String tweet, String authorization) throws Exception;

    @Operation(summary = "Get users tweets count.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sucesso"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<Integer> getTweetsCount(String userIdentifier) throws Exception;
}
