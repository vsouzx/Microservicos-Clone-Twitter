package br.comsouza.twitterclone.feed.controller.favs;

import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Validated
@Tag(name = "Favorites", description = "Favorites controller")
public interface IFavoritesController {

    @Operation(summary = "Gets favorites tweets list.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sucesso"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<List<TimelineTweetResponse>> getFavsTweets(@Parameter(description = "Page number") Integer pageNumber,
                                                              @Parameter(description = "Page size") Integer pageSize,
                                                              String authorization) throws Exception;
}
