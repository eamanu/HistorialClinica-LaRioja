import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { PermissionsService } from '@core/services/permissions.service';
import { ERole } from '@api-rest/api-model';
import { anyMatch } from '@core/utils/array.utils';
import { getElementViewFunction } from '@core/utils/directive.utils';

@Directive({
	selector: '[hasRole]'
})
export class HasRoleDirective {

	private showElement: (showElement: boolean) => void;

	constructor(
		templateRef: TemplateRef<any>,
		viewContainer: ViewContainerRef,
		private permissionsService: PermissionsService
	) {
		this.showElement = getElementViewFunction(viewContainer, templateRef);
	}

	@Input()
	set hasRole(allowedRoles: ERole[]) {
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.showElement(anyMatch<ERole>(userRoles, allowedRoles));
		});
	}


}
