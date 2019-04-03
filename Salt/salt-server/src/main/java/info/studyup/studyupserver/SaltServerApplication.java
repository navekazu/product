package info.studyup.studyupserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SaltServerApplication {

	public static void main(String[] args) throws Exception {
	    ReladomoInitializer.init();
		SpringApplication.run(SaltServerApplication.class, args);
	}

}

