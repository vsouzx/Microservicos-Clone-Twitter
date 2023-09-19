package br.com.souza.twitterclone.accounts.dto.user;

import br.com.souza.twitterclone.accounts.database.model.Images;
import br.com.souza.twitterclone.accounts.database.repository.IImagesRepository;
import jakarta.validation.constraints.NotNull;
import java.awt.Image;
import java.util.Optional;
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

    public ProfilePhotoResponse(IImagesRepository iImagesRepository, String profilePhotoIdentifier) throws Exception {
            Images image = iImagesRepository.findById(profilePhotoIdentifier)
                    .orElseThrow(() -> new Exception("Image could not be found"));

            this.xPosition = image.getXPosition();
            this.yPosition = image.getYPosition();
            this.photo = image.getPhoto();
    }
}
