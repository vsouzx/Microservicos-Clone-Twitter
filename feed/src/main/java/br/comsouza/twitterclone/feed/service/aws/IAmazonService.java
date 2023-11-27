package br.comsouza.twitterclone.feed.service.aws;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface IAmazonService {

    List<byte[]> loadAttachmentFromS3(String tweetIdentifier);
    void saveAttachmentInBucketS3(List<MultipartFile> attachment, String tweetIdentifier) throws Exception;
}
