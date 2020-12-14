import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { mockRouters } from "@presentation/utils/mock-routers.utils";
import { MOCKS_GUARDIA } from "./constants/mock-routers";
import { HomeComponent } from "./routes/home/home.component";
import { AppFeature, ERole } from "@api-rest/api-model";
import { RoleGuard } from "@core/guards/RoleGuard";
import { FeatureFlagGuard } from "@core/guards/FeatureFlagGuard";


const routes: Routes = [{
	path: '',
	children: [
		{
			path: '',
			component: HomeComponent
		},
		...mockRouters(MOCKS_GUARDIA)
	],
	canActivate: [RoleGuard, FeatureFlagGuard],
	data: {
		allowedRoles: [ERole.ADMINISTRADOR, ERole.ADMINISTRATIVO, ERole.ENFERMERO, ERole.ESPECIALISTA_MEDICO,
			ERole.PROFESIONAL_DE_SALUD, ERole.ROOT],
		featureFlag: AppFeature.HABILITAR_MODULO_GUARDIA
	}
}];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class GuardiaRoutingModule {
}