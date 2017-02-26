@echo off

IF "%1" == "" GOTO No1

java -jar -Dprofile=invoicegenerator -Dtransaction.numbers="%1" ./libs/invoice-emailsender.jar --spring.config.location="file:./configs/application.properties"
GOTO End1


:No1
echo WARNING: No Payment Receipt ID passed for processing...
GOTO End1

:End1