curl -k -u eaa803d9e6ff7b1e06f410a52c7a05c3fa40cb9f: -X GET http://localhost:9000/api/qualitygates/project_status?projectKey=org.springframework:EQS-training-sample-app > sonar-quality.json
for /F "tokens=1 USEBACKQ" %%x in (      
        `json-parser.bat sonar-quality.json projectStatus.status`
       ) do (
            set OUTPUT=%%x
       )
IF %OUTPUT%=="ERROR" (
	echo "Sonar QUALITY GATE Fails"
	exit /b 666
	)
echo "Sonar QUALITY GATE PASS"