import { Component, ElementRef, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { MatDialog } from "@angular/material/dialog";
import { TranslateService } from '@ngx-translate/core';

import { ConfirmDialogComponent } from "@core/dialogs/confirm-dialog/confirm-dialog.component";
import { ContextService } from "@core/services/context.service";
import { FeatureFlagService } from "@core/services/feature-flag.service";
import { scrollIntoError } from "@core/utils/form.utils";

import { PersonService } from "@api-rest/services/person.service";
import { InternmentEpisodeService } from "@api-rest/services/internment-episode.service";
import { PatientService } from "@api-rest/services/patient.service";
import { InternacionMasterDataService } from "@api-rest/services/internacion-master-data.service";
import { SectorService } from "@api-rest/services/sector.service";
import { ClinicalSpecialtySectorService } from "@api-rest/services/clinical-specialty-sector.service";
import { RoomService } from "@api-rest/services/room.service";
import { HealthcareProfessionalService } from "@api-rest/services/healthcare-professional.service";
import {
	CompletePatientDto,
	HealthcareProfessionalDto,
	PersonalInformationDto
} from "@api-rest/api-model";

import {
	AppFeature,
} from '@api-rest/api-model';

import { PatientBasicData } from "@presentation/components/patient-card/patient-card.component";
import { PersonalInformation } from "@presentation/components/personal-information/personal-information.component";
import { PatientTypeData } from "@presentation/components/patient-type-logo/patient-type-logo.component";
import { MapperService } from "@presentation/services/mapper.service";
import { SnackBarService } from '@presentation/services/snack-bar.service';

const ROUTE_INTERNMENT = 'internaciones/internacion/';

@Component({
	selector: 'app-new-internment',
	templateUrl: './new-internment.component.html',
	styleUrls: ['./new-internment.component.scss']
})
export class NewInternmentComponent implements OnInit {

	public form: FormGroup;
	public specialties;
	public sectors;
	public services;
	public rooms;
	public beds;
	public doctors: HealthcareProfessionalDto[];
	public patientId: number;
	public patientBasicData: PatientBasicData;
	public personalInformation: PersonalInformation;
	public patientTypeData: PatientTypeData;
	private readonly routePrefix;

	constructor(private readonly formBuilder: FormBuilder,
		private readonly el: ElementRef,
		private readonly router: Router,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly sector: SectorService,
		private readonly clinicalSpecialtySectorService: ClinicalSpecialtySectorService,
		private readonly room: RoomService,
		private readonly healthcareProfessionalService: HealthcareProfessionalService,
		private readonly patientService: PatientService,
		private readonly personService: PersonService,
		private readonly mapperService: MapperService,
		private readonly route: ActivatedRoute,
		private readonly internmentEpisodeService: InternmentEpisodeService,
		public dialog: MatDialog,
		public translator: TranslateService,
		private readonly snackBarService: SnackBarService,
		private readonly contextService: ContextService,
		private readonly featureFlagService: FeatureFlagService) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/`;
	}

	ngOnInit(): void {

		this.route.queryParams.subscribe(params => {
			this.patientId = Number(params['patientId']);
			this.patientService.getPatientCompleteData<CompletePatientDto>(this.patientId)
				.subscribe(completeData => {
					this.patientTypeData = this.mapperService.toPatientTypeData(completeData.patientType);
					this.patientBasicData = this.mapperService.toPatientBasicData(completeData);
					this.personService.getPersonalInformation<PersonalInformationDto>(completeData.person.id)
						.subscribe(personInformationData => {
							this.personalInformation =
								this.mapperService.toPersonalInformationData(completeData, personInformationData);
						});
				});
		});

		this.form = this.formBuilder.group({
			specialtyId: [null, [Validators.required]],
			sectorId: [null, [Validators.required]],
			serviceId: [{ value: null, disabled: true }, [Validators.required]],
			roomId: [{ value: null, disabled: true }, [Validators.required]],
			bedId: [{ value: null, disabled: true }, [Validators.required]],
			doctorId: [null, [Validators.required]],
			contactName: [null],
			contactPhoneNumber: [null],
			contactRelationship: [null],
		});

		this.internacionMasterDataService.getClinicalSpecialty().subscribe(data => {
			this.specialties = data;
		});

		this.sector.getAll().subscribe(data => {
			this.sectors = data;
		});

		this.healthcareProfessionalService.getAllDoctors().subscribe(data => {
			this.doctors = data;
		});

		this.featureFlagService.isActive(AppFeature.RESPONSIBLE_DOCTOR_REQUIRED).subscribe(isOn => {
			if (!isOn) {
				this.form.controls.doctorId.clearValidators();
				this.form.controls.doctorId.reset();
			}
		});

	}

	setServices() {
		const sectorId: number = this.form.controls.sectorId.value;
		this.clinicalSpecialtySectorService.getClinicalSpecialty(sectorId).subscribe(data => {
			this.services = data;
		});
		this.form.controls.serviceId.reset();
		this.form.controls.serviceId.enable();

		this.form.controls.roomId.reset();
		this.form.controls.roomId.disable();

		this.form.controls.bedId.reset();
		this.form.controls.bedId.disable();

	}

	setRooms() {
		const sectorId: number = this.form.controls.sectorId.value;
		const serviceId: number = this.form.controls.serviceId.value;
		this.sector.getAllRoomsBySectorAndSpecialty(sectorId, serviceId).subscribe(data => {
			this.rooms = data;
		});
		this.form.controls.roomId.reset();
		this.form.controls.roomId.enable();

		this.form.controls.bedId.reset();
		this.form.controls.bedId.disable();
	}

	setBeds() {
		const roomId: number = this.form.controls.roomId.value;
		this.room.getAllFreeBedsByRoom(roomId).subscribe(data => {
			this.beds = data;
		});
		this.form.controls.bedId.reset();
		this.form.controls.bedId.enable();
	}

	save(): void {
		if (this.form.valid) {
			this.openDialog();
		} else {
			scrollIntoError(this.form, this.el);
		}
	}

	openDialog(): void {
		this.translator.get('internaciones.internacion-paciente.confirmacion').subscribe((res: string) => {
			const dialogRef = this.dialog.open(ConfirmDialogComponent, {
				width: '450px',
				data: {
					title: 'Nueva internación',
					content: `${res}${this.patientBasicData.id}?`,
					okButtonLabel: 'Confirmar internación'
				}
			});

			dialogRef.afterClosed().subscribe(result => {
				if (result) {
					const intenmentEpisodeReq = this.mapToPersonInternmentEpisodeRequest();
					this.internmentEpisodeService.setNewInternmentEpisode(intenmentEpisodeReq)
						.subscribe(data => {
							if (data && data.id) {
								const url = `${this.routePrefix}${ROUTE_INTERNMENT}${data.id}/paciente/${this.patientId}`;
								this.router.navigate([url]);
								this.snackBarService.showSuccess('internaciones.new-internment.messages.SUCCESS');
							}
						}, _ => this.snackBarService.showError('internaciones.new-internment.messages.ERROR'));
				}
			});
		});

	}

	private mapToPersonInternmentEpisodeRequest() {
		var response = {
			patientId: this.patientId,
			bedId: this.form.controls.bedId.value,
			clinicalSpecialtyId: this.form.controls.specialtyId.value,
			responsibleDoctorId: this.form.controls.doctorId.value,
			responsibleContact: null
		}
		const fullname = this.form.controls.contactName.value;
		const phoneNumber = this.form.controls.contactPhoneNumber.value;
		const relationship = this.form.controls.contactRelationship.value
		if (fullname || phoneNumber || relationship) {
			response.responsibleContact = {};
			if (fullname) {
				response.responsibleContact.fullName = fullname;
			}
			if (phoneNumber) {
				response.responsibleContact.phoneNumber = phoneNumber;
			}
			if (relationship) {
				response.responsibleContact.relationship = relationship;
			}
		}
		return response;
	}
}