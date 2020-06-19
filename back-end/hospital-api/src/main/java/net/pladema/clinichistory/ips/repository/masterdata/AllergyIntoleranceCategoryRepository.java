package net.pladema.clinichistory.ips.repository.masterdata;

import net.pladema.clinichistory.ips.repository.masterdata.entity.AllergyIntoleranceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllergyIntoleranceCategoryRepository extends JpaRepository<AllergyIntoleranceCategory, String> {

}