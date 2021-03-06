import { Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { HttpClient } from '@angular/common/http';
import { EducationLevelDto, EthnicityDto, GenderDto, IdentificationTypeDto, PersonOccupationDto } from '@api-rest/api-model';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class PersonMasterDataService {

	constructor(
		private http: HttpClient
	) { }

	getGenders(): Observable<GenderDto[]> {
		const url = `${environment.apiBase}/person/masterdata/genders`;
		return this.http.get<GenderDto[]>(url);
	}


	getIdentificationTypes(): Observable<IdentificationTypeDto[]> {
		const url = `${environment.apiBase}/person/masterdata/identificationTypes`;
		return this.http.get<IdentificationTypeDto[]>(url);
	}

	getEthnicities(): Observable<EthnicityDto[]> {
		const url = `${environment.apiBase}/person/masterdata/ethnicities`;
		return this.http.get<EthnicityDto[]>(url);
	}

	getOccupations(): Observable<PersonOccupationDto[]> {
		const url = `${environment.apiBase}/person/masterdata/occupations`;
		return this.http.get<PersonOccupationDto[]>(url);
	}

	getEducationLevels(): Observable<EducationLevelDto[]> {
		const url = `${environment.apiBase}/person/masterdata/educationLevel`;
		return this.http.get<EducationLevelDto[]>(url);
	}
}
