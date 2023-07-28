package br.comsouza.twitterclone.feed.dto.handler.posts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewTweetRequest {

    private String message;

}
