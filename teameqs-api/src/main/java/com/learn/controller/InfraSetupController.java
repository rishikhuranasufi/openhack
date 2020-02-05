package com.learn.controller;

import com.learn.Application;
import com.learn.model.User;
import com.learn.payload.ApiResponse;
import com.learn.payload.UserDetails;
import com.learn.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@RestController
@Api("Infrastructure Controller API's are developed to configure cloud pipeline and configuring Slack notifications")
public class InfraSetupController {

    public static final String SUCCESS = "Success";
    public static final String FAIL = "Fail";
    public static final String HTTPS_HOOKS_SLACK_COM = "https://hooks.slack.com/";
    public static final String HOME_EC_2_USER_OPENHACK_LAMBDA = "/home/ec2-user/openhack/lambda";
    public static final String LAMBDA_TERRAFORM_SH = "/lambdaTerraform.sh";
    @Autowired
    private UserRepository repository;
    @Autowired
    PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @RequestMapping(value = "/v1/createPipeline/{accessKey}/{secretKey}/{repoName}/{environment}", method = RequestMethod.GET)
    public ResponseEntity<?> createPipeline(@PathVariable @ApiParam(value="AWS Access key" ,required = true) String accessKey,
                                            @PathVariable @ApiParam(value="AWS Secret key" ,required = true) String secretKey,
                                            @PathVariable @ApiParam(value="AWS Code Commit repo name" ,required = true) String repoName,
                                            @PathVariable @ApiParam(value="Environment of AWS Pipeline " ,required = true) String environment) {

        String gitURL = "https://git-codecommit.us-east-1.amazonaws.com/v1/repos/"+repoName;

        String gitURLInArray[] = gitURL.split("/");
        String appName = gitURLInArray[gitURLInArray.length-1];

        log.info("GIT URL is "+gitURL);
        log.info("Environment is "+environment);
        log.info("AppName is "+appName);

        String cmdToExecute="terraform apply -auto-approve -var access_key="+accessKey+" "+
                " -var secret_key="+secretKey+"" +
                " -var gitURL="+gitURL+"" +
                " -var app_name="+appName+"" +
                " -var env="+environment+"";

        String cmdSecondResponse =executeCommand(cmdToExecute,"/home/ec2-user/openhack");

        if (cmdSecondResponse.equals(FAIL))
            return new ResponseEntity<ApiResponse>(new ApiResponse(false, "ISSUE while executing command "
                    +cmdToExecute),
                    HttpStatus.NOT_IMPLEMENTED);

        return ResponseEntity.ok(new ApiResponse(true,"Command executed successfully "
                +cmdToExecute));
    }


    @RequestMapping(value = "/v1/configureNotification/{accessKey}/{secretKey}/{slackHook}/{message}/{slackUserName}/{slackChannel}/{repoName}", method = RequestMethod.GET)
    public ResponseEntity<?> configureNotification(@PathVariable @ApiParam(value="AWS access key" ,required = true) String accessKey,
                                                   @PathVariable @ApiParam(value="AWS Secret key" ,required = true) String secretKey,
                                                   @PathVariable @ApiParam(value="Slack hook details" ,required = true) String slackHook,
                                                   @PathVariable @ApiParam(value="PIPELINE_NAME has STATE, message" ,required = true) String message,
                                                   @PathVariable @ApiParam(value="User name via notification will be send" ,required = true) String slackUserName,
                                                   @PathVariable @ApiParam(value="Slack channel in which notification will be send" ,required = true) String slackChannel,
                                                   @PathVariable @ApiParam(value="AWS Code Commit repo name" ,required = true) String repoName) {

        String slackWebURL = HTTPS_HOOKS_SLACK_COM + "services/TP7GK0LLT/BPK1ALJ93/mmXDzYh9pLUiAdkh1bR5e1SU";
        log.info("Slack Web Service details "+slackHook);
        log.info("Slack Web URL "+slackWebURL);
        log.info("Notification message "+message);
        log.info("AppName is "+repoName);
        log.info("Slack Channel "+slackChannel);
        log.info("Slack UserName "+slackUserName);

        String messageParameter = message.contains(" ")?" -var message=\""+message+"\"":" -var message="+message;
        String cmdToExecute="terraform apply -auto-approve -var access_key="+accessKey+""+
                " -var secret_key="+secretKey+"" +
                " -var slack_webhook_url="+slackWebURL+"" +
                messageParameter+
                " -var slack_username="+slackUserName+"" +
                " -var slack_channel="+slackChannel+"" +
                " -var repo_name="+repoName+"";

        String cmdSecondResponse =executeCommand(cmdToExecute, HOME_EC_2_USER_OPENHACK_LAMBDA);

        if (cmdSecondResponse.equals(FAIL))
            return new ResponseEntity<ApiResponse>(new ApiResponse(false, "ISSUE while executing command "
                    +cmdToExecute),
                    HttpStatus.NOT_IMPLEMENTED);

        return ResponseEntity.ok(new ApiResponse(true,"Command executed successfully "
                +cmdToExecute));
    }


    //terraform apply -var foo=bar -var foo=baz

    private String executeCommand(String cmdToExecute, String commandToBeExecutedFrom) {

        log.info("Command to be executed "+ cmdToExecute);
        log.info("Command to be executed From "+ commandToBeExecutedFrom);

        try {

            // -- Linux --

            // Run a shell command
            // Process process = Runtime.getRuntime().exec("ls /home/mkyong/");

            // Run a shell script
            // Process process = Runtime.getRuntime().exec("path/to/hello.sh");

            // -- Windows --

            // Run a command
            //Process process = Runtime.getRuntime().exec("cmd /c dir C:\\Users\\mkyong");

            //Run a bat file
            Process process;
            if(commandToBeExecutedFrom.equals(HOME_EC_2_USER_OPENHACK_LAMBDA)) {
                createFile(cmdToExecute);
                String[] cmd = { "sh", HOME_EC_2_USER_OPENHACK_LAMBDA+LAMBDA_TERRAFORM_SH};
                process = Runtime.getRuntime().exec(cmd,null, new File(commandToBeExecutedFrom));
            }else
                process = Runtime.getRuntime().exec(cmdToExecute, null, new File(commandToBeExecutedFrom));

            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                log.debug("Success!");
                log.info(output.toString());

                if (output.toString().contains("Error:"))
                    return FAIL;

                //System.exit(0);
                return SUCCESS;
            } else {
                StringBuilder testOutput = new StringBuilder();
                BufferedReader testReader = new BufferedReader(
                        new InputStreamReader(process.getErrorStream()));

                String testLine;
                while ((testLine = testReader.readLine()) != null) {
                    testOutput.append(testLine + "\n");
                }
                log.error("Issue in command execution");
                log.info(testOutput.toString());
                return FAIL;
                //abnormal...
            }
        } catch (IOException e) {
            e.printStackTrace();
            return FAIL;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return FAIL;
        }
    }

    private void createFile(String content){

        try{
            File file = new File(HOME_EC_2_USER_OPENHACK_LAMBDA+ LAMBDA_TERRAFORM_SH);

            //Create the file
            if (file.createNewFile())
            {
                System.out.println("File is created!");
            } else {
                System.out.println("File already exists.");
            }

    //Write Content
            file.setExecutable(true);
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}