package net.pladema.sgx.backoffice.rest;

import net.pladema.sgx.backoffice.validation.BackofficeEntityValidator;

public class BackofficeEntityValidatorAdapter<E, I> implements BackofficeEntityValidator<E, I>{

	@Override
	public void assertCreate(E entity) {
	}

	@Override
	public void assertUpdate(I id, E entity) {
	}

	@Override
	public void assertDelete(I id) {
	}

}
