package com.example.api.app.service;

import com.example.api.app.exceptions.UserServiceException;
import com.example.api.app.io.entity.AddressEntity;
import com.example.api.app.io.entity.UserEntity;
import com.example.api.app.io.repositories.UserRepository;
import com.example.api.app.service.userservise.UserServiceImpl;
import com.example.api.app.shared.AddressDto;
import com.example.api.app.shared.UserDto;
import com.example.api.app.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Utils utils;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserEntity userEntity;
    private String userId = "1234567";
    private String encryptedPassword = "kjfkbhr3rl24lr4l3";

    @BeforeEach
    void before() {
        userService = new UserServiceImpl(userRepository, utils, bCryptPasswordEncoder);
        MockitoAnnotations.initMocks(this);
        userEntity = new UserEntity().setUserId("1234567")
                .setFirstName("Vadym")
                .setLastName(userId)
                .setEmail("test@test.com")
                .setEncryptedPassword(encryptedPassword)
                .setAddresses(getAddressEntities());
    }

    @Test
    void createUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generateRandomString(anyInt())).thenReturn("823hkh23khk23k");
        when(utils.generateRandomString(anyInt())).thenReturn(userId);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserDto userDto = new UserDto()
                .setFirstName("Vadym")
                .setLastName("Iakobchuk")
                .setEmail("test@test.com")
                .setPassword("1234567")
                .setAddresses(getAddressDTOS());

        UserDto createdUser = userService.createUser(userDto);

        assertNotNull(createdUser);
        assertEquals(userEntity.getFirstName(), createdUser.getFirstName());
        assertEquals(userEntity.getLastName(), createdUser.getLastName());
        assertNotNull(createdUser.getUserId());
        assertEquals(userEntity.getAddresses().size(), createdUser.getAddresses().size());

        verify(utils, times(3)).generateRandomString(30);
        verify(bCryptPasswordEncoder, times(1)).encode("1234567");
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void verifyCreateUserThrowsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        assertThrows(UserServiceException.class,
                () -> userService.createUser(new UserDto().setEmail("test@test.com")));
    }

    @Test
    void verifyGetUser() {

        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDto user = userService.getUser("test@test.com");

        assertNotNull(user);
        assertEquals("Vadym", user.getFirstName());
    }

    @Test
    void verifyGetUserThrowsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,
                () -> userService.getUser("test@test.com"));
    }

    private List<AddressDto> getAddressDTOS() {
        AddressDto addressDto = new AddressDto()
                .setCity("Krakow")
                .setCountry("Poland")
                .setStreet("Florianska 1")
                .setPostalCode("31456");

        AddressDto billingAddressDto = addressDto.setType("billing");
        AddressDto shippingAddressDto = addressDto.setType("shipping");

        List<AddressDto> addressDtoList = new ArrayList<>();
        addressDtoList.add(billingAddressDto);
        addressDtoList.add(shippingAddressDto);

        return addressDtoList;
    }

    private List<AddressEntity> getAddressEntities() {
        List<AddressDto> addressDtoList = getAddressDTOS();
        Type listType = new TypeToken<List<AddressEntity>>() {
        }.getType();
        return new ModelMapper().map(addressDtoList, listType);
    }
}