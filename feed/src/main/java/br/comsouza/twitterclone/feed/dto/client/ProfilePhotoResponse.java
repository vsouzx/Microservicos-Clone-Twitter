package br.comsouza.twitterclone.feed.dto.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfilePhotoResponse {

    private Integer xPosition;
    private Integer yPosition;
    private byte[] photo;

}
