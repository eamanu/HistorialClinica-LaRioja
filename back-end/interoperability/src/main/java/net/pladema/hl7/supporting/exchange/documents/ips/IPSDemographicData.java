package net.pladema.hl7.supporting.exchange.documents.ips;

import net.pladema.hl7.concept.administration.DeviceResource;
import net.pladema.hl7.concept.administration.OrganizationResource;
import net.pladema.hl7.supporting.conformance.InteroperabilityCondition;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ResourceType;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
@Conditional(InteroperabilityCondition.class)
public class IPSDemographicData {

    private final Map<ResourceType, Reference> references = new EnumMap<>(ResourceType.class);

    private final DeviceResource deviceResource;
    private final OrganizationResource organizationResource;

    public IPSDemographicData(DeviceResource deviceResource,
                              OrganizationResource organizationResource){
        this.deviceResource = deviceResource;
        this.organizationResource = organizationResource;
    }

    public List<Bundle.BundleEntryComponent> fetchEntries(String patientId){
        List<Bundle.BundleEntryComponent> entries = new ArrayList<>();

        //================Organization resource================
        Bundle.BundleEntryComponent entryOrganization = organizationResource
                .fetchEntry(patientId, new EnumMap<>(ResourceType.class));
        references.put(ResourceType.Organization, new Reference(entryOrganization.getFullUrl()));

        Reference locationRef = new Reference();
        locationRef.setId(entryOrganization.getResource().getId());
        references.put(ResourceType.Location, locationRef);

        entries.add(entryOrganization);


        //===================Device resource===================
        Bundle.BundleEntryComponent entryDevice = deviceResource
                .fetchEntry(entryOrganization.getResource().getId(), new EnumMap<>(ResourceType.class));
        references.put(ResourceType.Device, new Reference(entryDevice.getFullUrl()));
        entries.add(entryDevice);

        return entries;
    }

    public Map<ResourceType, Reference> getReferences(){
        return references;
    }
}
