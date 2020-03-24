##Required Java version jdk1.7.0_80 or above

java -classpath ./libs/*;./configs/; -Djh.config.log4j="./configs/log4j.properties" -Djh.config.springcontext="./configs/jh-main-context.xml"  -Dspring.profiles.active=jamaica,website_sync com.jh.bootstrap.JhpluginGenericBootstrap webProductsSync downloadParseAndSyncAllFiles