package com.example.api.app.service.addressservice;

import com.example.api.app.io.entity.AddressEntity;
import com.example.api.app.io.entity.UserEntity;
import com.example.api.app.io.repositories.AddressRepository;
import com.example.api.app.io.repositories.UserRepository;
import com.example.api.app.shared.AddressDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<AddressDTO> getAddresses(String userId) {
        List<AddressDTO> addressDTOS = new ArrayList<>();

        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            return addressDTOS;
        }

        Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
        addresses.forEach(addressEntity -> addressDTOS.add(modelMapper.map(addressEntity, AddressDTO.class)));

        return addressDTOS;
    }

    @Override
    public AddressDTO getAddress(String addressId) {
        AddressDTO addressDTO = null;
        AddressEntity byAddressId = addressRepository.findByAddressId(addressId);
        if (byAddressId != null) {
            addressDTO = modelMapper.map(byAddressId, AddressDTO.class);
        }
        return addressDTO;
    }
}
