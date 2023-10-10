DECLARE @sessionUserIdentifier UNIQUEIDENTIFIER = 'C0AA0EFD-B15B-416C-9CDE-5DF127DF5744',
		@targetUserIdentifier  UNIQUEIDENTIFIER = '08F85AB9-83FC-4CAD-A6F2-7C2A54739B8E'

SELECT identifier
	  ,user_sender_identifier
	  ,user_receiver_identifier
	  ,text												text
	  ,creation_date									messageDate
	  ,seen												seen
FROM direct_messages 
WHERE (user_sender_identifier = @sessionUserIdentifier or user_receiver_identifier = @sessionUserIdentifier)
    AND (user_sender_identifier = @targetUserIdentifier or user_receiver_identifier = @targetUserIdentifier)
ORDER by creation_date desc
