import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { ServiceWorkerModule } from '@angular/service-worker';

import { httpInterceptorProviders } from './http-interceptors';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { environment } from '@environments/environment';

// Módulos nuestros que se cargan al inicio
import { ApiRestModule } from '@api-rest/api-rest.module';
import { AppMaterialModule } from './app.material.module';
import { CoreModule } from '@core/core.module';
import { PacientesModule } from './modules/pacientes/pacientes.module';
import { PresentationModule } from './modules/presentation/presentation.module';

@NgModule({
	declarations: [
		AppComponent,
	],
	imports: [
		BrowserAnimationsModule,
		BrowserModule,
		FormsModule,
		HttpClientModule,
		RouterModule,
		ServiceWorkerModule.register(
			'ngsw-worker.js', {
				enabled: environment.production
			}
		),
		TranslateModule.forRoot({
			loader: {
				provide: TranslateLoader,
				useFactory: (createTranslateLoader),
				deps: [HttpClient]
			}
		}),
		// Módulos nuestros que se cargan al inicio
		ApiRestModule,
		AppMaterialModule,
		CoreModule,
		PacientesModule,
		PresentationModule,
		// Module import order
		// https://angular.io/guide/router#module-import-order
		AppRoutingModule,
	],
	providers: [httpInterceptorProviders],
	bootstrap: [AppComponent]
})
export class AppModule {
}

export function createTranslateLoader(http: HttpClient) {
	return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}
