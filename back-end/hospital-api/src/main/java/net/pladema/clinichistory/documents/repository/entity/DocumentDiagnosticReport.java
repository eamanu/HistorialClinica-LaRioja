package net.pladema.clinichistory.documents.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "document_diagnostic_report")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentDiagnosticReport implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@EmbeddedId
	private DocumentDiagnosticReportPK pk;

	public DocumentDiagnosticReport(Long documentId, Integer diagnosticReportId){
		pk = new DocumentDiagnosticReportPK(documentId, diagnosticReportId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DocumentDiagnosticReport that = (DocumentDiagnosticReport) o;
		return Objects.equals(pk, that.pk);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pk);
	}

	public Long getDocumentId() {
		return pk.getDocumentId();
	}

}
