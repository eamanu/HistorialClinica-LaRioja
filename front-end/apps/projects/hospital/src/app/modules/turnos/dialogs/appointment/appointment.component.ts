import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { NewAttentionComponent } from '../new-attention/new-attention.component';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { APPOINTMENT_STATES_ID, getAppointmentState, MAX_LENGTH_MOTIVO } from '../../constants/appointment';
import { ContextService } from '@core/services/context.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AppointmentDto } from '@api-rest/api-model';
import { ERole } from '@api-rest/api-model';
import { CancelAppointmentComponent } from '../cancel-appointment/cancel-appointment.component';
import { getError, hasError, processErrors } from '@core/utils/form.utils';
import { AppointmentsFacadeService } from '../../services/appointments-facade.service';
import { MapperService } from '@core/services/mapper.service';
import {
	determineIfIsHealthInsurance,
	HealthInsurance,
	PatientMedicalCoverage, PrivateHealthInsurance
} from '@core/dialogs/medical-coverage/medical-coverage.component';
import { map, take } from 'rxjs/operators';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { PermissionsService } from '@core/services/permissions.service';
import { Observable } from 'rxjs';

const TEMPORARY_PATIENT = 3;
const ROLES_TO_CHANGE_STATE: ERole[] = [ERole.ADMINISTRATIVO, ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ENFERMERO];
const ROLES_TO_EDIT_PHONE_NUMBER: ERole[]
	= [ERole.ADMINISTRATIVO, ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ENFERMERO];
@Component({
	selector: 'app-appointment',
	templateUrl: './appointment.component.html',
	styleUrls: ['./appointment.component.scss']
})
export class AppointmentComponent implements OnInit {

	readonly appointmentStatesIds = APPOINTMENT_STATES_ID;
	readonly TEMPORARY_PATIENT = TEMPORARY_PATIENT;
	getAppointmentState = getAppointmentState;
	getError = getError;
	hasError = hasError;

	appointment: AppointmentDto;
	estadoSelected: APPOINTMENT_STATES_ID;
	formMotivo: FormGroup;
	institutionId = this.contextService.institutionId;
	coverageText: string;
	coverageData: PatientMedicalCoverage;
	hasRoleToChangeState$: Observable<boolean>;
	hasRoleToEditPhoneNumber$: Observable<boolean>;

	constructor(
		@Inject(MAT_DIALOG_DATA) public appointmentData: PatientAppointmentInformation,
		public dialogRef: MatDialogRef<NewAttentionComponent>,
		private readonly dialog: MatDialog,
		private readonly appointmentService: AppointmentsService,
		private readonly snackBarService: SnackBarService,
		private readonly contextService: ContextService,
		private readonly formBuilder: FormBuilder,
		private readonly appointmentFacade: AppointmentsFacadeService,
		private readonly mapperService: MapperService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly permissionsService: PermissionsService,

	) {
	}

	ngOnInit(): void {

		this.formMotivo = this.formBuilder.group({
			motivo: ['', [Validators.required, Validators.maxLength(MAX_LENGTH_MOTIVO)]]
		});
		this.appointmentService.get(this.appointmentData.appointmentId)
			.subscribe(appointment => {
				this.appointment = appointment;
				this.estadoSelected = this.appointment?.appointmentStateId;
				if (this.appointment.stateChangeReason) {
					this.formMotivo.controls.motivo.setValue(this.appointment.stateChangeReason);
				}
				if (this.appointment.patientMedicalCoverageId) {
					this.patientMedicalCoverageService.
						getPatientMedicalCoverage(this.appointment.patientMedicalCoverageId)
						.pipe(
							map(
								s => this.mapperService.toPatientMedicalCoverage(s)
							)
						)
						.subscribe(coverageData => {
							if (coverageData) {
								const isHealthInsurance = determineIfIsHealthInsurance(coverageData.medicalCoverage);
								this.coverageData = coverageData;
								if (isHealthInsurance) {
									let healthInsurance: HealthInsurance;
									healthInsurance = coverageData.medicalCoverage as HealthInsurance;
									this.coverageText = healthInsurance.acronym ?
										healthInsurance.acronym : healthInsurance.name;
								} else {
									let privateHealthInsurance: PrivateHealthInsurance;
									privateHealthInsurance = coverageData.medicalCoverage as PrivateHealthInsurance;
									this.coverageText = privateHealthInsurance.name;
								}
							}
						});
				}
			});

		this.hasRoleToChangeState$ = this.permissionsService.hasContextAssignments$(ROLES_TO_CHANGE_STATE).pipe(take(1));

		this.hasRoleToEditPhoneNumber$ = this.permissionsService.hasContextAssignments$(ROLES_TO_EDIT_PHONE_NUMBER).pipe(take(1));
	}

	changeState(newStateId: APPOINTMENT_STATES_ID): void {
		this.estadoSelected = newStateId;
	}

	onClickedState(newStateId: APPOINTMENT_STATES_ID): void {
		if (this.estadoSelected !== newStateId) {
			this.changeState(newStateId);
			if (this.isANewState(newStateId) && !this.isMotivoRequired()) {
				this.submitNewState(newStateId);
			}
		}
	}

	private isANewState(newStateId: APPOINTMENT_STATES_ID) {
		return newStateId !== this.appointment?.appointmentStateId;
	}

	cancelAppointment(): void {
		const dialogRefCancelAppointment = this.dialog.open(CancelAppointmentComponent, {
			data: this.appointmentData.appointmentId
		});
		dialogRefCancelAppointment.afterClosed().subscribe(canceledAppointment => {
			if (canceledAppointment) {
				this.closeDialog('statuschanged');
			}
		});
	}

	saveAbsent(): void {
		if (this.formMotivo.valid) {
			this.submitNewState(APPOINTMENT_STATES_ID.ABSENT, this.formMotivo.value.motivo);
		}
	}

	isMotivoRequired(): boolean {
		return this.estadoSelected === APPOINTMENT_STATES_ID.ABSENT;
	}

	isCancelable(): boolean {
		return (this.estadoSelected === APPOINTMENT_STATES_ID.ASSIGNED &&
			this.appointment?.appointmentStateId === APPOINTMENT_STATES_ID.ASSIGNED) ||
			(this.estadoSelected === APPOINTMENT_STATES_ID.CONFIRMED &&
				this.appointment?.appointmentStateId === APPOINTMENT_STATES_ID.CONFIRMED);
	}

	private submitNewState(newStateId: APPOINTMENT_STATES_ID, motivo?: string): void {
		this.appointmentFacade.changeState(this.appointmentData.appointmentId, newStateId, motivo)
			.subscribe(() => {
				this.closeDialog('statuschanged');
				this.snackBarService.showSuccess(`Estado de turno actualizado a ${getAppointmentState(newStateId).description} exitosamente`);
			}, _ => {
				this.changeState(this.appointment?.appointmentStateId);
				this.snackBarService.showError(`Error al actualizar estado de turno
				${getAppointmentState(this.appointment?.appointmentStateId).description} a ${getAppointmentState(newStateId).description}`);
			});
	}

	updatePhoneNumber(phoneNumber: string) {
		this.appointmentFacade.updatePhoneNumber(this.appointmentData.appointmentId, phoneNumber).
			subscribe(() => {
				this.snackBarService.showSuccess('turnos.appointment.phoneNumber.SUCCESS');
			}, error => {
				processErrors(error, (msg) => this.snackBarService.showError(msg));
			});
	}

	closeDialog(returnValue?: string) {
		this.dialogRef.close(returnValue);
	}
}

export interface PatientAppointmentInformation {
	patient: {
		id: number,
		fullName?: string
		identificationNumber?: string,
		typeId: number;
	};
	appointmentId: number;
	appointmentStateId: number;
	date: Date;
	phoneNumber: string;
	healthInsurance?: {
		name: string,
		acronym?: string;
	};
	medicalCoverageName: string;
	affiliateNumber: string;
}
