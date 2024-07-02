package com.study.connection.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * s3 설정
 */
@Lazy
@Configuration
public class S3Config {
    @Value("${aws.lambda.accessKey}")
    private String accessKeyId;

    @Value("${aws.lambda.secretAccessKey}")
    private String secretKey;

    @Value("${aws.lambda.region}")
    private String region;

    @Bean
    public AmazonS3 amazonS3 (){
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretKey);
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}
