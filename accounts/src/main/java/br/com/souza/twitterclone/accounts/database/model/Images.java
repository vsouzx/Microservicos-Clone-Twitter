package br.com.souza.twitterclone.accounts.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "images")
@Builder
public class Images implements Serializable {

    @Id
    @Column(name = "identifier", length = 36)
    private String identifier;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "Xposition")
    private Integer xPosition;

    @Column(name = "Yposition")
    private Integer yPosition;

}
