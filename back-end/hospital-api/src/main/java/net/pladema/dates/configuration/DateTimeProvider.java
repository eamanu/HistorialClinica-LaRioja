package net.pladema.dates.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Clase creada para poder mockear en los test unitarios.
 */
@Component
public class DateTimeProvider {

    private static final Logger LOG = LoggerFactory.getLogger(DateTimeProvider.class);

    public static final String OUTPUT = "Output -> {}";

    public LocalDateTime nowDateTime(){
        LocalDateTime result = LocalDateTime.now();
        LOG.debug(OUTPUT, result);
        return result;
    }

    public LocalDate nowDate(){
        LocalDate result = LocalDate.now();
        LOG.debug(OUTPUT, result);
        return result;
    }
}