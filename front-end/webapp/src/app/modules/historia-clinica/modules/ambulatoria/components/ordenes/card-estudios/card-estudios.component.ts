import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DiagnosticReportInfoDto, PrescriptionDto } from '@api-rest/api-model';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ESTUDIOS } from 'src/app/modules/historia-clinica/constants/summaries';
import { STUDY_STATUS } from '../../../constants/prescripciones-masterdata';
import { ConfirmarPrescripcionComponent } from '../../../dialogs/ordenes-prescripciones/confirmar-prescripcion/confirmar-prescripcion.component';
import { NuevaPrescripcionComponent } from '../../../dialogs/ordenes-prescripciones/nueva-prescripcion/nueva-prescripcion.component';
import { VerResultadosEstudioComponent } from '../../../dialogs/ordenes-prescripciones/ver-resultados-estudio/ver-resultados-estudio.component';
import { PrescripcionesService, PrescriptionTypes } from '../../../services/prescripciones.service';
import { PrescriptionItemData } from '../item-prescripciones/item-prescripciones.component';
import { CompletarEstudioComponent } from './../../../dialogs/ordenes-prescripciones/completar-estudio/completar-estudio.component';

@Component({
  selector: 'app-card-estudios',
  templateUrl: './card-estudios.component.html',
  styleUrls: ['./card-estudios.component.scss']
})
export class CardEstudiosComponent implements OnInit {

	public readonly estudios = ESTUDIOS;
	public readonly STUDY_STATUS = STUDY_STATUS;
	public diagnosticReportsInfo : DiagnosticReportInfoDto[];

	@Input('patientId') patientId: number;

	constructor(
		private readonly dialog: MatDialog,
		private prescripcionesService: PrescripcionesService,
		private snackBarService: SnackBarService,
	) { }

	ngOnInit(): void {
		this.getStudy();
	}

	private getStudy(): void {
		this.prescripcionesService.getPrescription(PrescriptionTypes.STUDY, this.patientId, null, null, null).subscribe(
			response => {
				this.diagnosticReportsInfo = response;
			});
	}

	openDialogNewStudy() {
		const newStudyDialog = this.dialog.open(NuevaPrescripcionComponent,
			{
				data: {
					patientId: this.patientId,
					titleLabel: 'ambulatoria.paciente.ordenes_prescripciones.new_prescription_dialog.STUDY_TITLE',
					addLabel: 'ambulatoria.paciente.ordenes_prescripciones.new_prescription_dialog.ADD_STUDY_LABEL',
					prescriptionType: PrescriptionTypes.STUDY,
					prescriptionItemList: undefined,
					addPrescriptionItemDialogData: {
						titleLabel: 'ambulatoria.paciente.ordenes_prescripciones.add_prescription_item_dialog.STUDY_TITLE',
						searchSnomedLabel: 'ambulatoria.paciente.ordenes_prescripciones.add_prescription_item_dialog.STUDY',
						showDosage: false,
						showStudyCategory: true,
						eclTerm: 'procedure',
					}
				},
				width: '35%',
			});

		newStudyDialog.afterClosed().subscribe((newPrescription: PrescriptionDto) => {
			if (newPrescription) {
				const newServiceRequest$ = this.prescripcionesService.createPrescription(PrescriptionTypes.STUDY, newPrescription, this.patientId);
				const confirmPrescriptionDialog = this.dialog.open(ConfirmarPrescripcionComponent,
					{
						disableClose: true,
						data: {
							titleLabel: 'ambulatoria.paciente.ordenes_prescripciones.confirm_prescription_dialog.STUDY_TITLE',
							downloadButtonLabel: 'ambulatoria.paciente.ordenes_prescripciones.confirm_prescription_dialog.DOWNLOAD_BUTTON_STUDY',
							successLabel: 'ambulatoria.paciente.ordenes_prescripciones.toast_messages.POST_STUDY_SUCCESS',
							errorLabel: 'ambulatoria.paciente.ordenes_prescripciones.toast_messages.POST_STUDY_ERROR',
							prescriptionRequest: newServiceRequest$,
						},
						width: '35%',
					});

				confirmPrescriptionDialog.afterClosed().subscribe( (hasError: boolean) => {
					if (!hasError) {
						this.getStudy();
					}
				});
			}
		});
	}

	deleteStudy(id: number) {
		this.prescripcionesService.deleteStudy(this.patientId, id).subscribe(() => {
			this.snackBarService.showSuccess('ambulatoria.paciente.ordenes_prescripciones.toast_messages.DELETE_STUDY_SUCCESS');
			this.getStudy();
		}, _ => {this.snackBarService.showError('ambulatoria.paciente.ordenes_prescripciones.toast_messages.DELETE_STUDY_ERROR')});
	}

	prescriptionItemDataBuilder(diagnosticReport: DiagnosticReportInfoDto): PrescriptionItemData {
		return {
			prescriptionStatus: this.prescripcionesService.renderStatusDescription(PrescriptionTypes.STUDY, diagnosticReport.statusId),
			prescriptionPt: diagnosticReport.snomed.pt,
			problemPt: diagnosticReport.healthCondition.snomed.pt,
			doctor: diagnosticReport.doctor,
			totalDays: diagnosticReport.totalDays
		}
	}

	completeStudy(diagnosticReport: DiagnosticReportInfoDto) {
		const newCompleteStudy = this.dialog.open(CompletarEstudioComponent,
			{
				data: {
					diagnosticReport: diagnosticReport,
					patientId: this.patientId,
				},
				width: '35%',
			});

			newCompleteStudy.afterClosed().subscribe((completed: any) => {
				if (completed) {
					if (completed.completed) {
						this.snackBarService.showSuccess('ambulatoria.paciente.ordenes_prescripciones.toast_messages.COMPLETE_STUDY_SUCCESS');
						this.getStudy();
					} else {
						this.snackBarService.showError('ambulatoria.paciente.ordenes_prescripciones.toast_messages.COMPLETE_STUDY_ERROR');
					}
				} 
			});
	}

	showStudyResults(diagnosticReport: DiagnosticReportInfoDto) {
		const newShowStudyResults = this.dialog.open(VerResultadosEstudioComponent,
			{
				data: {
					diagnosticReport: diagnosticReport,
					patientId: this.patientId,
				},
				width: '35%',
			});
	}
}
