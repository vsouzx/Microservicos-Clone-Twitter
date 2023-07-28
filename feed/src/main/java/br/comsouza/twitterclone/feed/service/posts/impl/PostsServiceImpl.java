package br.comsouza.twitterclone.feed.service.posts.impl;

import br.comsouza.twitterclone.feed.database.model.Tweets;
import br.comsouza.twitterclone.feed.database.repository.ITweetsRepository;
import br.comsouza.twitterclone.feed.enums.TweetTypeEnum;
import br.comsouza.twitterclone.feed.handler.exceptions.InvalidTweetException;
import br.comsouza.twitterclone.feed.service.posts.IPostsService;
import br.comsouza.twitterclone.feed.service.tweettype.ITweetTypeService;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PostsServiceImpl implements IPostsService {

    private final ITweetsRepository tweetsRepository;
    private final ITweetTypeService iTweetTypeService;

    public PostsServiceImpl(ITweetsRepository tweetsRepository,
                            ITweetTypeService iTweetTypeService) {
        this.tweetsRepository = tweetsRepository;
        this.iTweetTypeService = iTweetTypeService;
    }

    @Override
    public void postNewTweet(String message, String sessionIdIdentifier, MultipartFile attachment) throws Exception{
        final String tweetIdentifier = UUID.randomUUID().toString();

        if((message == null || message.isBlank()) && (attachment == null || attachment.isEmpty())){
            throw new InvalidTweetException();
        }

        tweetsRepository.save(Tweets.builder()
                .tweetIdentifier(tweetIdentifier)
                .userIdentifier(sessionIdIdentifier)
                .originalTweetIdentifier(tweetIdentifier)
                .message(message)
                .messageTranslations(null) // TODO: Fazer chamada ao chat GPT numa thread paralela
                .type(iTweetTypeService.findTweetTypeByDescription(TweetTypeEnum.TWEET.toString()).getTypeIdentifier())
                .publicationTime(LocalDateTime.now())
                .attachment(!attachment.isEmpty() ? attachment.getBytes() : null)
                .build());
    }

}
