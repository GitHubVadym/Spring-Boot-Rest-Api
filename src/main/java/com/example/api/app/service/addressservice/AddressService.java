package com.example.api.app.service.addressservice;

import com.example.api.app.shared.AddressDto;

import java.util.List;

public interface AddressService {
    List<AddressDto> getAddresses(String userId);

    AddressDto getAddress(String addressId);
}
