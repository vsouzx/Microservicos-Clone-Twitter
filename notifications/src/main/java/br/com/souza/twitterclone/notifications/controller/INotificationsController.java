package br.com.souza.twitterclone.notifications.controller;

import br.com.souza.twitterclone.notifications.dto.notifications.DeleteNotificationRequest;
import br.com.souza.twitterclone.notifications.dto.notifications.NewNotificationRequest;
import br.com.souza.twitterclone.notifications.dto.notifications.NotificationsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Validated
public interface INotificationsController {

    @Operation(summary = "Creates new notification.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Conexao feita com sucesso"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<Void> createNewNotification(@Valid NewNotificationRequest request,
                                               String authorization) throws Exception;

    @Operation(summary = "Get all user notifications.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Conexao feita com sucesso"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<List<NotificationsResponse>> getUserNotifications(String authorization,
                                                                     Integer page,
                                                                     Integer pageSize) throws Exception;

    @Operation(summary = "Get all user notifications.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Conexao feita com sucesso"),
            @ApiResponse(responseCode = "400", description = "Se houve erro do usuário na consulta", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno sem causa mapeada", content = @Content)
    })
    ResponseEntity<Void> deleteNotification(@Valid DeleteNotificationRequest request) throws Exception;

}
