package tools.directorymirroringtool.process;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class MirroringProcessSerializer {
    private static final String CONFIG_FILE_NAME = ".directoryMirroringTool.json";

    void readMirroringProcess() throws IOException {

    }

    void writeMirroringProcess(List<MirroringProcess> list) throws IOException {
        // test
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        byte[] jsonData = new byte[0];
        try {
            jsonData = mapper.writeValueAsBytes(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Path path = Paths.get(System.getProperty("user.home"), CONFIG_FILE_NAME);
        try {
            try (OutputStream out = Files.newOutputStream(path
                    , StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)){
                out.write(jsonData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
