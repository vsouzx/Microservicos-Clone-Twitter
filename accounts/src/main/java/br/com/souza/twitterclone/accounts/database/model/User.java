package br.com.souza.twitterclone.accounts.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User{

    @Id
    @Column(name = "identifier", length = 36)
    private String identifier;

    @Column(name = "first_name", length = 255)
    private String firstName;

    @Column(name = "last_name", length = 255)
    private String lastName;

    @Column(name = "username", length = 255)
    private String username;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "biography", length = 255)
    private String biography;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "site", length = 255)
    private String site;

    @Column(name = "confirmed_email")
    private Boolean confirmedEmail;

    @Column(name = "confirmation_code")
    private String confirmationCode;

    @Column(name = "registration_time")
    private LocalDateTime registrationTime;

}
