<a href="http://predixdev.github.io/rabbitmq-producer-template/javadocs/index.html" target="_blank" >
	<img height="50px" width="100px" src="images/javadoc.png" alt="view javadoc"></a>
&nbsp;
<a href="http://predixdev.github.io/rabbitmq-producer-template" target="_blank">
	<img height="50px" width="100px" src="images/pages.jpg" alt="view github pages">
</a>
RabbitMQ Producer Template
=====================
##Follow the tutorial 
[Microservice Templates: RabbitMQ Producer SDK](https://predix.io/resources/tutorials/tutorial-details.html?tutorial_id=1779&tag=1609&journey=RabbitMQ&resources=1552,1779,1554) 


##Using this project
This project is a utility project intended to be included as a dependency.  To add as a dependency, add this to your maven pom.xml

	<dependency>
		<groupId>com.ge.predix.solsvc</groupId>
		<artifactId>rabbitmq-producer-template</artifactId>
		<version>${rabbitmq-producer-version-here}</version>
	</dependency>

##How to define properties for this project

In your microservice project that depends on this utility.

	- RabbitMQ Binding
		- In the manifest.yml of your microservice add this:
			- services:
      				- <my-rabbitmq-instance-name> 
	- For Unit Tests
		- Note that integration tests cannot call RabbitMQ Server in the cloud.  This is because the cloud foundry router only accepts connections over port 80.
		- Follow the pattern in src/test/resources/application.properties


##How to build this project
The test cases assume that there is a Rabbit MQ Server installed on localhost.

	1) mvn clean install

	2) Start rabbitmqserver

	    sudo rabbitmq-server or rabbitmq-server

	3) To run integration tests with default RabbitMQ Host:localhost, Port:5672, Queue:SampleMainQ, ErrorQueue:SampleErrorQueue

		mvn clean install

	4) To run integration tests with non-default RabbitMQ host and port

		mvn clean install -Dserver=<rabbitmqhost> -Dport=<rabbitmq port>

	5) To run integration tests with non-default RabbitMQ host and port and non-default Queue names 

		mvn clean install -Dserver=<rabbitmqhost> -Dport=<rabbitmq port> -Dmainq=<queue name> -Derrorq=<error queue name>

[![Analytics](https://ga-beacon.appspot.com/UA-82773213-1/rabbitmq-producer-template/readme?pixel)](https://github.com/PredixDev)
