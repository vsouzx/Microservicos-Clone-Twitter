package br.comsouza.twitterclone.feed.service.interactions;

public interface IInteractionsService {

    Integer getTweetCommentsCount(String tweetIdentifier);
    Integer getTweetRetweetsCount(String tweetIdentifier);
    Integer getTweetLikesCount(String tweetIdentifier);
    Integer getTweetViewsCount(String tweetIdentifier);
}
