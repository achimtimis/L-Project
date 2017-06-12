package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "app.client")
@EnableEurekaClient
public class QuizMainServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizMainServiceApplication.class, args);
	}
}
