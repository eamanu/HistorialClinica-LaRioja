package net.pladema.assets.service.impl;

import net.pladema.assets.service.AssetsService;
import net.pladema.assets.service.domain.AssetsFileBo;
import net.pladema.assets.service.domain.Assets;
import net.pladema.sgx.files.FileService;
import net.pladema.sgx.files.StreamFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AssetsServiceImpl implements AssetsService {

    private static final String ORIGINAL_PATH = "/assets/webapp/";
    private static final String CUSTOM_PATH = "/assets/custom/";
    private static final Logger LOG = LoggerFactory.getLogger(AssetsServiceImpl.class);

    private static final Assets SPONSOR_LOGO = new Assets("image/png", "sponsor-logo-512x128.png");
    private static final Assets FAVICON = new Assets("image/x-icon", "favicon.ico");
    private static final Assets ICON_72 = new Assets("image/png", "icons/icon-72x72.png");
    private static final Assets ICON_96 = new Assets("image/png", "icons/icon-96x96.png");
    private static final Assets ICON_128 = new Assets("image/png", "icons/icon-128x128.png");
    private static final Assets ICON_144 = new Assets("image/png", "icons/icon-144x144.png");
    private static final Assets ICON_152 = new Assets("image/png", "icons/icon-152x152.png");
    private static final Assets ICON_192 = new Assets("image/png", "icons/icon-192x192.png");
    private static final Assets ICON_384 = new Assets("image/png", "icons/icon-384x384.png");
    private static final Assets ICON_512 = new Assets("image/png", "icons/icon-512x512.png");

    private final FileService fileService;
    private final StreamFile streamFile;

    List<Assets> assetsList = new ArrayList<>(Arrays.asList(SPONSOR_LOGO, FAVICON, ICON_72, ICON_96, ICON_128, ICON_144, ICON_152, ICON_192, ICON_384, ICON_512));

    public AssetsServiceImpl(FileService fileService, StreamFile streamFile) {
        this.fileService = fileService;
        this.streamFile = streamFile;
    }

    @Override
    public Optional<Assets> findByName(String name) {
        LOG.debug("Input parameters ->  {} fileName {}", name);
        return this.assetsList.stream().filter(a -> a.getNameFile().equals(name)).findAny();
    }

    @Override
    public AssetsFileBo getFile(String fileName) {
        LOG.debug("Input parameters ->  {} fileName {}", fileName);

        Assets newAsset = this.findByName(fileName).get();
        String partialPath = CUSTOM_PATH.concat(newAsset.getNameFile());
        String completePath = fileService.buildRelativePath(partialPath);
        
        if (this.streamFile.existFile(completePath)) {
            return new AssetsFileBo(
                    this.fileService.loadFile(partialPath),
                    newAsset.getContentType());
        }

        String newPartialPath = ORIGINAL_PATH.concat(newAsset.getNameFile());
        return new AssetsFileBo(
                new ClassPathResource(newPartialPath),
                newAsset.getContentType());
    }
}
