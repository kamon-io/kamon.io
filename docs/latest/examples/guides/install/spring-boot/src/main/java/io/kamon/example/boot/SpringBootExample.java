package io.kamon.example.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// tag:kamon-init:start
import kamon.Kamon;

@SpringBootApplication
public class SpringBootExample {

	public static void main(String[] args) {
		Kamon.init();
		SpringApplication.run(SpringBootExample.class, args);
	}
}

// tag:kamon-init:end
