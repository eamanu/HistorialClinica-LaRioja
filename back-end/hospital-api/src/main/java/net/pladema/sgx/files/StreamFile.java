package net.pladema.sgx.files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class StreamFile {

    private static final Logger LOG = LoggerFactory.getLogger(StreamFile.class);

    @Value("${internment.document.directory:temp}")
    private String rootDirectory;

    public StreamFile(){
        super();
    }

    public String buildPath(String relativeFilePath) {
        return getRootDirectory() + relativeFilePath;
    }

    public boolean saveFileInDirectory(String path, ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        boolean fileCreated;
        boolean directoryCreated = true;

        File file = new File(path);
        if(!file.getParentFile().exists())
            directoryCreated = file.getParentFile().mkdirs();

        if(directoryCreated && !file.exists()) {
            fileCreated = file.createNewFile();

            try(OutputStream outputStream = new FileOutputStream(file)){
                outputStream.write(byteArrayOutputStream.toByteArray());
            }
            LOG.debug("File loaded in directory -> {}", fileCreated);
            return fileCreated;
        }
        return false;
    }

    public String readFileAsString(String path, Charset charset) throws IOException{
        LOG.debug("Input parameter -> path {}", path);
        String result = null;
        ByteArrayInputStream is = reader(path);
        int numberOfBytes = is.available();
        byte[] bytes = new byte[numberOfBytes];
        is.read(bytes, 0, numberOfBytes);
        result = new String(bytes, charset);
        LOG.debug("Output -> {}", result);
        return result;
    }

    private ByteArrayInputStream reader(String path) throws IOException{
        LOG.debug("Input parameters -> path {}", path);
        Path filePath = Paths.get(path);
        byte[] data = Files.readAllBytes(filePath);
        LOG.debug("Output -> data {}", data);
        return new ByteArrayInputStream(data);
    }

    private String getRootDirectory() {
        if (rootDirectory == null || rootDirectory.equals("temp"))
            rootDirectory = System.getProperty("java.io.tmpdir");

        return Paths.get(rootDirectory).toString();
    }

}
