package com.livemybike.shop.images;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

@Repository
public class S3ImageRepo implements ImageRepo {

    @Value("${aws.s3AccessKey}")
    private String s3AccessKey;

    @Value("${aws.s3SecretKey}")
    private String s3SecretKey;

    @Value("${aws.s3Bucket}")
    private String s3Bucket;

    private AmazonS3 amazonS3;

    @PostConstruct
    public void init() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(s3AccessKey, s3SecretKey);
        amazonS3 = new AmazonS3Client(awsCredentials);
        Region euFrankfurt = Region.getRegion(Regions.EU_CENTRAL_1);
        amazonS3.setRegion(euFrankfurt);
    }

    @Override
    public void save(Image image, Long offerId) {

        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType(image.getContentType());
        meta.setContentLength(image.getSize());

        StringBuilder imageName = new StringBuilder();
        imageName.append(offerId).append(image.getImageSize().getValue())
                .append("_").append(image.getName());

        try {
            amazonS3.putObject(s3Bucket, imageName.toString(), image.getImageInputStream(), meta);
        } catch (AmazonClientException e) {
            throw new ImageStoringException("Can not upload image to s3", e);
        }
    }

}
