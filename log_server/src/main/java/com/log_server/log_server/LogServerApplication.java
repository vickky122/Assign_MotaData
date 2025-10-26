package com.log_server.log_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
public class LogServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogServerApplication.class, args);
	}

}
