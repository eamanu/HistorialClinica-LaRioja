import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';

@Component({
	selector: 'app-bar',
	templateUrl: './bar.component.html',
	styleUrls: ['./bar.component.scss']
})
export class BarComponent implements OnInit {
	@Input('position') position = 'static';

	constructor(
		private router: Router
	) { }

	ngOnInit(): void {
	}

}