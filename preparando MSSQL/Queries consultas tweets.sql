--Ver quantidade de likes/views de um tweet
SELECT t.tweet_identifier
	  ,MAX(t.message)
      ,MAX(t.message_translations)
	  ,MAX(t.attachment)
	  ,COUNT(k.tweet_identifier) likes
FROM tweets t
INNER JOIN tweets_likes k
	ON k.tweet_identifier = t.tweet_identifier
WHERE t.tweet_identifier = 'C2119267-26EB-4C07-907A-F00839F4E1A8'
GROUP BY t.tweet_identifier, k.tweet_identifier

--Ver quem curtiu o tweet
SELECT u.identifier
	  ,u.username
FROM tweets t
INNER JOIN tweets_likes k
	ON k.tweet_identifier = t.tweet_identifier
INNER JOIN users u
	ON u.identifier= k.user_identifier
WHERE t.tweet_identifier = 'C2119267-26EB-4C07-907A-F00839F4E1A8'

--Ver quantidade de likes views de um tweet
SELECT t.tweet_identifier
	  ,MAX(t.message)
      ,MAX(t.message_translations)
	  ,MAX(t.attachment)
	  ,COUNT(v.tweet_identifier) views
FROM tweets t
INNER JOIN tweets_views v
	ON v.tweet_identifier = t.tweet_identifier
WHERE t.tweet_identifier = 'C2119267-26EB-4C07-907A-F00839F4E1A8'
GROUP BY t.tweet_identifier