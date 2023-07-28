package br.comsouza.twitterclone.feed.service.posts.impl;

import br.comsouza.twitterclone.feed.database.model.Tweets;
import br.comsouza.twitterclone.feed.database.model.TweetsTypes;
import br.comsouza.twitterclone.feed.database.repository.ITweetsRepository;
import br.comsouza.twitterclone.feed.enums.TweetTypeEnum;
import br.comsouza.twitterclone.feed.handler.exceptions.InvalidTweetException;
import br.comsouza.twitterclone.feed.handler.exceptions.TweetNotFoundException;
import br.comsouza.twitterclone.feed.service.posts.IPostsService;
import br.comsouza.twitterclone.feed.service.tweettype.ITweetTypeService;
import java.time.LocalDateTime;
import java.util.Optional;
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
    public void postNewTweet(String message, String sessionUserIdentifier, MultipartFile attachment) throws Exception {
        final String tweetIdentifier = UUID.randomUUID().toString();

        if ((message == null || message.isBlank()) && (attachment == null || attachment.isEmpty())) {
            throw new InvalidTweetException();
        }

        tweetsRepository.save(Tweets.builder()
                .tweetIdentifier(tweetIdentifier)
                .userIdentifier(sessionUserIdentifier)
                .originalTweetIdentifier(tweetIdentifier)
                .message(message)
                .messageTranslations(null) // TODO: Fazer chamada ao chat GPT numa thread paralela
                .type(iTweetTypeService.findTweetTypeByDescription(TweetTypeEnum.TWEET.toString()).getTypeIdentifier())
                .publicationTime(LocalDateTime.now())
                .attachment(!attachment.isEmpty() ? attachment.getBytes() : null)
                .build());
    }

    @Override
    public void retweetToggle(String message, String sessionUserIdentifier, MultipartFile attachment, String originalTweetIdentifier) throws Exception {
        Optional<Tweets> originalTweet = tweetsRepository.findById(originalTweetIdentifier);

        //TODO: adicionar lógica para ver se o dono do tweet original não bloqueou o session user ou vice versa
        if (originalTweet.isEmpty()) {
            throw new TweetNotFoundException();
        }

        TweetsTypes tweetType = iTweetTypeService.findTweetTypeByDescription(TweetTypeEnum.RETWEET.toString());

        Optional<Tweets> retweet = tweetsRepository.findByUserIdentifierAndOriginalTweetIdentifierAndType(sessionUserIdentifier, originalTweetIdentifier, tweetType.getTypeIdentifier());

        if (retweet.isPresent()) {
            tweetsRepository.delete(retweet.get());
        } else {
            tweetsRepository.save(Tweets.builder()
                    .tweetIdentifier(UUID.randomUUID().toString())
                    .userIdentifier(sessionUserIdentifier)
                    .originalTweetIdentifier(originalTweetIdentifier)
                    .message(message)
                    .messageTranslations(null) // TODO: Fazer chamada ao chat GPT numa thread paralela
                    .type(tweetType.getTypeIdentifier())
                    .publicationTime(LocalDateTime.now())
                    .attachment(!attachment.isEmpty() ? attachment.getBytes() : null)
                    .build());
        }
    }

    @Override
    public void commentToggle(String message, String sessionUserIdentifier, MultipartFile attachment, String originalTweetIdentifier) throws Exception {
        Optional<Tweets> originalTweet = tweetsRepository.findById(originalTweetIdentifier);

        //TODO: adicionar lógica para ver se o dono do tweet original não bloqueou o session user ou vice versa
        if (originalTweet.isEmpty()) {
            throw new TweetNotFoundException();
        }

        TweetsTypes tweetType = iTweetTypeService.findTweetTypeByDescription(TweetTypeEnum.COMMENT.toString());

        Optional<Tweets> retweet = tweetsRepository.findByUserIdentifierAndOriginalTweetIdentifierAndType(sessionUserIdentifier, originalTweetIdentifier, tweetType.getTypeIdentifier());

        if (retweet.isPresent()) {
            tweetsRepository.delete(retweet.get());
        } else {
            tweetsRepository.save(Tweets.builder()
                    .tweetIdentifier(UUID.randomUUID().toString())
                    .userIdentifier(sessionUserIdentifier)
                    .originalTweetIdentifier(originalTweetIdentifier)
                    .message(message)
                    .messageTranslations(null) // TODO: Fazer chamada ao chat GPT numa thread paralela
                    .type(tweetType.getTypeIdentifier())
                    .publicationTime(LocalDateTime.now())
                    .attachment(!attachment.isEmpty() ? attachment.getBytes() : null)
                    .build());
        }
    }
}
