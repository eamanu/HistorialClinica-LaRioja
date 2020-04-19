import { Component, Input, OnInit } from '@angular/core';

@Component({
	selector: 'app-patient-card',
	templateUrl: './patient-card.component.html',
	styleUrls: ['./patient-card.component.scss']
})
export class PatientCardComponent implements OnInit {

	@Input() patient: PatientBasicData;

	constructor() {	}

	ngOnInit(): void { }

}

export class PatientBasicData {
	id: number;
	firstName: string;
	lastName: string;
	gender: string;
	age: number;
}
