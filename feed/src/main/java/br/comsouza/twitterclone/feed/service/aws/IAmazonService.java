package br.comsouza.twitterclone.feed.service.aws;

import org.springframework.web.multipart.MultipartFile;

public interface IAmazonService {

    byte[] loadAttachmentFromS3(String tweetIdentifier);
    void saveAttachmentInBucketS3(MultipartFile attachment, String tweetIdentifier) throws Exception;
}
