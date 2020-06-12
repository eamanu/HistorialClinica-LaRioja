package net.pladema.sgx.masterdata.repository;

import net.pladema.internation.repository.projections.InternmentMasterDataProjection;

import java.util.Collection;

public interface MasterdataRepository {

    <T> Collection<InternmentMasterDataProjection> findAllInternmentProjectedBy(Class<T> clazz, String...filterIds);
}