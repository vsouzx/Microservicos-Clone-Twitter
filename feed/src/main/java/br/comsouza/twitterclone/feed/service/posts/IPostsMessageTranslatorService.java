package br.comsouza.twitterclone.feed.service.posts;

import br.comsouza.twitterclone.feed.database.model.Tweets;
import br.comsouza.twitterclone.feed.dto.posts.TimelineTweetResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IPostsMessageTranslatorService {

    void translateMessage(Tweets tweet) throws Exception;
}
