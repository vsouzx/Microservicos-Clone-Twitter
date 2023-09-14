package br.com.souza.twitterclone.accounts.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
@Builder
public class User implements Serializable {

    @Id
    @Column(name = "identifier", length = 36)
    private String identifier;

    @Column(name = "first_name", length = 255)
    private String firstName;

    @Column(name = "username", length = 255)
    private String username;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "password", length = 255)
    private String password;

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

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "registration_time")
    private LocalDateTime registrationTime;

    @Column(name = "private_account")
    private Boolean privateAccount;

    @Column(name = "language_preference")
    private String languagePreference;

    @Column(name = "profile_photo_identifier", length = 36)
    private String profilePhotoIdentifier;

    @Column(name = "background_photo_identifier", length = 36)
    private String backgroundPhotoIdentifier;

    @Column(name = "first_access")
    private Boolean firstAccess;

    @Column(name = "verified")
    private Boolean verified;

}
