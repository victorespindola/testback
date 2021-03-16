package io.pismo.testback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class TestbackApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestbackApplication.class, args);
	}

}
