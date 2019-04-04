package info.studyup.studyupserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SaltServerApplication {

	public static void main(String[] args) throws Exception {
	    ReladomoInitializer.init(args.length>=1? args[0]: null);
		SpringApplication.run(SaltServerApplication.class, args);
	}

}

