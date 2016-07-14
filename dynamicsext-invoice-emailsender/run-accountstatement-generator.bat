@echo off

IF "%1" == "" GOTO No1
IF "%2" == "" GOTO No2

java -jar -Dprofile=accountstatementgenerator -Dcustomer.id="%1" -Ddate.to="%2" ./libs/invoice-emailsender.jar --spring.config.location="file:./configs/application.properties"
GOTO End1


:No1
echo WARNING: No 'Customer ID' passed for processing...
GOTO End1

:No2
echo WARNING: No 'Closing Date' passed for processing...
GOTO End1

:End1