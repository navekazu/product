package tools.mailer.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import tools.mailer.entity.Account;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public abstract class SerializerBase<T> {
    protected static boolean encrypt = true;
    protected static boolean testMode = false;

    static void setTestMode() {
        encrypt = false;
        testMode = true;
    }

    protected abstract String persistenceName();
    protected abstract TypeReference getJsonTypeReference();

    List<T> readAll() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Path path = getPersistenceFile();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (InputStream in = Files.newInputStream(path, StandardOpenOption.READ)) {
            int data;
            while ((data=in.read())!=-1) {
                bos.write(data);
            }
        }

        return mapper.readValue(toDecrypt(bos.toByteArray()), getJsonTypeReference());
    }

    void writeAll(List<T> list) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        byte[] jsonData = mapper.writeValueAsBytes(list);
        Path path = getPersistenceFile();
        try (OutputStream out = Files.newOutputStream(path
                , StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)){
            out.write(toEncrypt(jsonData));
        }
    }

    byte[] toEncrypt(byte[] data) throws UnsupportedEncodingException {
        if (!encrypt) {
            return data;
        }
        return data;
    }
    byte[] toDecrypt(byte[] data) throws UnsupportedEncodingException {
        if (!encrypt) {
            return data;
        }
         return data;
    }

    private Path getAppPath() {
        return Paths.get(System.getProperty("user.home"), ".mailer"+(testMode? "_test": ""));
    }

    Path getPersistenceFile() throws IOException {
        // "~/.mailer/"
        Path appDir = getAppPath();
        if (!Files.exists(appDir)) {
            Files.createDirectory(appDir);
        }

        // "~/.mailer/xxx.json"
        return Paths.get(appDir.toString(), persistenceName()+".json");
    }

    Path getPersistenceDirectory() throws IOException {
        // "~/.mailer/"
        Path appDir = getAppPath();

        // "~/.mailer/xxx/"
        Path path = Paths.get(appDir.toString(), persistenceName());

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        return path;
    }
}
