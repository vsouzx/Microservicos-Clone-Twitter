package br.com.souza.twitterclone.mailsender.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
@Builder
public class Users implements Serializable {

    @Id
    @Column(name = "identifier", length = 36)
    private String identifier;

    @Column(name = "username", length = 255)
    private String username;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "confirmed_email")
    private Boolean confirmedEmail;

    @Column(name = "confirmation_code")
    private String confirmationCode;

}
