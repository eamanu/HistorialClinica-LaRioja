export const MOCKS_TURNOS = [
	{
		path: 'solicitar',
		loads: [
			{
				name: 'HealthcareProfessional.searchByName(name: string): ProfessionalDto[]', // dto ya existe
				roles: 'Todo ADMINISTRATIVO sobre la institución',
				path: '/api/institutions/{institutionId}/healthcareprofessional/search-by-name?name=...',
				method: 'GET',
				fetch: [
					{
						id: 66,
						licenceNumber: '19215/1',
						firstName: 'Ricardo',
						lastName: 'Gutierrez',
						identificationNumber: '12345678',
					}
				],
			}
		],
		actions: [
			{
				name: 'Seleccionar profesional',
				navigate: './profesionalId',
			},
		],
	},
	{
		path: 'solicitar/profesionalId',
		loads: [
			{
				name: 'HealthcareProfessional.getOne(healthcareProfessionalId: number): ProfessionalDto',
				roles: 'Todo ADMINISTRATIVO sobre la institución',
				path: '/api/institutions/{institutionId}/healthcareprofessional/{healthcareProfessionalId}',
				method: 'GET',
				fetch: {
					id: 66,
					licenceNumber: '19215/1',
					firstName: 'Ricardo',
					lastName: 'Gutierrez',
					identificationNumber: '12345678',
				}
			},
			{
				name: 'Diary.getDiaries(healthcareProfessionalId: number): DiaryListDto[]',
				roles: 'Todo ADMINISTRATIVO sobre la institución',
				path: '/api/institutions/{institutionId}/medicalConsultations/diary?healthcareProfessionalId=...',
				method: 'GET',
				fetch: [{
					id: 99,
					startDate: '2020-07-01',
					endDate: '2020-08-31',
					doctorsOfficeId: 5,
					appointmentDuration: 5,
					professionalAssignShift: true,
					includeHoliday: false
				}, {

					id: 100,
					startDate: '2020-07-01',
					endDate: '2020-08-31',
					doctorsOfficeId: 5,
					appointmentDuration: 8,
					professionalAssignShift: false,
					includeHoliday: true
				}],
			},
			{
				name: 'DiaryOpeningHours.getMany(diaryIds: number[]): DiaryOpeningHoursDto[]',
				roles: 'Todo ADMINISTRATIVO sobre la institución',
				path: '/api/institutions/{institutionId}/medicalConsultations/diaryOpeningHours?diaryIds=99,100',
				method: 'GET',
				fetch: [
					{
						openingHours: {
							id: 66,
							dayWeekId: 1, // lunes
							from: '08:00',
							to: '12:00'
						},
						overturnCount: 4,
						medicalAttentionTypeId: 0, // programada
					},
					{
						openingHours: {
							id: 67,
							dayWeekId: 1, // lunes
							from: '14:00',
							to: '18:00'
						},
						overturnCount: 0,
						medicalAttentionTypeId: 1, // espontanea
					},
				],
			},
			{
				name: 'Diary.delete(diaryId: number): void',
				roles: 'ADMINISTRADOR AGENDA',
				path: '/api/institutions/{institutionId}/medicalConsultations/diary/{diaryId}',
				method: 'DELETE'
			},
			{
				name: 'Appointments.getList(diaryIds: number): AppointmentListDto[]',
				roles: 'Todo ADMINISTRATIVO sobre la institución',
				path: '/api/institutions/{institutionId}/medicalConsultations/appointments?diaryIds=99,100',
				method: 'GET',
				fetch: [
					{
						id: 10,
						patient: {
							id: 9,
							person: { //BasicPersonalDataDto
								firstName: 'María',
								lastName: 'Gonzalez'
							}
						},
						date: '2020-07-13',
						hour: '07:15',
						overturn: false,
					},
					{
						id: 11,
						patient: {  // nuevo dto
							id: 25,
							person: {//BasicPersonalDataDto
								firstName: 'Jorge',
								lastName: 'Martines'
							}
						},
						date: '2020-07-13',
						hour: '07:30',
						overturn: false,
					}
				],
			}
		],
		actions: [
			{
				name: '[Modal] Nuevo turno en un horario',
				navigate: './nuevo-turno',
			},
			{
				name: '[Modal] Ver turno',
				navigate: './ver-turno',
			},
			{
				name: 'Editar agenda',
				navigate: './agenda/agendaId/editar',
			},
		],
	},
	{
		path: 'solicitar/profesionalId/ver-turno',
		loads: [
			{
				name: 'Appointments.get(appointmentId: number): AppointmentDto',
				roles: 'Todo ADMINISTRATIVO sobre la institución, ESPECIALISTA_MEDICO y PROFESIONAL_DE_SALUD',
				path: '/api/patient/{patientId}/appointment-data/{appointmentId}',
				method: 'GET',
				fetch: {
					date: '2020-07-13',
					hour: '07:30',
					appointmentStateId: 1,
					isOverTurn: false,
					patient: {
						id: 9,
						person: {
							firstName: 'María',
							lastName: 'Gonzalez',
							identificationNumber: '12345678'
						},
						medicalCoverageName: 'OSDE',
						medicalCoverageAffiliateNumber: '3213211'

					}
				},
			},
			{
				name: 'Appointments.changeState(stateId: number): void',
				comments: 'Pendiente'
			}
		]
	},
	{
		path: 'solicitar/profesionalId/nuevo-turno',
		loads: [
			{
				name: 'Patient.getPatientMinimal(identificationTypeId: number, identificationNumber: string, genderId: number): number[]',
				roles: 'Todo ADMINISTRATIVO sobre la institución',
				path: 'ya existe: net.pladema.patient.controller.PatientController#getPatientMinimal',
				method: 'GET',
				fetch: [
					{
						id: 66,
					}
				],
			}
		],
		actions: [
			{
				name: '[Modal] Nuevo turno en un horario (step 2)',
				navigate: '../nuevo-turno2',
			},
		],
	},
	{
		path: 'solicitar/profesionalId/agenda/agendaId/editar',
		loads: [
			{
				name: 'Diary.get(diaryId: number): CompleteDiaryDto',
				roles: 'ADMINISTRADOR AGENDA',
				path: '/api/institutions/{institutionId}/medicalConsultations/diary/{diaryId}',
				method: 'GET',
				fetch:
					{
						id: 99,
						sectorId: 1,
						clinicalSpecialtyId: 3,
						healthcareProfessionalId: 1,
						doctorsOfficeId: 5,
						startDate: '2020-07-01',
						endDate: '2020-08-31',
						appointmentDuration: 5,
						automaticRenewal: false,
						professionalAssignShift: false,
						includeHoliday: false,
						diaryOpeningHours: [
							{
								openingHours: {
									dayWeekId: 1, // lunes
									from: '08:00',
									to: '12:00'
								},
								overturnCount: 4,
								medicalAttentionTypeId: 0, // programada
							},
							{
								openingHours: {
									dayWeekId: 1, // lunes
									from: '14:00',
									to: '18:00'
								},
								overturnCount: 0,
								medicalAttentionTypeId: 1, // espontanea
							},
						]
					}
			},
			{
				name: 'Diary.update(diaryId: number, diaryADto: DiaryADto): void',
				roles: 'ADMINISTRADOR AGENDA',
				path: '/api/institutions/{institutionId}/medicalConsultations/diary/{diaryId}',
				method: 'PUT'
			}
		]
	},
	{
		path: 'solicitar/profesionalId/nuevo-turno2',
		loads: [
			{
				name: 'MedicalCoverage.list(): MedicalCoverageDto[]',
				roles: 'Todo ADMINISTRATIVO sobre la institución',
				path: '/api/medicalcoverage',
				method: 'GET',
				fetch: [
					{
						id: 66,
						nombre: 'Swiss Medical'
					},
					{
						id: 67,
						nombre: 'Osde'
					},
				],
				comments: 'Este endpoint esta pendiente de analisis, se requiere persistencia de obras sociales'
			},
			{
				name: 'Patient.getAppointmentPatientData(patientId: number): AppointmentPatientDto',
				roles: 'Todo ADMINISTRATIVO sobre la institución',
				path: '/api/patient/{patientId}/appointment-patient-data',
				method: 'GET',
				fetch: {
					id: 9,
					person: {//BasicPersonalData interface
						firstName: 'María',
						lastName: 'Gonzalez',
						identificationNumber: '12345678'
					},
					medicalCoverageName: 'OSDE',
					medicalCoverageAffiliateNumber: '3213211'
				},
			},
			{
				name: 'Appointments.create(createAppointmentDto: CreateAppointmentDto): number',
				roles: 'Todo ADMINISTRATIVO sobre la institución. ESPECIALISTA_MEDICO y PROFESIONAL_DE_SALUD para el caso que puedan otorgar turnos',
				path: '/api/institutions/{institutionId}/medicalConsultations/appointments',
				method: 'POST',
				fetch: {
					id: 2,
					patient: {
						id: 25,
						person: {
							firstName: 'Jorge',
							lastName: 'Martinez'
						}
					},
					date: '2020-07-13',
					hour: '07:30',
					isOverTurn: false,
				},
			},
		],
	}
];

