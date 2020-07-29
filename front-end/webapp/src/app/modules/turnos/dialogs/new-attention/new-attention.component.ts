import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { MasterDataInterface } from '../../../api-rest/api-model';
import { MedicalConsultationMasterdataService } from '../../../api-rest/services/medical-consultation-masterdata.service';
import { MEDICAL_ATTENTION } from '../../constants/descriptions';

@Component({
	selector: 'app-new-attention',
	templateUrl: './new-attention.component.html',
	styleUrls: ['./new-attention.component.scss']
})
export class NewAttentionComponent implements OnInit {

	form: FormGroup;
	public readonly SPONTANEOUS = MEDICAL_ATTENTION.SPONTANEOUS;
	public medicalAttentionTypes: MasterDataInterface<number>[];

	constructor(public dialogRef: MatDialogRef<NewAttentionComponent>,
		private readonly formBuilder: FormBuilder,
		private readonly medicalConsultationMasterdataService: MedicalConsultationMasterdataService) { }


	ngOnInit(): void {

		this.medicalConsultationMasterdataService.getMedicalAttention()
			.subscribe(medicalAttentionTypes => this.medicalAttentionTypes = medicalAttentionTypes);

		this.form = this.formBuilder.group({
			overTurnCount: [null, Validators.min(0)],
			medicalAttentionType: [null, Validators.required]
		});

	}

	onSelectionChanged(): void {
		const medicalAttentionType = this.form.controls.medicalAttentionType.value;

		if (medicalAttentionType.description === MEDICAL_ATTENTION.SPONTANEOUS) {
			this.form.controls.overTurnCount.disable();
		} else {
			this.form.controls.overTurnCount.enable();
		}
	}

	submit() {
		if (this.form.valid) {
			this.dialogRef.close(this.form.value);
		}
	}

	closeDialog() {
		this.dialogRef.close();
	}

}