package com.compomics.tessa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class VisualisationtoolApplication {

	public static void main(String[] args) {

		SpringApplication.run(VisualisationtoolApplication.class, args);
	}
}
