#!/bin/sh
#Below variables will remain same for all other Job
# This script checks the quality gates of Sonar Scan and validation is done via REST API sonar.
# We can pass Project key as a parameter of this script, even if we don't pass project key, script will automatically fetch #details(project key) from .sonar directory.

#NOTE :: If any team is using Sonar other than Central  EDN Sonar, please export url from script task and override Bamboo global varibale bamboo_sonarqube_auth_token with plan variable
export sonar_token="$bamboo_sonarqube_auth_token"

file="sonarqube-project-status.json"
## Checking Analysis status
export sonar_scan_log="report-task.txt"
export sonar_ce_task_file="sonar_ce_task_file.json"
export count=0;

if [ -z $user_sonar_workspace ];then
	export sonar_workspace="$bamboo_working_directory/.sonar"
	echo "Default Sonar Workspace :$sonar_workspace"
else
	export sonar_workspace="$user_sonar_workspace/.sonar"
	echo "User defined Sonar Workspace:$sonar_workspace"
fi 

if [ -z $user_sonar_analysis_counter ];then
	export sonar_analysis_counter=18;
	echo "Default Sonar analysis counter:$sonar_analysis_counter"
else 
	export sonar_analysis_counter=$user_sonar_analysis_counter;
	echo "User defined Sonar analysis counter:$sonar_analysis_counter"
fi

if [ -f $sonar_workspace/$sonar_scan_log ];then
	while IFS='=' read -r key value
		do
			eval "${key}='${value}'"
	done < "$sonar_workspace/$sonar_scan_log"
else
	echo "Sonar Workspace does not contain report-task.txt,<Project-work-space/.sonar>"
fi

echo "Sonar Analysis URL: $ceTaskUrl"
if [ ! -z $ceTaskUrl ];then
	while true; do
	rm -rf $sonar_ce_task_file
	curl -u $sonar_token: -o $sonar_ce_task_file -X GET $ceTaskUrl
		export sonar_analysis_status=`jq '.task.status' $sonar_ce_task_file`
		if [ $sonar_analysis_status == "\"SUCCESS\"" ]; then
			echo "good to go for quality check"
			break;
		fi
			echo "Waiting for completion of sonar analysis..."
			export count=$(($count+1));
			sleep 10;
		if [ $count -gt $sonar_analysis_counter ]; then
			echo "Sonar Analysis is still in progress..default value set for waiting is 180 seconds..";
			echo "If you want to increase the waiting period then set below variables in your bamboo task";
			echo "export user_sonar_analysis_counter=18";
			echo "Note here that each counter is equal to 10 seconds";
			exit 1;
		fi
	done
else
	echo "Once report submitted to sonar server..Sonar performs the analysis in background."
	echo "we skipped analysis status check as Sonar ceTask id is not available.In this case you might get older quality status"
	echo "Please check if report-task.txt is available in your project .sonar directory<Project-work-space/.sonar>"
fi 
## end of sonar analysis check 

if [ -z $url ]
then
    export url="http://localhost:9000"
fi
echo "SonarQube Server Url:$url"
if [ -z $1 ];then
projKey=$projectKey;
echo "Default Project Key :$projKey"
else
projKey=$1
echo "User defined Project Key :$projKey"
fi
if [ -f $file ] ; then
	rm -rf $file
fi
export url=$(echo "$url" |sed 's:/*$::')
echo "URL post trim:$url"
reportUrl=$url/api/qualitygates/project_status?projectKey=$projKey
echo "Parsing report from ${reportUrl}"
curl -k -u $sonar_token: -X GET $reportUrl > $file
if ! grep -q 'msg\|not found\|projectKey' $file;then
	var=`cat $file | cut -d: -f3 | cut -d, -f1`
	if [ $var = "\"ERROR\"" ]; then
		echo "Task failed because sonarqube quality status is FAILED"
		echo "The report: "
		cat $file
		exit 1;
	else
		if [ $var = "\"OK\"" ]; then
			echo "Successful status on sonar server";
			cat $file;
		else
			echo "File- $file does not contain OK or Error as status. Seems something wrong with Rest API."
			cat $file;
			exit 1;
		fi
	fi
else
	echo "Sonar Project Key is not valid";
	exit 1;
fi
