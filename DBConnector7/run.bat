@rem set JAVA_HOME=C:\Program Files\Java\jdk-9
@rem set JAVA_HOME=C:\Program Files\Java\jdk-11.0.1
set JAVA_HOME=C:\Program Files\Java\jdk-11.0.1-oracle
set PATH="%JAVA_HOME%\bin";%PATH%
java -classpath build\libs\DBConnector7.jar tools.dbconnector7.App
exit
