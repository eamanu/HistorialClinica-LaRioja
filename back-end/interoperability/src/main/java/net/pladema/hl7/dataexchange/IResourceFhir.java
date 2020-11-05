package net.pladema.hl7.dataexchange;

import net.pladema.hl7.dataexchange.model.adaptor.FhirAddress;
import net.pladema.hl7.dataexchange.model.adaptor.FhirCode;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

@Component
public abstract class IResourceFhir {

    private static String dominio;
    private static String systemName;

    public IResourceFhir(){
        super();
    }

    @Value("${ws.renaper.dominio}")
    public void setDominio(String dominio){
        IResourceFhir.dominio = dominio;
    }

    @Value("${system.name:Historia de Salud Integrada}")
    public void setSystemName(String systemName){
        IResourceFhir.systemName = systemName;
    }

    public static String getDominio(){
        return IResourceFhir.dominio;
    }

    public static String getSystemName(){
        return IResourceFhir.systemName;
    }

    public abstract ResourceType getResourceType();

    public static <R extends IBaseResource> Bundle.BundleEntryComponent fetchEntry(R resource) {
        return new Bundle.BundleEntryComponent()
                .setFullUrl(fullUrl(resource))
                .setResource((Resource) resource);
    }

    public static <R extends IBaseResource> String fullUrl(R resource){
        String url = url(resource);
        String id = resource.getIdElement().getValue();
        if(id != null)
            return url.concat("/").concat(id);
        return url;
    }

    public static Address newAddress(FhirAddress fhirAddress){
        return new Address().setUse(Address.AddressUse.WORK)
                .addLine(fhirAddress.getAddress())
                .setCity(fhirAddress.getCity())
                .setCountry(fhirAddress.getCountry())
                .setState(fhirAddress.getProvince())
                .setPostalCode(fhirAddress.getPostcode());
    }

    public static CodeableConcept newCodeableConcept(String system, FhirCode code) {
        return newCodeableConcept(new Coding()
                .setSystem(system)
                .setCode(code.getTheCode())
                .setDisplay(code.getTheDisplay())
        );
    }

    public static CodeableConcept newCodeableConcept(Coding coding){
        return new CodeableConcept().addCoding(coding);
    }

    public static Extension newExtension(String profile, FhirCode value){
        return new Extension().setUrl(profile).setValue(new CodeType(value.getTheCode()));
    }
    public static Meta newMeta(String... profiles) {
        Meta meta = new Meta();
        Arrays.stream(profiles).forEach(meta::addProfile);
        return meta;
    }

    public static <R extends IBaseResource> Identifier newIdentifier(R resource){
        return newIdentifier(resource, resource.getIdElement().getValue());
    }

    public static <R extends IBaseResource> Identifier newIdentifier(R resource, String value){
        return newIdentifier(url(resource), value);
    }

    public static Identifier newIdentifier(String system, String value){
        return new Identifier().setSystem(system).setValue(value);
    }

    public static Reference newReference(String value){
        return new Reference(value);
    }

    public static Reference newReference(String system, String value){
        if(value != null) {
            String ref = system.concat("/").concat(value);
            return newReference(ref);
        }
        return new Reference();
    }

    public static Reference newReferenceAsIdentifier(String system, String value, String display){
        return new Reference()
                .setIdentifier(newIdentifier(system, value))
                .setDisplay(display);
    }

    public static Reference newReferenceAsIdentifier(String system, String value){
        return newReferenceAsIdentifier(system, value, "");
    }

    public static Quantity newQuantity(String unit, BigDecimal value){
        return new Quantity().setUnit(unit).setValue(value);
    }

    public static Quantity newQuantity(String system, String code, String unit, BigDecimal value){
        return newQuantity(unit, value)
                .setSystem(system)
                .setCode(code);
    }

    public static ContactPoint newTelecom(String value, ContactPoint.ContactPointUse use){
        return newTelecom(value).setUse(use);
    }

    public static ContactPoint newTelecom(String value){
        return new ContactPoint()
                .setSystem(ContactPoint.ContactPointSystem.PHONE)
                .setValue(value);
    }

    public static <R extends IBaseResource> String url(R resource){
        return dominio.concat("/").concat(resource.fhirType());
    }
}
