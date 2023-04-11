package tools.dbconnector7.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

public class PersistenceManager {
	private Config config;

	private String CONFIG_FILE_NAME = "config.json";

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
		return Paths.get(System.getProperty("user.home"), ".dbc7");
	}
	
	private Path getConfigFile() throws IOException {
		Path appDir = baseDirectory();
		Files.createDirectories(appDir);
		
		Path configFile = Paths.get(appDir.toString(), CONFIG_FILE_NAME);

		if (!Files.exists(configFile)) {
			Files.createFile(configFile);
		}
		
		return configFile;
	}
}
