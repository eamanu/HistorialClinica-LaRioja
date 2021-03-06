import { Component, OnInit } from '@angular/core';
import { CompletePatientDto, InternmentEpisodeProcessDto, PatientMedicalCoverageDto, PersonalInformationDto, PersonPhotoDto } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { MapperService } from '@presentation/services/mapper.service';
import { ActivatedRoute, Router } from '@angular/router';
import { PersonService } from '@api-rest/services/person.service';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { PersonalInformation } from '@presentation/components/personal-information/personal-information.component';
import { PatientTypeData } from '@presentation/components/patient-type-logo/patient-type-logo.component';
import { ContextService } from '@core/services/context.service';
import { InternmentPatientService } from '@api-rest/services/internment-patient.service';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';


@Component({
  selector: 'app-patient-profile',
  templateUrl: './patient-profile.component.html',
  styleUrls: ['./patient-profile.component.scss']
})
export class PatientProfileComponent implements OnInit {

	public patientBasicData: PatientBasicData;
	public personalInformation: PersonalInformation;
	public patientTypeData: PatientTypeData;
	public person: PersonalInformationDto;
	public personPhoto: PersonPhotoDto;
	private patientId: number;
	private readonly routePrefix;
	public internmentEpisode: InternmentEpisodeProcessDto;
	public patientMedicalCoverage: PatientMedicalCoverageDto[];

	constructor(
		private readonly patientService: PatientService,
		private readonly mapperService: MapperService,
		private readonly route: ActivatedRoute,
		private readonly router: Router,
		private readonly personService: PersonService,
		private readonly contextService: ContextService,
		private readonly internmentPatientService: InternmentPatientService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService) {
		this.routePrefix = `institucion/${this.contextService.institutionId}`;
	}

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('idPaciente'));
				this.patientService.getPatientCompleteData<CompletePatientDto>(this.patientId)
					.subscribe(completeData => {
						this.patientTypeData = this.mapperService.toPatientTypeData(completeData.patientType);
						this.patientBasicData = this.mapperService.toPatientBasicData(completeData);
						this.personService.getPersonalInformation<PersonalInformationDto>(completeData.person.id)
							.subscribe(personInformationData => {
								this.personalInformation = this.mapperService.toPersonalInformationData(completeData, personInformationData);
							});
					});

				this.patientService.getPatientPhoto(this.patientId)
					.subscribe((personPhotoDto: PersonPhotoDto) => {this.personPhoto = personPhotoDto;
				});


				this.patientMedicalCoverageService.getActivePatientMedicalCoverages(this.patientId)
					.subscribe(patientMedicalCoverageDto => this.patientMedicalCoverage = patientMedicalCoverageDto);

				this.internmentPatientService.internmentEpisodeIdInProcess(this.patientId)
					.subscribe(internmentEpisodeProcessDto => {
						if (internmentEpisodeProcessDto) {
							this.internmentEpisode = internmentEpisodeProcessDto;
						}
					});
			});
	}

	goToAmbulatoria() {
		const url = `${this.routePrefix}/ambulatoria/paciente/${this.patientId}`;
		this.router.navigateByUrl(url);
	}
}
