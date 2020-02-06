## API's to automate AWS Infra 
Its contains Open Hack solution to automate AWS infra Setup and objective is to create a re-usable framework that can provide notifications to various collaborator tools where users subscribe. 

## This document describes step by step instruction to install and setup the these API's.

### Pre-Reqs

### Install following software on your machine

* JDK 1.8 or above
* Terraform, find below commands to install terraform on Linux machine
* Command to install package --> sudo wget https://releases.hashicorp.com/terraform/0.12.2/terraform_0.12.2_linux_amd64.zip
* Command to unzip and link it to bin --> sudo unzip ./ terraform_0.12.2_linux_amd64.zip -d /usr/local/bin
* Command to verify terraform installation --> terraform -v


Once the above software's are installed, follow below steps to deploy API.

### Steps to install and deploy API
* Clone this Repo, go to teameqs-api directory and update file src/main/resources/application.yml file with AWS accesskey and secretKey.
* From root of teameqs-api execute command mvn clean install -DskipTests=true.
* Go to target directory and copy jar file to Linux machine where you have installed all pre-reqs s/w's.
* Create a directory under user home with name .openhack for-example ~/.openhack and copy code pipeline and notification directory from Repo to .openhack directory
* Execute command java -jar openhack-app-**.jar </dev/null &>/dev/null &
* Go to URL http://DOMAIN_NAME:8090/eqs-team/swagger-ui.html and you will see two API.
* Try out both the API's with all required parameter and see AWS infra getting created automatically for you.
