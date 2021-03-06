import spanishMessages from '@blackbox-vision/ra-language-spanish';

export default {
    ...spanishMessages,
    sgh: {
        dashboard: {
            title: 'Historia de salud integrada',
            subtitle: 'Bienvenido',
        },
        components: {
            customtoolbar: {
                backButton: 'Volver',
            }
        },
    },
    error: {
        "role-level": {
            institution: {
                required: 'El rol requiere una institución'
            }
        },
        "beds": {
            "existsHospitalization": "La cama no puede ser editada porque tiene un episodio de internación asociado",
            "enabled-available": "Solo las camas habilitadas pueden estar disponibles",
            "available-free": "Solo las camas disponibles pueden estar libres"
        },
        "doctorsoffices": {
            "closingBeforeOpening": "La hora de apertura no puede ser posterior a la hora de cierre",
            "matchingIds": "Esa Especialidad | Sector no pertenece a esa institución"
        },
        "healthcareprofessional":{
          "exists": "Esta persona ya está registrada como profesional en el sistema"
        },
        "healthcare-professional":{
            "specialty-profession-exists": "La profesión y especialidad ya se encuentra asignada",
            "only-one-specialty": "Esta especialidad no puede borrarse dado que es la única que posee el profesional",
            "specialty-profession-not-exists": "La especialidad no existe",
            "affected-to-diary-agenda":"Esta especialidad y profesional están afectados a una agenda en curso"
        },
        "role":{
          "requiresprofessional": "Alguno de los roles asignados requiere que el usuario sea un profesional"
        },
        "user":{
            "exists": "Esta persona ya tiene un usuario en el sistema",
            "hasrole": "El profesional que quiere eliminar tiene un rol asociado"
        },
        "sector":{
          "mandatoryCareType": "El tipo de cuidado es obligatorio para ese tipo de organización de sector",
          "parentOfItself": "Un sector no puede ser padre de sí mismo"
        },
        forbidden: 'No tiene los permisos necesarios',
        "sector-description-inst-unique": "Ya existe un sector con el mismo nombre en la institución"
    },
    resources: {
        beds: {
            name: 'Cama |||| Camas',
            fields: {
                roomId: 'Habitación',
                bedNumber: 'Nro. de cama',
                bedCategoryId: 'Categoría',
                enabled: 'Habilitada',
                available: 'Disponible',
                free: 'Libre',
                internmentepisodes: 'Episodios de internación activos para esta cama'
            },
            createRelated: 'Crear Cama'
        },
        rooms: {
          name: 'Habitación |||| Habitaciones',
          fields: {
              description: 'Nombre',
              type: 'Tipo',
              specialityId: 'Especialidad',
              dischargeDate: 'Fecha de alta',
              sectorId: 'Sector',
              clinicalSpecialtySectorId: 'Especialidad | Sector',
              roomNumber: 'Nro. habitación',
              beds: 'Camas'
          },
            createRelated: 'Crear Habitación'
        },
        cities: {
            name: 'Ciudad |||| Ciudades',
            fields: {
                description: 'Nombre',
                departmentId: 'Partido',
            },
        },
        departments: {
            name: 'Partido |||| Partidos',
            fields: {
                description: 'Nombre',
            },
        },
        doctorsoffices:{
          name: 'Consultorio |||| Consultorios',
          fields:{
              description: 'Nombre',
              openingTime: 'Horario de apertura',
              closingTime: 'Horario de cierre',
              clinicalSpecialtySectorId: 'Especialidad | Sector',
              institutionId: 'Institución'
          },
          createRelated: 'Crear Consultorio',
        },
        addresses: {
            name: 'Dirección |||| Direcciones',
            fields: {
                street: 'Calle',
                number: 'Número',
                floor: 'Piso',
                apartment: 'Nro. dpto.',
                quarter: 'Cuarto',
                postcode: 'Código postal',
                cityId: 'Ciudad',
                departmentId: 'Departamento',
                provinceId: 'Provincia',
                latitude: 'Latitud',
                longitude: 'Longitud',
            },
        }, 
        institutions: {
            name: 'Institución |||| Instituciones',
            fields: {
                name: 'Nombre',
                website: 'Sitio web',
                phone: 'Teléfono',
                sisaCode: 'Código SISA',
                addressId: 'Dirección',
                sectors: 'Sectores',
                dependencyId: 'Dependencia',
            },
        }, 
        sectors: {
            name: 'Sector |||| Sectores',
            fields: {
                institutionId: 'Institución',
                description: 'Nombre',
                clinicalspecialtysectors: 'Especialidad | Sector',
                ageGroupId: 'Grupo de edad',
                sectorTypeId: 'Tipo de sector',
                sectorOrganizationId: 'Organización',
                careTypeId: 'Tipo de cuidado',
                hospitalizationTypeId: 'Permanencia',
                sectorId: 'Sector padre'
            },
            createRelated: 'Crear Sector'
        },

        clinicalspecialties: {
            name: 'Especialidad |||| Especialidades',
            fields: {
                name: 'Nombre',
                description: 'Descripción',
                sctidCode: 'Código SNOMED',
            },
        }, 
        clinicalspecialtysectors: {
            name: 'Especialidad | Sector',
            fields: {
                description: 'Descripción',
                sectorId: 'Sector',
                clinicalSpecialtyId: 'Especialidad',
                rooms: 'Habitaciones',
                doctorsoffices: 'Consultorios',
            },
            createRelated: 'Crear Especialidad | Sector'
        }, 
        professionalspecialties: {
            name: 'Profesión |||| Profesiones',
            fields: {
                description: 'Descripción',
                descriptionProfessionRef: 'Descripción profesión padre',
                sctidCode: 'Código SNOMED',
                educationTypeId: 'Formación',
            }
        }, 
        healthcareprofessionals: {
            name: 'Profesional |||| Profesionales',
            fields: {
                personId: 'Persona',
                licenseNumber: 'Nro. Licencia',
                isMedicalDoctor: 'Es médico?',
                healthcareprofessionalspecialties: 'Profesional | Especialidad',
                professionalSpecialtyId: 'Profesión',
                clinicalSpecialtyId: 'Especialidad'
            },
            createRelated: 'Crear Profesional',
        }, 
        healthcareprofessionalspecialties: {
            name: 'Profesión | Especialidad',
            fields: {
                healthcareProfessionalId: 'Profesional',
                professionalSpecialtyId: 'Profesión',
                clinicalSpecialtyId: 'Especialidad',
                personId: 'Persona',
                description: 'Descripción',
            },
            createRelated: 'Crear Profesión | Especialidad',
        }, 
        users: {
            name: 'Admin |||| Admins',
            fields: {
                username: 'Nombre de usuario',
                personId: 'Persona',
                enable: 'Habilitado',
                lastLogin: 'Último ingreso',
                institutionId: 'Institución',
                roleId: 'Rol'
            },
            fieldGroups: {
                passwordResets: 'Establecer clave de acceso'
            },
            action: {
                reset: 'Visitar link',
                newReset: 'Generar link',
            },
            createRelated: 'Crear Usuario',
        },
        internmentepisodes: {
            name: 'Episodio de internación |||| Episodios de internación',
            fields: {
                entryDate: 'Fecha de entrada'
            }
        },
        person: {
            name: 'Persona |||| Personas',
            fields: {
                firstName: 'Nombre',
                middleNames: 'Segundo nombre',
                lastName: 'Apellido',
                otherLastNames: 'Segundo apellido',
                genderId: 'Género',
                identificationTypeId: 'Tipo de documento',
                identificationNumber: 'Nº de documento',
                birthDate: 'Fecha de nacimiento'
            },
            tabs: {
                details: 'Datos personales',
                users: 'Usuario'
            },
        }
    },
};
