package tools.dbconnector8.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

public class PersistenceManager {
	private static PersistenceManager persistenceManager = new PersistenceManager();
	private Config config;

	private String CONFIG_FILE_NAME = "config.json";
	private String PASSWORD_FILE_NAME = "password";
	private String PERSISTENCE_QUERY_FILE_NAME = "query_%s.sql";

	private PersistenceManager() {
	}
	
	public static PersistenceManager getPersistenceManager() {
		return persistenceManager;
	}
	
	public synchronized Config getConfig() throws IOException {
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

	public synchronized void writeConfig() throws IOException {
		if (Objects.isNull(config)) {
			return ;
		}

		Path path = getConfigFile();

		ObjectMapper  mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(path.toFile(), config);
	}

	public String getPersistenceQuery() throws IOException {

		Path path = getPersistenceQueryFile();

		return Files.readString(path);
	}
	
	public void writePersistenceQuery(String contents) throws IOException {

		Path path = getPersistenceQueryFile();

		Files.writeString(path, contents);
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
	
	private Path getPersistenceQueryFile() throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        String formattedDate = LocalDate.now().format(formatter);
        String fileName = String.format(PERSISTENCE_QUERY_FILE_NAME, formattedDate);
		return getAppFile(fileName);
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
