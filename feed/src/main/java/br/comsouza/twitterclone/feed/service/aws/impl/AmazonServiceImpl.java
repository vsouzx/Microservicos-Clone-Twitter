package br.comsouza.twitterclone.feed.service.aws.impl;

import br.comsouza.twitterclone.feed.service.aws.IAmazonService;
import br.comsouza.twitterclone.feed.util.TextHelper;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Objects;
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
    public byte[] loadAttachmentFromS3(String tweetIdentifier) {
        try{
            S3Object s3Object = s3client.getObject(System.getenv("BUCKET_NAME"), tweetIdentifier.toUpperCase());
            S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
            return IOUtils.toByteArray(s3ObjectInputStream);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public void saveAttachmentInBucketS3(MultipartFile attachment, String tweetIdentifier) throws Exception {
        if(attachment != null && !attachment.isEmpty()){
            File file = new File(tweetIdentifier.toUpperCase());
            try (OutputStream os = new FileOutputStream(file)) {
                os.write(attachment.getBytes());
            }

            s3client.putObject(System.getenv("BUCKET_NAME"), tweetIdentifier.toUpperCase(), file);
            file.delete();
        }
    }


}