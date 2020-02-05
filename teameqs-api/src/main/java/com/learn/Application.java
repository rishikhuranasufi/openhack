package com.learn;
/**
 * Created by rishi.khurana on 9/12/2018..
 */
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.*;
import com.learn.model.User;
import com.learn.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableDynamoDBRepositories(mappingContextRef = "dynamoDBMappingContext", includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {UserRepository.class})})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Bean
    CommandLineRunner init(UserRepository dynamoDBRepository,
                           AmazonDynamoDB amazonDynamoDB, DynamoDBMapper dynamoDBMapper,
                           DynamoDBMapperConfig config) {
        /*return args -> {
            checkOrCreateTable(amazonDynamoDB, dynamoDBMapper, config, User.class);
            createEntities(dynamoDBRepository);
        };*/
        return null;
    }

    public static void checkOrCreateTable(AmazonDynamoDB amazonDynamoDB, DynamoDBMapper mapper,
                                          DynamoDBMapperConfig config, Class<?> entityClass) {
        String tableName = entityClass.getAnnotation(DynamoDBTable.class).tableName();
        try {
            //amazonDynamoDB.describeTable(tableName);
            log.info("Table {} found", tableName);
            return;
        } catch (ResourceNotFoundException rnfe) {
            log.warn("Table {} doesn't exist - Creating", tableName);
        }

        CreateTableRequest ctr = mapper.generateCreateTableRequest(entityClass, config);
        ProvisionedThroughput pt = new ProvisionedThroughput(1L, 1L);
        ctr.withProvisionedThroughput(pt);
        List<GlobalSecondaryIndex> gsi = ctr.getGlobalSecondaryIndexes();
        if (gsi != null) {
            gsi.forEach(aGsi -> aGsi.withProvisionedThroughput(pt));
        }

        //amazonDynamoDB.createTable(ctr);
        //waitForDynamoDBTable(amazonDynamoDB, tableName);
    }

    public static void waitForDynamoDBTable(AmazonDynamoDB amazonDynamoDB, String tableName) {
        do {
            try {
                Thread.sleep(5 * 1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException("Couldn't wait detect table " + tableName);
            }
        } while (!amazonDynamoDB.describeTable(tableName).getTable().getTableStatus()
                .equals(TableStatus.ACTIVE.name()));
    }

    private void createEntities(UserRepository dynamoDBRepository) {
        if(dynamoDBRepository.findByUsername("portaladmin") == null) {
            dynamoDBRepository.save(new User("portal.admin@eqs.com","$2a$10$qBEktYcjQiT6I5sORy2.c.mTMM3YJE86OLVDUrg0S9a7v5Qo.0uH2","Portal Admin","portaladmin","ADMIN"));
        }
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                //System.out.println(beanName);
            }

        };
    }

}