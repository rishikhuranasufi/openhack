package com.learn.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import org.socialsignin.spring.data.dynamodb.mapping.DynamoDBMappingContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * Created by rishi.khurana on 10/10/2018.
 */
@Configuration
public class DynamoDBConfig {

    @Value("${api.aws.dynamodb.accesskey}")
    private String amazonAWSAccessKey;

    @Value("${api.aws.dynamodb.secretkey}")
    private String amazonAWSSecretKey;

    @Value("${api.aws.dynamodb.endpoint}")
    private String dynamoDbEndpoint;

    public AWSCredentialsProvider amazonAWSCredentialsProvider() {
        return new AWSStaticCredentialsProvider(amazonAWSCredentials());
    }

    @Bean
    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
    }

    @Bean
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB, DynamoDBMapperConfig config) {
        return new DynamoDBMapper(amazonDynamoDB, config);
    }

    @Bean
    public DynamoDBMapperConfig dynamoDBMapperConfig() {
        return DynamoDBMapperConfig.DEFAULT;
    }

    @Bean("amazonDynamoDB")
    public AmazonDynamoDB amazonDynamoDB() {
        AmazonDynamoDB amazonDynamoDB = new AmazonDynamoDBClient(amazonAWSCredentials());
        amazonDynamoDB.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_2));

        if (!StringUtils.isEmpty(dynamoDbEndpoint)) {
            amazonDynamoDB.setEndpoint(dynamoDbEndpoint);
        }

        return amazonDynamoDB;
    }

    @Bean
    public DynamoDBMappingContext dynamoDBMappingContext() {
        return new DynamoDBMappingContext();
    }

}
