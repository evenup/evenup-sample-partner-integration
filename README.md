This README describes how to use this sample application from EvenUp.

* **Overview**

This is a sample application to demonstrate to our partners how they might interface with our REST services.  It is not to be used in any way in production, but instead to show the flow of data to and from EvenUp.

Most of the application is written in Groovy, due to it's simple syntax, Java-compatibility and seamless use of JSON.  However, we can pretty much treat it as Java.


* **Building**

In order to build the project, you will need a Java 7 JDK (http://www.oracle.com/technetwork/java/javase/7u60-relnotes-2200106.html) and Maven 3.x (http://maven.apache.org/download.cgi).  Then issue the following command from the root directory (where this README file lives):

```
mvn clean install
```

After this, you should have three jar files in the target directory (where version is the version you have downloaded, e.g. "0.0.1-SNAPSHOT"):

1. evenup-sample-partner-integration-version.jar 
2. evenup-sample-partner-integration-version-sources.jar
3. evenup-sample-partner-integration-version-jar-with-dependencies.jar

* **Running**

To run the Client View, which allows you to log into EvenUp and send REST requests, as well as to start the REST Callback Server, issue the following command:

```
java -jar target/evenup-sample-partner-integration-version-jar-with-dependencies.jar
```

In many environments, you will be unable to successfully use the REST Callback Server from your desktop, as all ports are blocked by a firewall.  In this scenario, you can run a TCP Tunnel, such as ngrok (https://ngrok.com/) or localtunnel (http://localtunnel.me/).  These will produce a temporary URL, which can be set via the UI (Partner->Set REST Callback).  EvenUp's REST notification system will then send events here, and they will be ferried to the REST server running on your machine.  For example, after starting the REST server (Callback Server->Start) and choosing a post of 9999, we could start ngrok as follows:
```
ngrok 9999
```

In some cases you may wish to launch only the REST Callback Server and not integrate it with the client application.  In this case you would run the following:

```
java -cp target/evenup-sample-partner-integration-version-jar-with-dependencies.jar com.evenup.sample.rest.server.JettyRESTServer -p port-to-use (defaults to 9000)
```

