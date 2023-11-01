package br.com.souza.twitterclone.accounts.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users_search_historic")
@Builder
public class UsersSearchHistoric{

    @Id
    @Column(name = "identifier", length = 36)
    private String identifier;

    @Column(name = "searcher_identifier", length = 36)
    private String searcherIdentifier;

    @Column(name = "text")
    private String text;

    @Column(name = "searched_identifier", length = 36)
    private String searchedIdentifier;

    @Column(name = "search_date")
    private LocalDateTime searchDate;

}
