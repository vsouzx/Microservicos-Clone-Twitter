package br.comsouza.twitterclone.feed.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TweetResponseUtil {

    public static final String COMMON_QUERY = """
                  SELECT t.tweet_identifier   
                  ,t.original_tweet_identifier   
                  ,p.description   
                  ,u.identifier   
                  ,u.username   
                  ,u.first_name   
                  ,u.profile_photo_url   
                  ,t.message   
                  ,(SELECT COUNT(*)  
                    FROM tweets t2 
                 INNER JOIN tweets_types tp 
                 	ON tp.type_identifier = t2.type 
                 WHERE t2.original_tweet_identifier = t.tweet_identifier  
                 	AND tp.description = 'COMMENT') commentsCount 
                   ,(SELECT COUNT(*)  
                    FROM tweets t2 
                 INNER JOIN tweets_types tp 
                 	ON tp.type_identifier = t2.type 
                 WHERE t2.original_tweet_identifier = t.tweet_identifier  
                 	AND tp.description IN ('RETWEET', 'NO_VALUE_RETWEET')) retweetCount 
                   ,(SELECT COUNT(*)  
                     FROM tweets_likes  
                  WHERE tweet_identifier = t.tweet_identifier) likesCount 
                   ,(SELECT COUNT(*)  
                     FROM tweets_views  
                  WHERE tweet_identifier = t.tweet_identifier) viewsCount 
                   ,(SELECT COUNT(*)  
                     FROM tweets_favs   
                  WHERE tweet_identifier = t.tweet_identifier) favsCount 
                   ,(SELECT IIF(MAX(tweet_identifier) IS NULL, CONVERT(BIT, 0), CONVERT(BIT, 1)) 
                     FROM tweets_likes  
                  WHERE tweet_identifier = t.tweet_identifier 
                  AND user_identifier = @sessionUserIdentifier) isLikedByMe 
                   ,(SELECT IIF(MAX(tweet_identifier) IS NULL, CONVERT(BIT, 0), CONVERT(BIT, 1)) 
                     FROM tweets_favs  
                  WHERE tweet_identifier = t.tweet_identifier 
                  AND user_identifier = @sessionUserIdentifier) isFavoritedByMe 
                   ,(SELECT IIF(MAX(t2.tweet_identifier) IS NULL, CONVERT(BIT, 0), CONVERT(BIT, 1)) 
                     FROM tweets t2 
                  INNER JOIN tweets_types tp 
                 	ON tp.type_identifier = t2.type 
                  WHERE t2.user_identifier = @sessionUserIdentifier 
                  AND t2.original_tweet_identifier = t.tweet_identifier 
                  AND tp.description IN ('RETWEET', 'NO_VALUE_RETWEET')) isRetweetedByMe 
                  ,t.has_attachment 
                  ,u.verified 
                  ,t.publication_time 
                     ,CASE WHEN t.user_identifier = @sessionUserIdentifier
                     		THEN CONVERT(BIT, 1) 
                     	   WHEN t.can_be_replied_by_not_followed_user = 0 /* privado para quem o dono do tweet segue */
                     		THEN (SELECT IIF(MAX(followed_identifier) IS NULL, CONVERT(BIT, 0), CONVERT(BIT, 1))
                     			  FROM users_follows
                     			  WHERE followed_identifier = @sessionUserIdentifier
                     				AND follower_identifier = u.identifier) /* verifica se o dono do tweet segue o session user */
                     	   WHEN t.can_be_replied_by_not_followed_user = -1 /* apenas para quem é verificado */
                     		THEN (SELECT verified 
                     			  FROM users
                     			  WHERE identifier = @sessionUserIdentifier) /* verifica se o session user é verificado */
                     	   ELSE CONVERT(BIT, 1) /* é liberado para todos */
                      END canBeRepliedByMe
                     ,IIF(u.identifier = @sessionUserIdentifier, CONVERT(BIT, 1), CONVERT(BIT, 0)) isTweetMine 
                     ,CASE WHEN t.user_identifier = @sessionUserIdentifier
                     		THEN NULL
                     	  ELSE (SELECT IIF(MAX(followed_identifier) IS NULL, CONVERT(BIT, 0), CONVERT(BIT, 1))
                     			FROM users_follows
                     			WHERE followed_identifier = u.identifier
                     				  AND follower_identifier = @sessionUserIdentifier)
                      END isTweetUserFollowedByMe
            """;
}
