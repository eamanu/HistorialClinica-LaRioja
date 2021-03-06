import { Component, Input, OnInit } from '@angular/core';
import { SummaryHeader } from '@presentation/components/summary-card/summary-card.component';
import { SIGNOS_VITALES } from '../../constants/summaries';
import { VitalSingCurrentPrevious } from '@presentation/components/signo-vital-current-previous/signo-vital-current-previous.component';
import { Last2VitalSignsDto, VitalSignDto } from '@api-rest/api-model';
import { momentParseDateTime } from '@core/utils/moment.utils';
import { MatDialog } from '@angular/material/dialog';
import { AddVitalSignsComponent } from '../../dialogs/add-vital-signs/add-vital-signs.component';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-signos-vitales-summary',
	templateUrl: './signos-vitales-summary.component.html',
	styleUrls: ['./signos-vitales-summary.component.scss']
})
export class SignosVitalesSummaryComponent implements OnInit {

	@Input() internmentEpisodeId: number;
	@Input() editable = false;
	@Input() vitalSigns$: Observable<Last2VitalSignsDto>;

	signosVitalesSummary: SummaryHeader = SIGNOS_VITALES;
	signosVitales: VitalSingCurrentPrevious[] = [];

	constructor(
		public dialog: MatDialog
	) { }

	ngOnInit(): void {
		this.refreshVitalSigns();
	}

	refreshVitalSigns(): void {
		this.vitalSigns$.subscribe(
			this.initSignosVitales(), this.initSignosVitales()
		);
	}

	initSignosVitales(): (vitalSigns: Last2VitalSignsDto) => void {
		const LABELS = {
			systolicBloodPressure: 'Tensión arterial sistólica',
			diastolicBloodPressure: 'Tensión arterial diastólica',
			heartRate: 'Frecuencia cardíaca',
			respiratoryRate: 'Frecuencia respiratoria',
			temperature: 'Temperatura',
			bloodOxygenSaturation: 'Saturación de oxígeno',
		};
		return (vitalSigns: Last2VitalSignsDto) => {
			this.signosVitales = [];
			const current: VitalSignDto = vitalSigns.current || {};
			const previous: VitalSignDto = vitalSigns.previous || {};
			Object.keys(LABELS).forEach(key => this.signosVitales.push(
				{
					description: LABELS[key],
					currentValue: {
						value: Number(current[key]?.value),
						effectiveTime: current[key]?.effectiveTime ? momentParseDateTime(current[key].effectiveTime) : undefined
					},
					previousValue: {
						value: Number(previous[key]?.value),
						effectiveTime: previous[key]?.effectiveTime ? momentParseDateTime(previous[key].effectiveTime) : undefined
					}
				}
			));
		};
	}


	openDialog() {
		const dialogRef = this.dialog.open(AddVitalSignsComponent, {
			disableClose: true,
			width: '35%',
			data: {
				internmentEpisodeId: this.internmentEpisodeId
			}
		});

		dialogRef.afterClosed().subscribe(submitted => {
			if (submitted) {
				this.refreshVitalSigns();
			}
		});
	}
}
