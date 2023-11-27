package br.comsouza.twitterclone.feed.service.aws.impl;

import br.comsouza.twitterclone.feed.service.aws.IAmazonService;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class AmazonServiceImpl implements IAmazonService{

    private final AmazonS3 s3client;

    public AmazonServiceImpl(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    @Override
    public List<byte[]> loadAttachmentFromS3(String tweetIdentifier) {
        int attachmentCount = 0;
        List<byte[]> response = new ArrayList<>();
        do{
            try{
                S3Object s3Object = s3client.getObject(System.getenv("BUCKET_NAME"), tweetIdentifier.toUpperCase() + attachmentCount);
                S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
                response.add(IOUtils.toByteArray(s3ObjectInputStream));
                attachmentCount++;
            }catch (Exception e){
                return !response.isEmpty() ? response : null;
            }
        }while (attachmentCount < 4);
        return response;
    }

    @Override
    public void saveAttachmentInBucketS3(List<MultipartFile> attachments, String tweetIdentifier) throws Exception {
        int attachmentCount = 0;

        for(MultipartFile attachment : attachments){
            if(attachment != null && !attachment.isEmpty()){
                File file = new File(tweetIdentifier.toUpperCase());
                try (OutputStream os = new FileOutputStream(file)) {
                    os.write(attachment.getBytes());
                }

                s3client.putObject(System.getenv("BUCKET_NAME"), tweetIdentifier.toUpperCase() + attachmentCount, file);
                file.delete();
                attachmentCount++;
            }
        }
    }
}