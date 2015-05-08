package kamon.annotation.examples;

import kamon.Kamon;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationLauncher {

  public static void main(String[] args) {
    Kamon.start();
    SpringApplication.run(ApplicationLauncher.class, args);
  }
}
