import React from 'react';
import {
    TextInput,
    ReferenceInput,
    SelectInput,
    Edit,
    SimpleForm,
    required
} from 'react-admin';

const ProfessionalSpecialtyEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" >
            <TextInput source="description" validate={[required()]} />
            <TextInput source="descriptionProfessionRef" validate={[required()]} />
            <TextInput source="sctidCode" validate={[required()]} />
            <ReferenceInput
                source="educationTypeId"
                reference="educationtypes"
                sort={{ field: 'description', order: 'ASC' }}
            >
                <SelectInput optionText="description" optionValue="id"/>
            </ReferenceInput>
        </SimpleForm>
    </Edit>
);

export default ProfessionalSpecialtyEdit;
