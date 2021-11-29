package kukekyakya.kukemarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class KukemarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(KukemarketApplication.class, args);
	}

}
