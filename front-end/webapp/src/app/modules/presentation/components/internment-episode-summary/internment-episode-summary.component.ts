import { Component, OnInit, Input } from '@angular/core';

@Component({
	selector: 'app-internment-episode-summary',
	templateUrl: './internment-episode-summary.component.html',
	styleUrls: ['./internment-episode-summary.component.scss']
})
export class InternmentEpisodeSummaryComponent implements OnInit {

	@Input() internmentEpisode: InternmentEpisode;

	constructor() { }

	ngOnInit(): void {
	}

}

export interface InternmentEpisode {
	roomNumber: string;
	bedNumber: string;
	specialtyName: string;
	doctor: {
		firstName: string;
		lastName: string;
		license: string;
	};
	totalInternmentDays: number;
	admissionDatetime: string;
}
