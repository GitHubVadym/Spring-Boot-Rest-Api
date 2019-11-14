package com.example.api.app.ui.controller;

import com.example.api.app.service.addressservice.AddressService;
import com.example.api.app.service.userservise.UserService;
import com.example.api.app.shared.UserDto;
import com.example.api.app.ui.model.response.UserRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserControllerTest {
    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;
    @Mock
    private AddressService addressService;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userController = new UserController(userService, addressService);
        MockitoAnnotations.initMocks(this);
        userDto = new UserDto()
                .setFirstName("Vadym")
                .setLastName("Iakobchuk")
                .setEmail("test@test.com")
                .setPassword("1234567");
    }

    @Test
    void getUsers() {
        when(userService.getUserById(anyString())).thenReturn(userDto);
        UserRest returnedUser = userController.getUser("hk34k43k");

        assertNotNull(returnedUser);
        assertEquals("Vadym", returnedUser.getFirstName());
        assertEquals("Iakobchuk", returnedUser.getLastName());
    }
}