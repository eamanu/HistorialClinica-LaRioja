import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { AppointmentDailyAmountDto, AppointmentDto, AppointmentListDto, CreateAppointmentDto } from '@api-rest/api-model';
import { HttpClient, HttpParams } from '@angular/common/http';

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

	getList(diaryIds: number[]): Observable<AppointmentListDto[]> {
		if (!diaryIds || diaryIds.length === 0) {
			return of([]);
		}
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/appointments`;
		return this.http.get<AppointmentListDto[]>(url, {
			params: {
				diaryIds: `${diaryIds.join(',')}`
			}
		});
	}

	changeState(appointmentId: number, appointmentStateId: number, reason?: string): Observable<boolean> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('appointmentStateId', JSON.stringify(appointmentStateId));

		if (reason) {
			queryParams = queryParams.append('reason', reason);
		}

		const url = `${environment.apiBase}/institutions/
					${this.contextService.institutionId}/medicalConsultations/appointments/
					${appointmentId}/change-state`;
		return this.http.put<boolean>(url, {}, {params : queryParams});
	}

	get(appoinmentId: number): Observable<AppointmentDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/appointments/${appoinmentId}`;
		return this.http.get<AppointmentDto>(url);
	}

	hasNewConsultationEnabled(patientId: number): Observable<boolean> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('patientId', JSON.stringify(patientId));

		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/appointments/confirmed-appointment`;
		return this.http.get<boolean>(url, {params : queryParams});
	}

	considerAppointments(): Observable<boolean> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/appointments/consider-appointment`;
		return this.http.get<boolean>(url);
	}

	updatePhoneNumber(appointmentId: number, phoneNumber: string ): Observable<boolean> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = (phoneNumber) ? queryParams.append('phoneNumber', phoneNumber) : queryParams;

		const url = `${environment.apiBase}/institutions/
					${this.contextService.institutionId}/medicalConsultations/appointments/
					${appointmentId}/update-phone-number`;
		return this.http.put<boolean>(url, {}, {params : queryParams});
	}

	getDailyAmounts(diaryId: number): Observable<AppointmentDailyAmountDto[]> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = (diaryId) ? queryParams.append('diaryId', JSON.stringify(diaryId)) : queryParams;

		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/appointments
		/getDailyAmounts`;
		return this.http.get<AppointmentDailyAmountDto[]>(url,
			{
				params: queryParams
			});
	}

}
