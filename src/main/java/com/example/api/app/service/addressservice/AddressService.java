package com.example.api.app.service.addressservice;

import com.example.api.app.shared.AddressDTO;

import java.util.List;

public interface AddressService {
    List<AddressDTO> getAddresses(String userId);

    AddressDTO getAddress(String addressId);
}
