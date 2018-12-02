@echo off 

echo ---Deploying site 


copy d:\home\site\repository\AppDB\target\*.war %DEPLOYMENT_TARGET%\webapps\*.war
