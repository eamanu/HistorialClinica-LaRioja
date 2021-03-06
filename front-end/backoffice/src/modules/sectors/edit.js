import React from 'react';
import {
    Datagrid,
    DeleteButton,
    Edit,
    EditButton,
    FormDataConsumer,
    ReferenceField,
    ReferenceInput,
    ReferenceManyField,
    required,
    SelectInput,
    SimpleForm,
    TextField,
    TextInput,
} from 'react-admin';
import CreateRelatedButton from '../components/CreateRelatedButton';
import SectionTitle from '../components/SectionTitle';
import SgxSelectInput from "../../sgxSelectInput/SgxSelectInput";
import CustomToolbar from "../../modules/components/CustomToolbar";
import SgxDateField from "../../dateComponents/sgxDateField";

const INTERNACION = 2;

const SectorType = (sourceId) => {
    return (
        <ReferenceInput
            {...sourceId}
            reference="sectortypes"
            perPage={100}
            sort={{ field: 'description', order: 'ASC' }}
        >
            <SelectInput optionText="description" optionValue="id" />
        </ReferenceInput>);

};

const HospitalizationField = ({formData, ...rest}) => {
    return formData.sectorTypeId !== INTERNACION ? null : (
        <ReferenceInput {...rest} sort={{ field: 'description', order: 'ASC' }}>
            <SelectInput optionText="description" optionValue="id" />
        </ReferenceInput>
    )
}

const Sector = ({ formData, ...rest }) => {
    return (
        <ReferenceInput
            {...rest}
            reference="sectors"
            perPage={100}
            sort={{ field: 'description', order: 'ASC' }}
            filter={{institutionId: formData.institutionId}}
        >
            <SelectInput optionText="description" optionValue="id" />
        </ReferenceInput>);
};

const SectorEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true}/>}>
            <TextInput source="description" validate={[required()]} />

            <SgxSelectInput source="institutionId" element="institutions" optionText="name" alwaysOn allowEmpty={false}/>

            {/*Parent Sector*/}
            <FormDataConsumer>
                {formDataProps => (<Sector {...formDataProps} source="sectorId"/>)}
            </FormDataConsumer>
            {/*Sector Type*/}
            <SectorType source="sectorTypeId"/>
            {/*Age Groups*/}
            <FormDataConsumer>
                {formDataProps => ( <HospitalizationField {...formDataProps} reference="agegroups" source="ageGroupId"/>)}
            </FormDataConsumer>
            {/*Sector Organizations*/}
            <FormDataConsumer >
                {formDataProps => ( <HospitalizationField {...formDataProps} reference="sectororganizations" source="sectorOrganizationId" />)}
            </FormDataConsumer>
            {/*Care Type*/}
            <FormDataConsumer >
                {formDataProps => ( <HospitalizationField {...formDataProps} reference="caretypes" source="careTypeId" />)}
            </FormDataConsumer>
            {/*Hospitalization Type*/}
            <FormDataConsumer>
                {formDataProps => ( <HospitalizationField {...formDataProps} reference="hospitalizationtypes" source="hospitalizationTypeId"/>)}
            </FormDataConsumer>

            <SectionTitle label="resources.sectors.fields.clinicalspecialtysectors"/>
            <CreateRelatedButton
                reference="clinicalspecialtysectors"
                refFieldName="sectorId"
                label="resources.clinicalspecialtysectors.createRelated"
            />
            {/*TODO: Aislar esto en un componente. Tambi??n se usa en show.js*/}
            <ReferenceManyField
                addLabel={false}
                reference="clinicalspecialtysectors"
                target="sectorId"
                sort={{ field: 'description', order: 'DESC' }}
            >
                <Datagrid rowClick="show">
                    <TextField source="description" />
                    <ReferenceField source="clinicalSpecialtyId" reference="clinicalspecialties">
                        <TextField source="name" />
                    </ReferenceField>
                    <DeleteButton />
                </Datagrid>
            </ReferenceManyField>

            <SectionTitle label="resources.clinicalspecialtysectors.fields.rooms"/>
            <CreateRelatedButton
                reference="rooms"
                refFieldName="sectorId"
                label="resources.rooms.createRelated"
            />
            <ReferenceManyField
                addLabel={false}
                reference="rooms"
                target="sectorId"
                sort={{ field: 'description', order: 'DESC' }}
            >
                <Datagrid rowClick="show">
                    <TextField source="roomNumber" />
                    <TextField source="description"/>
                    <TextField source="type" />
                    <SgxDateField source="dischargeDate" />
                    <EditButton />
                </Datagrid>
            </ReferenceManyField>
        </SimpleForm>
    </Edit>
);

export default SectorEdit;
