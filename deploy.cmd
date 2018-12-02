@echo off 

echo ---Deploying site 


copy d:\home\site\repository\azureapp\target\*.war %DEPLOYMENT_TARGET%\webapps\*.war
