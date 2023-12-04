package br.comsouza.twitterclone.feed.controller.posts;

import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Validated
public interface IPostsDetailsController {

    @Operation(summary = "Get tweet details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sucesso"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<TimelineTweetResponse> getTweetDetails(String identifier,
                                                          String authorization,
                                                          Boolean load) throws Exception;

    @Operation(summary = "Get tweet list comments.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sucesso"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<List<TimelineTweetResponse>> getTweetComments(String identifier,
                                                                 @Parameter(description = "Page number") @NotNull Integer page,
                                                                 @Parameter(description = "Page size") @NotNull Integer size) throws Exception;

}
