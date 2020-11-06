package net.pladema.sgx.images;

import net.pladema.sgx.files.StreamFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class ImageFileService {

    private static final Logger LOG = LoggerFactory.getLogger(ImageFileService.class);

    private static final String OUTPUT = "Output -> {}";

    private static final String FILE_EXTENSION = ".b64image";

    private static final Charset ENCODING = StandardCharsets.UTF_8;

    private final StreamFile streamFile;

    public ImageFileService(StreamFile streamFile){
        this.streamFile = streamFile;
    }

    public String buildPath(String fileRelativePath){
        LOG.debug("Input paramenter -> fileRelativePath {}", fileRelativePath);
        String path = streamFile.buildPath(fileRelativePath);
        LOG.debug(OUTPUT, path);
        return path;
    }

    public String createFileName(){
        String result = UUID.randomUUID().toString() + FILE_EXTENSION;
        LOG.debug(OUTPUT, result);
        return result;
    }

    public String readImage(String path){
        LOG.debug("Input parameter -> path {}", path);
        String result = null;
        try {
            result = streamFile.readFileAsString(path, ENCODING);
        } catch (IOException e) {
            LOG.error("Cannot read image file at {}", path);
            e.printStackTrace();
        }
        LOG.debug(OUTPUT, imageDataToString(result));
        return result;
    }

    public boolean saveImage(String path, String imageData) {
        LOG.debug("Input parameters -> path {}, imageData {}", path, imageDataToString(imageData));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.writeBytes(imageData.getBytes());
        try {
            boolean result = streamFile.saveFileInDirectory(path, os);
            LOG.debug(OUTPUT, result);
            return result;
        } catch (IOException e) {
            LOG.error("Cannot save image file at {}", path);
            e.printStackTrace();
            return false;
        }
    }

    private String imageDataToString(String imageData){
        return "(" +
                "exists imageData='" + (imageData != null) + '\'' +
                ')';
    }
}