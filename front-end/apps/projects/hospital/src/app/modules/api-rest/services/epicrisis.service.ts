import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EpicrisisGeneralStateDto, EpicrisisDto, ResponseEpicrisisDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { DownloadService } from '@core/services/download.service';
import { ContextService } from '@core/services/context.service';

@Injectable({
	providedIn: 'root'
})
export class EpicrisisService {

	constructor(
		private http: HttpClient,
		private downloadService: DownloadService,
		private contextService: ContextService,
	) { }

	getInternmentGeneralState(internmentEpisodeId: number): Observable<EpicrisisGeneralStateDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/epicrisis/general`;
		return this.http.get<EpicrisisGeneralStateDto>(url);
	}

	createDocument(epicrisis: EpicrisisDto, internmentEpisodeId: number): Observable<ResponseEpicrisisDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/epicrisis`;
		return this.http.post<ResponseEpicrisisDto>(url, epicrisis);
	}

	getPDF(epicrisisId: number, internmentEpisodeId: number): Observable<any> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/epicrisis/${epicrisisId}/report`;
		const fileName = `Epicrisis_internacion_${internmentEpisodeId}`;
		return this.downloadService.downloadPdf(url, fileName);
	}

}
