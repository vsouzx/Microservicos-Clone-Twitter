package br.comsouza.twitterclone.feed.dto.client;

import java.util.List;
import lombok.Data;

@Data
public class GPTResponse {

    private List<Choices> choices;

}