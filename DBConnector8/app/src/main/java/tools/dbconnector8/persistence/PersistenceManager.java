package tools.dbconnector8.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

public class PersistenceManager {
	private Config config;

	private String CONFIG_FILE_NAME = "config.json";
	private String PASSWORD_FILE_NAME = "password";

	public Config getConfig() throws IOException {
		if (!Objects.isNull(config)) {
			return config;
		}
		
		Path path = getConfigFile();

		ObjectMapper  mapper = new ObjectMapper();
		
		try {
			config = mapper.readValue(path.toFile(), Config.class);

		} catch(MismatchedInputException e) {
			config = new Config();
		}
        
        return config;
	}

	public void writeConfig() throws IOException {
		if (Objects.isNull(config)) {
			return ;
		}

		Path path = getConfigFile();

		ObjectMapper  mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(path.toFile(), config);
	}

	private Path baseDirectory() {
		return Paths.get(System.getProperty("user.home"), ".dbc8");
	}
	
	private Path getConfigFile() throws IOException {
		return getAppFile(CONFIG_FILE_NAME);
	}

	private Path getPasswordFile() throws IOException {
		return getAppFile(PASSWORD_FILE_NAME);
	}
	
	private Path getAppFile(String file) throws IOException {
		Path appDir = baseDirectory();
		Files.createDirectories(appDir);
		
		Path appFile = Paths.get(appDir.toString(), file);

		if (!Files.exists(appFile)) {
			Files.createFile(appFile);
		}
		
		return appFile;
	}

	public boolean existsBootPassword() throws IOException {
		Path path = getPasswordFile();
		String hashValue = Files.readString(path);
		
		return hashValue.length()!=0;
	}
	
	public boolean checkBootPassword(String password) throws IOException {
		Path path = getPasswordFile();
		String hashValue = Files.readString(path);

		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		
		return bcrypt.matches(password, hashValue);
	}
	
	public void updateBootPassword(String password) throws IOException {
		Path path = getPasswordFile();
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		String hashValue = bcrypt.encode(password);
		Files.writeString(path, hashValue);
	}
	
	public void foo() {
		System.out.println("foo");
	}
}
