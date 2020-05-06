import { Injectable } from '@angular/core';
import { BasicPatientDto, InternmentSummaryDto } from '@api-rest/api-model';
import { InternmentEpisodeSummary } from '../components/internment-episode-summary/internment-episode-summary.component';
import { PatientBasicData } from '../components/patient-card/patient-card.component';

@Injectable({
  providedIn: 'root'
})
export class MapperService {

	toInternmentEpisodeSummary: (o: InternmentSummaryDto) => InternmentEpisodeSummary = MapperService._toInternmentEpisodeSummary;
	toPatientBasicData: (o: BasicPatientDto) => PatientBasicData = MapperService._toPatientBasicData;

	constructor() {
	}

	private static _toInternmentEpisodeSummary(internmentSummary: InternmentSummaryDto): InternmentEpisodeSummary {
		return {
			bedNumber: internmentSummary.bed.bedNumber,
			roomNumber: internmentSummary.bed.room.roomNumber,
			specialtyName: internmentSummary.specialty.name,
			doctor: {
				firstName: internmentSummary.doctor.firstName,
				lastName: internmentSummary.doctor.lastName,
				license: internmentSummary.doctor.licence
			},
			totalInternmentDays: internmentSummary.totalInternmentDays,
			admissionDatetime: internmentSummary.createdOn?.toString()
		};
	}

	private static _toPatientBasicData(patient: BasicPatientDto): PatientBasicData {
		return {
			id: patient.id,
			firstName: patient.person.firstName,
			lastName: patient.person.lastName,
			gender: patient.person.gender.description,
			age: patient.person.age
		};
	}

}
