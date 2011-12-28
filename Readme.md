This guide explains how to prepare your computer to use Java BlueVia SDK to develop applications with access to Telefonica’s network services.

## Getting started ##

This guide explains how to prepare your computer to use Java BlueVia SDK to develop applications with access to Telefonica’s network services. First check out the system requirements that your computer must meet, and then follow the installation steps.

## System requirements ##

Minimun system requeriments:
[http://www.oracle.com/technetwork/java/javase/downloads/index.html](Java 1.6)

Recommended system requeriments:

- [http://www.eclipse.org/](Eclipse IDE)
- [http://maven.apache.org/](Maven 2)
- [http://maven.apache.org/eclipse-plugin.html](Maven plugin for Eclipse)

It is not strictly necessary to use Eclipse and Maven as your development environment, but it is recommended to manage dependencies in your projects.

## Using Bluevia Java SDK in your applications ##

Depending on whether or not you are using Maven, you will have to add library dependencies to the project classpath or just edit your pom.xml to enable Maven to manage them:

### Eclipse standalone project ###


You have to include the library in your Eclipse project and configure the dependencies:

1. Download Bluevia Library and save it in your hard disk: Bluevia Java SDK
2. Create your Project in Eclipse: select File > New > Java Project.
3. Include the Bluevia Library into the Java build path:
    - As JAR file:
        - Select Project > Properties.
        - In Java Build Path section, click on Libraries tab.
    - As source code:
        - Select Project > Properties.
        - In Java Build Path section, click on Source tab.
        - Click on Add folder and select the path of the source.
4. Finally, include the JAR dependencies of Bluevia SDK in Libraries tab clicking on Add External JARs (The JAR files are included in "dependency" folder). 

### Maven project ###

1. Download Bluevia Library and save it in your hard disk: Bluevia Java SDK
2. Create your Project in Eclipse: select File > New > Maven Project.
3. Include the Bluevia Library into the Java build path:
    - As JAR file:
        - Select Project > Properties.
        - In Java Build Path section, click on Libraries tab.
    - As source code:
        - Select Project > Properties.  
        - In Java Build Path section, click on Source tab.
        - Click on Add folder and select the path of the source.
4. Finally, edit the pom.xml file adding the following lines to include the library dependencies:

&nbsp;

        <dependencies>
            ..............
            <dependency>    
                    <groupId>org.apache.httpcomponents</groupId>
                            <artifactId>httpclient</artifactId>
                            <version>4.0.1</version>
                    </dependency>
                    <dependency>    
                            <groupId>org.apache.httpcomponents</groupId>
                            <artifactId>httpcore</artifactId>
                            <version>4.0.1</version>
                    </dependency>
                    <dependency>
                        <groupId>commons-codec</groupId>
                        <artifactId>commons-codec</artifactId>
                        <version>1.3</version>
                    </dependency>
                    <dependency>
                            <groupId>oauth.signpost</groupId>
                            <artifactId>signpost-core</artifactId>
                            <version>1.2.1.1</version>
                    </dependency>
                    <dependency>
                            <groupId>oauth.signpost</groupId>
                            <artifactId>signpost-commonshttp4</artifactId>
                            <version>1.2.1.1</version>
                    </dependency>
                    <dependency>
                            <groupId>oauth.signpost</groupId>
                            <artifactId>signpost-jetty6</artifactId>
                            <version>1.2.1.1</version>
                    </dependency>
                    <dependency>
                            <groupId>commons-logging</groupId>
                            <artifactId>commons-logging</artifactId>
                            <version>1.1</version>
                    </dependency>
                    <dependency>
                            <groupId>javax.mail</groupId>
                            <artifactId>mail</artifactId>
                            <version>1.4</version>
                    </dependency>
        ..............
        </dependencies>


## API Authentication ##

BlueVia requires both the user and your application to be identified and authenticated to allow applications use its APIs. BlueVia supports OAuth Protocol, a token-passing mechanims where users never have to reveal their passwords or credentials to the application. Bluevia library implements an OAuth client to supply developers an easy access to authentication procedure: see the `Oauth reference` as part of the library documentation 

For more information visit the [https://bluevia.com/en/knowledge/getStarted.Authentication](API authentication)  section of BlueVia reference.

## API examples ##

The following guides explain the behavior of each Bluevia API, including code samples to start developing a simple application in an easy way:

- SMS API
- MMS API
- Directory API
- Advertising API
- Payment API

They can be found in the library documentation.
