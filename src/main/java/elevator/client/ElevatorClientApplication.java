package elevator.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ElevatorClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElevatorClientApplication.class, args);
	}
}
