package com.example.windows_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class WindowsClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(WindowsClientApplication.class, args);
	}

}
