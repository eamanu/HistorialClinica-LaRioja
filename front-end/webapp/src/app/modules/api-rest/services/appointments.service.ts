import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { AppointmentListDto, CreateAppointmentDto } from '@api-rest/api-model';
import { HttpClient } from '@angular/common/http';

import { environment } from '@environments/environment';
import { ContextService } from '@core/services/context.service';
@Injectable({
  providedIn: 'root'
})
export class AppointmentsService {

	constructor(
		private http: HttpClient,
		private contextService: ContextService,
	) { }


	create(appointment: CreateAppointmentDto): Observable<number> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/appointments`;
		return this.http.post<number>(url, appointment);
	}

	getList(diaryIds: number[], from: string, to: string): Observable<AppointmentListDto[]> {
		if (!diaryIds || diaryIds.length === 0) {
			return of([]);
		}
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/appointments`;
		return this.http.get<AppointmentListDto[]>(url,{
			params: { 
				diaryIds: `${diaryIds.join(',')}` ,
				from: from,
				to: to
			}
		});
	}


}