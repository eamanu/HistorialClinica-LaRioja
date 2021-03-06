package net.pladema.clinichistory.documents.repository;

import net.pladema.clinichistory.documents.repository.entity.EvolutionNoteDocument;
import net.pladema.clinichistory.hospitalization.repository.domain.summary.EvaluationNoteSummaryVo;
import net.pladema.clinichistory.documents.repository.entity.EvolutionNoteDocumentPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EvolutionNoteDocumentRepository extends JpaRepository<EvolutionNoteDocument, EvolutionNoteDocumentPK> {

    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.clinichistory.hospitalization.repository.domain.summary.EvaluationNoteSummaryVo(d.id, d.statusId) " +
            "FROM EvolutionNoteDocument evnd " +
            "JOIN Document d ON (d.id = evnd.pk.documentId) " +
            "WHERE evnd.pk.internmentEpisodeId = :internmentEpisodeId " +
            "ORDER BY d.updateable.updatedOn DESC")
    Page<EvaluationNoteSummaryVo> getLastEvaluationNoteSummary(@Param("internmentEpisodeId") Integer internmentEpisodeId, Pageable pageable);
}
