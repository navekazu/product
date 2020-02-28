@echo off

set CP=jar\animal-sniffer-annotations-1.14.jar
set CP=%CP%;jar\bcpg-jdk15on-1.64.jar
set CP=%CP%;jar\bcpkix-jdk15on-1.64.jar
set CP=%CP%;jar\bcprov-jdk15on-1.64.jar
set CP=%CP%;jar\commons-codec-1.5.jar
set CP=%CP%;jar\error_prone_annotations-2.0.18.jar
set CP=%CP%;jar\guava-23.0.jar
set CP=%CP%;jar\j2objc-annotations-1.1.jar
set CP=%CP%;jar\JavaEWAH-1.1.6.jar
set CP=%CP%;jar\jsch-0.1.55.jar
set CP=%CP%;jar\jsr305-1.3.9.jar
set CP=%CP%;jar\jzlib-1.1.1.jar
set CP=%CP%;jar\log4j-1.2.17.jar
set CP=%CP%;jar\org.eclipse.jgit-5.6.0.201912101111-r.jar
set CP=%CP%;jar\slf4j-api-1.7.30.jar
set CP=%CP%;jar\slf4j-log4j12-1.7.30.jar

set JAR=build\libs\GitClient.jar


java -classpath %CP% -jar %JAR%

@echo on
