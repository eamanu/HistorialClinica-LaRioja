import { Component, Input, OnInit } from '@angular/core';
import { EmergencyCareTypes, Triages } from '../../constants/masterdata';
import { VitalSingCurrentPrevious } from '@presentation/components/signo-vital-current-previous/signo-vital-current-previous.component';

@Component({
	selector: 'app-triage-details',
	templateUrl: './triage-details.component.html',
	styleUrls: ['./triage-details.component.scss']
})

export class TriageDetailsComponent implements OnInit {

	@Input('triage')
	set triageData(triage: Triage) {
		this.triage = triage;
		this.vitalSignsCurrent = includesVitalSigns() ? this.mapToVitalSignCurrentPrevious(triage) : undefined;

		function includesVitalSigns(): boolean {
			return !!triage.emergencyCareType;
		}
	}

	readonly triages = Triages;
	readonly emergencyCareTypes = EmergencyCareTypes;

	triage: Triage;
	vitalSignsCurrent: VitalSingCurrentPrevious[];

	constructor() {
	}

	ngOnInit(): void {
	}

	private mapToVitalSignCurrentPrevious(triage: Triage): VitalSingCurrentPrevious[] {
		const vitalSignsCurrent: VitalSingCurrentPrevious[] = [];
		const LABELS = triage.emergencyCareType === EmergencyCareTypes.PEDIATRIA ?
			{
				respiratoryRate: 'Frecuencia respiratoria',
				bloodOxygenSaturation: 'Saturación de oxígeno',
				heartRate: 'Frecuencia cardíaca',
			} :
			{
				systolicBloodPressure: 'Tensión arterial sistólica',
				diastolicBloodPressure: 'Tensión arterial diastólica',
				heartRate: 'Frecuencia cardíaca',
				respiratoryRate: 'Frecuencia respiratoria',
				temperature: 'Temperatura',
				bloodOxygenSaturation: 'Saturación de oxígeno',
			};

		Object.keys(LABELS).forEach(key => {
				const vitalSign = getVitalSign(key);
				vitalSignsCurrent.push({
					description: LABELS[key],
					currentValue: {
						value: vitalSign?.value || undefined,
						effectiveTime: vitalSign?.effectiveTime || undefined
					}
				});
			}
		);

		return vitalSignsCurrent;

		function getVitalSign(key): { value: number, effectiveTime: Date } {
			if (triage.vitalSigns && triage.vitalSigns[key]) {
				return triage.vitalSigns[key];
			}
			if (triage.breathing && (key === 'respiratoryRate' || key === 'bloodOxygenSaturation')) {
				return triage.breathing[key];
			}
			if (triage.circulation && key === 'heartRate') {
				return triage.circulation[key];
			}
			return undefined;
		}
	}

}

export interface Triage {
	emergencyCareType?: EmergencyCareTypes;
	creationDate: Date;
	category: {
		id: number;
		name: string;
		colorHex: string;
	};
	professional: {
		firstName: string,
		lastName: string
	};
	doctorsOfficeDescription?: string;
	vitalSigns?: {
		bloodOxygenSaturation: {
			value: number,
			effectiveTime: Date
		},
		diastolicBloodPressure?: {
			value: number,
			effectiveTime: Date
		},
		heartRate: {
			value: number,
			effectiveTime: Date
		},
		respiratoryRate: {
			value: number,
			effectiveTime: Date
		},
		systolicBloodPressure?: {
			value: number,
			effectiveTime: Date
		},
		temperature?: {
			value: number,
			effectiveTime: Date
		}
	};
	appearance?: {
		bodyTemperatureDescription?: string,
		cryingExcessive?: boolean,
		muscleHypertoniaDescription?: string
	};
	breathing?: {
		respiratoryRetractionDescription: string,
		stridor: boolean,
		respiratoryRate: {
			value: number
			effectiveTime: Date,
		},
		bloodOxygenSaturation: {
			value: number
			effectiveTime: Date,
		}
	};
	circulation?: {
		perfusionDescription: string,
		heartRate: {
			value: number
			effectiveTime: Date,
		}
	};
	notes?: string;
}
