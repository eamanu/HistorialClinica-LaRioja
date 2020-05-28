import { Injectable } from '@angular/core';
import { Observable, of } from "rxjs";
import { PublicService } from "@api-rest/services/public.service";
import { HttpClient } from "@angular/common/http";
import { map } from "rxjs/operators";

const FLAVOR_TANDIL = 'tandil';
const FLAVOR_HOSPITALES = 'minsal';
const FEATURE_FLAGS = [
	{
		name: 'habilitarEditarPaciente',
		flavorMatch: FLAVOR_TANDIL
	},
	{
		name: 'agregarContactoResponsable',
		flavorMatch: FLAVOR_TANDIL
	},
	{
		name: 'agregarMedicoCabecera',
		flavorMatch: FLAVOR_TANDIL
	}];

@Injectable({
	providedIn: 'root'
})
export class FeatureFlagService {

	constructor(private http: HttpClient,
	            private publicService: PublicService) {
	}

	public isOn(ff: string): Observable<boolean> {
		return this.publicService.getInfo().pipe(map(data => {
			let ffObject = this.getIfIsPresent(ff);
			return ffObject && data.flavor === ffObject.flavorMatch ? true : false;
		}));
	}

	private getIfIsPresent(ff: string) {
		return FEATURE_FLAGS.find(element => element.name === ff);
	}
}
