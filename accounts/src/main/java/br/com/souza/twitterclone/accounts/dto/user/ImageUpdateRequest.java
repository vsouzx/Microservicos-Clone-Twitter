package br.com.souza.twitterclone.accounts.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageUpdateRequest {

    @NotNull
    private String xPosition;

    @NotNull
    private String yPosition;

}
