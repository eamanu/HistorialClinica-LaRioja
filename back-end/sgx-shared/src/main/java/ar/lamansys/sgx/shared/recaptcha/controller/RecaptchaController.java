package ar.lamansys.sgx.shared.recaptcha.controller;

import io.swagger.annotations.Api;
import ar.lamansys.sgx.shared.recaptcha.service.ICaptchaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/recaptcha")
@Api(value = "Recaptcha", tags = { "Recaptcha" })
public class RecaptchaController {

    private static final Logger LOG = LoggerFactory.getLogger(RecaptchaController.class);

    public static final String OUTPUT = "Output -> {}";

    private final ICaptchaService captchaService;

    public RecaptchaController(ICaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @GetMapping(value = "/is-enable")
    public ResponseEntity<Boolean> isRecaptchaEnable() {
        LOG.debug("{}", "Check for recaptcha enable");
        boolean result = captchaService.isRecaptchaEnable();
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }
}
