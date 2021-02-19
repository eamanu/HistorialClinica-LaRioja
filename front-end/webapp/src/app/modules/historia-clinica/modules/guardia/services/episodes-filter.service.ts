import { Injectable } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Episode } from '../routes/home/home.component';
import { TriageCategoryDto, TriageMasterDataService } from '@api-rest/services/triage-master-data.service';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { Observable } from 'rxjs';
import { MasterDataInterface } from '@api-rest/api-model';
import { tap } from 'rxjs/operators';
import { atLeastOneValueInFormGroup } from '@core/utils/form.utils';

const NO_INFO: MasterDataInterface<number> = {
	id: -1,
	description: 'No definido'
};

@Injectable({
	providedIn: 'root'
})
export class EpisodesFilterService {

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly triageMasterDataService: TriageMasterDataService,
		private readonly emergencyCareMasterDataService: EmergencyCareMasterDataService
	) {
		this.form = this.formBuilder.group({
			triage: [null],
			emergencyCareType: [null],
			patientId: [null],
			firstName: [null],
			lastName: [null],
			temporal: [null],
			noPatient: [null]
		});
	}

	private form: FormGroup;

	static filterByTriage(episode: Episode, filters: EpisodeFilters): boolean {
		return (filters.triage ? episode.triage.id === filters.triage : true);
	}

	static filterByEmergencyCareType(episode: Episode, filters: EpisodeFilters): boolean {
		if (!filters.emergencyCareType) {
			return true;
		}
		if (filters.emergencyCareType === NO_INFO.id) {
			return (!episode.type);
		}
		return (filters.emergencyCareType ? episode.type?.id === filters.emergencyCareType : true);
	}

	static filterByPatientId(episode: Episode, filters: EpisodeFilters): boolean {
		return (filters.patientId ? episode.patient?.id === filters.patientId : true);
	}

	static filterByFirstName(episode: Episode, filters: EpisodeFilters): boolean {
		return (filters.firstName ?
			this.filterString(episode.patient?.person?.firstName, filters.firstName) : true);
	}

	static filterByLastName(episode: Episode, filters: EpisodeFilters): boolean {
		return (filters.lastName ?
			this.filterString(episode.patient?.person?.lastName, filters.lastName) : true);
	}

	static filterString(target: string, filterValue: string): boolean {
		return target?.toLowerCase().includes(filterValue.toLowerCase());
	}

	static filterTemporal(episode: Episode, filters: EpisodeFilters): boolean {
		const PACIENTE_TEMPORAL = 3;
		return (filters.temporal ? episode.patient?.typeId === PACIENTE_TEMPORAL : true);
	}

	static filterNoPatient(episode: Episode, filters: EpisodeFilters) {
		return (filters.noPatient ? !episode.patient : true);
	}

	getForm(): FormGroup {
		return this.form;
	}

	filter(episode: Episode): boolean {
		const filters = this.form.value as EpisodeFilters;
		return 	EpisodesFilterService.filterByTriage(episode, filters) &&
				EpisodesFilterService.filterByEmergencyCareType(episode, filters) &&
				EpisodesFilterService.filterByPatientId(episode, filters) &&
				EpisodesFilterService.filterByFirstName(episode, filters) &&
				EpisodesFilterService.filterByLastName(episode, filters) &&
				EpisodesFilterService.filterTemporal(episode, filters) &&
				EpisodesFilterService.filterNoPatient(episode, filters);
	}

	clear(control: string): void {
		this.form.controls[control].reset();
	}

	hasFilters(): boolean {
		return atLeastOneValueInFormGroup(this.form);
	}

	getTriageCategories(): Observable<TriageCategoryDto[]> {
		return this.triageMasterDataService.getCategories();
	}

	getEmergencyCareTypes(): Observable<MasterDataInterface<number>[]> {
		return this.emergencyCareMasterDataService.getType().pipe(tap(types => types.unshift(NO_INFO)));
	}

}

interface EpisodeFilters {
	triage?: number;
	emergencyCareType?: number;
	patientId?: number;
	firstName?: string;
	lastName?: string;
	temporal?: boolean;
	noPatient?: boolean;
}
