package net.pladema.address.controller.service;

import net.pladema.address.controller.dto.AddressDto;
import net.pladema.address.controller.mapper.AddressMapper;
import net.pladema.address.repository.entity.Address;
import net.pladema.address.repository.entity.City;
import net.pladema.address.repository.entity.Province;
import net.pladema.address.service.AddressMasterDataService;
import net.pladema.address.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AddressExternalServiceImpl implements AddressExternalService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final AddressMapper addressMapper;

    private final AddressService addressService;

    private final AddressMasterDataService addressMasterDataService;

    public AddressExternalServiceImpl(AddressMapper addressMapper, AddressService addressService, AddressMasterDataService addressMasterDataService) {
        super();
        this.addressService = addressService;
        this.addressMapper = addressMapper;
        this.addressMasterDataService = addressMasterDataService;
        LOG.debug("{}", "created service");
    }

    @Override
    public AddressDto addAddress(AddressDto addressDto) {
        LOG.debug("Input parameters -> {}", addressDto);
        Address newAddress = addressMapper.fromAddressDto(addressDto);
        LOG.debug("Mapped from AddressDto result -> {}", newAddress);
        newAddress = addressService.addAddress(newAddress);
        AddressDto result = addressMapper.toAddressDto(newAddress);
        LOG.debug("Mapped fromAddress result -> {}", result);
        LOG.debug("Output -> {}", result);
        return result;
    }
}
