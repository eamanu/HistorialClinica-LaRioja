package net.pladema.internation.service.impl;

import net.pladema.internation.controller.dto.SnomedDto;
import net.pladema.internation.repository.masterdata.SnomedRepository;
import net.pladema.internation.repository.masterdata.entity.Snomed;
import net.pladema.internation.service.SnomedService;
import net.pladema.internation.service.documents.impl.DocumentServiceImpl;
import net.pladema.patient.service.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SnomedServiceImpl implements SnomedService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final SnomedRepository snomedRepository;

    public SnomedServiceImpl(SnomedRepository snomedRepository){
        this.snomedRepository = snomedRepository;
    }

    @Override
    public String createSnomedTerm(SnomedDto snomedTerm){
        if(!StringHelper.isNullOrWhiteSpace(snomedTerm.getId()) && !StringHelper.isNullOrWhiteSpace(snomedTerm.getFsn())){
            Snomed snomed = new Snomed(
                    snomedTerm.getId(),
                    snomedTerm.getFsn(),
                    snomedTerm.getId(),
                    snomedTerm.getFsn());
            snomed = snomedRepository.save(snomed);
            return snomed.getId();
        }
        return null;
    }

}
