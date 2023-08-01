package br.comsouza.twitterclone.feed.controller.timeline;

import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ITimelineController {

    @Operation(summary = "Returns the 'following' timeline.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Retornado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<List<TimelineTweetResponse>> getFollowingTimeline(Integer page, Integer size) throws Exception;
}
