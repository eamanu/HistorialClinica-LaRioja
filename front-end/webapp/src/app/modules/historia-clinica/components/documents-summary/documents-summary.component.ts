import { Component, Input, OnInit } from '@angular/core';
import { DOCUMENTS, DOCUMENTS_SEARCH_FIELDS } from '../../constants/summaries';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Moment } from 'moment';
import { DocumentHistoricDto, DocumentSearchFilterDto, EDocumentSearch, DocumentSearchDto } from '@api-rest/api-model';
import { DateFormat, momentFormat, newMoment } from '@core/utils/moment.utils';
import { EvolutionNotesListenerService } from '../../modules/internacion/services/evolution-notes-listener.service';

@Component({
	selector: 'app-documents-summary',
	templateUrl: './documents-summary.component.html',
	styleUrls: ['./documents-summary.component.scss']
})
export class DocumentsSummaryComponent implements OnInit {

	@Input() internmentEpisodeId: number;

	public searchFields: SearchField[] = DOCUMENTS_SEARCH_FIELDS;
	public documentHistoric: DocumentHistoricDto;
	public documentsToShow: DocumentSearchDto[];
	public readonly documentsSummary = DOCUMENTS;
	public today: Moment = newMoment();
	public form: FormGroup;
	public activeDocument;
	public searchTriggered: boolean = false;

	constructor(
		private formBuilder: FormBuilder,
		private evolutionNotesListenerService: EvolutionNotesListenerService,
	) {
		evolutionNotesListenerService.history$
			.subscribe(documents => {
				this.documentHistoric = documents;
				this.updateDocuments();
				this.activeDocument = undefined;
			});
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			text: [''],
			date: [null],
			field: ['ALL'],
			mainDiagnosisOnly: [false],
		});
		this.evolutionNotesListenerService.setInternmentEpisodeId(this.internmentEpisodeId);
	}

	search(): void {
		this.searchTriggered = true;
		const searchFilter: DocumentSearchFilterDto = {
			plainText: isDate(this.form.value.field) ?
				this.form.controls.date.valid && this.form.value.date ? momentFormat(this.form.value.date, DateFormat.API_DATE) : undefined
				: this.form.value.text,
			searchType: this.form.value.field,
		};

		if (!isDate(searchFilter.searchType) || (isDate(searchFilter.searchType) && searchFilter.plainText)) {
			this.evolutionNotesListenerService.setSerchFilter(searchFilter);
		}

		function isDate(field): boolean {
			return field === 'CREATEDON';
		}
	}

	setActive(document) {
		this.activeDocument = document;
	}

	updateDocuments() {
		this.documentsToShow = this.documentHistoric.documents.filter(document => {
			return this.form.value.mainDiagnosisOnly ? document.mainDiagnosis.length : true;
		});
	}

}

export interface SearchField {
	field: EDocumentSearch;
	label: string;
}