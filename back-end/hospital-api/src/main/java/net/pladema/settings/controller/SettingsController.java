package net.pladema.settings.controller;

import io.swagger.annotations.Api;
import net.pladema.assets.controller.service.AssetsExternalService;
import net.pladema.settings.service.SettingsService;
import org.apache.http.MethodNotSupportedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/settings")
@Api(value = "Settings", tags = { "Settings" })
public class SettingsController {

    private static final Logger LOG = LoggerFactory.getLogger(SettingsController.class);

    private static String getFileName(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        return requestURL.split("/assets/")[1];
    }

    private final SettingsService settingsService;
    private final AssetsExternalService assetsExternalService;

    public SettingsController(SettingsService settingsService, AssetsExternalService assetsExternalService) {
        this.settingsService = settingsService;
        this.assetsExternalService = assetsExternalService;
    }

    @PostMapping(value = "/assets/{fileName:.+}/**", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('ROOT')")
    @Transactional
    public boolean uploadFile(HttpServletRequest request,
                              @RequestPart("file") MultipartFile file) throws MethodNotSupportedException {
        //TODO: en service crear paquete exception con un exception handler en el controller o en otro paquete dentro de la capa de controler. Parserar a apierrordto
        LOG.debug("Input parameters ->  {} fileName {}",
                request.getRequestURL().toString());

        return settingsService.uploadFile(this.assetsExternalService.findByName(getFileName(request)), file);
    }

    @DeleteMapping(value = "/assets/{fileName:.+}/**")
    @PreAuthorize("hasAnyAuthority('ROOT')")
    @Transactional
    public boolean deleteFile(HttpServletRequest request) throws MethodNotSupportedException {
        LOG.debug("Input parameters ->  {} fileName {}",
                request.getRequestURL().toString());

        return settingsService.deleteFile(this.assetsExternalService.findByName(getFileName(request)));
    }
}
