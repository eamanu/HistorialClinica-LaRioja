import { Component, OnInit, OnDestroy } from '@angular/core';
import { BedSummaryDto } from '@api-rest/api-model';
import { MapperService } from '@presentation/services/mapper.service';
import { map, tap } from 'rxjs/operators';
import { BedManagmentService } from './../../services/bed-managment.service';
import { Subscription } from 'rxjs';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss'],
	providers: [ BedManagmentService ]
})
export class HomeComponent implements OnInit, OnDestroy {

	public selectedBed;
	public bedManagmentList: BedManagment[];
	public bedsAmount;

	private managmentBed$: Subscription;

	constructor(
		private mapperService: MapperService,
		private bedManagmentService: BedManagmentService
  	) { }

	ngOnInit(): void {
		this.managmentBed$ = this.bedManagmentService.getBedManagment().pipe(
			tap(bedsSummary => this.bedsAmount = bedsSummary ? bedsSummary.length : 0),
			map((bedsSummary: BedSummaryDto[]) => bedsSummary ? this.mapperService.toBedManagment(bedsSummary) : null)
		).subscribe(data => this.bedManagmentList = data);
	}

	selectBed(bedId) {
		this.selectedBed = bedId;
	}

	ngOnDestroy(): void {
		this.managmentBed$.unsubscribe();
  	}

}

export class BedManagment {
	sectorId: number;
	sectorDescription: string;
  	specialty:
	{
		specialtyId: number;
		specialtyName: string;
		beds:
			{
				bedId: number;
				bedNumber: string;
				free: boolean;
			}[];
  	}[];
}
