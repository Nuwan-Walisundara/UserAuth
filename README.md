# UserAuth
UserAuthentication capp &amp; Spring boot application

# Springboot Application

#How to deploy

Install JRE in PC
Download user-1.0.jar 
Run with below command (As a background service)
nohup java -jar user-1.0.jar &

#How to tail logs

Run the below command
tail -f nohup.out

# WSO2ESB Carbon Application

#How to deploy

Download wso2esb-5.0.0 from wso2 official site
Download loginapihub_capp_1.0.0-SNAPSHOT.car carbon application
Extract in PC 
Go to /wso2carbon/wso2esb-5.0.0/bin/ 
Execute sh file to start wso2esb server
sh wso2server.sh start
When wso2esb server started, login UI through admin/admin username & password pair.
		https://localhost:9443/carbon
Go to Carbon Applications -> Add -> Choose file -> Upload

#How to tail logs

Navigate to below directory & run the below command
cd /wso2esb-5.0.0/repository/logs
 tailf wso2carbon.log

