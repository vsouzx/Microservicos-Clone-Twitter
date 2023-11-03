package br.com.souza.twitterclone.accounts.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveHistoricRequest {

    private String text;
    private String targetUserIdentifier;

}
