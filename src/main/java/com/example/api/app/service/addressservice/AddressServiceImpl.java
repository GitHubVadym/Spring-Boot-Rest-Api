package com.example.api.app.service.addressservice;

import com.example.api.app.io.entity.AddressEntity;
import com.example.api.app.io.entity.UserEntity;
import com.example.api.app.io.repositories.AddressRepository;
import com.example.api.app.io.repositories.UserRepository;
import com.example.api.app.shared.AddressDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<AddressDto> getAddresses(String userId) {
        List<AddressDto> addressDtos = new ArrayList<>();

        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            return addressDtos;
        }

        Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
        addresses.forEach(addressEntity -> addressDtos.add(modelMapper.map(addressEntity, AddressDto.class)));

        return addressDtos;
    }

    @Override
    public AddressDto getAddress(String addressId) {
        AddressDto addressDto = null;
        AddressEntity byAddressId = addressRepository.findByAddressId(addressId);
        if (byAddressId != null) {
            addressDto = modelMapper.map(byAddressId, AddressDto.class);
        }
        return addressDto;
    }
}
