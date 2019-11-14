package com.example.api.app.ui.controller;

import com.example.api.app.service.addressservice.AddressService;
import com.example.api.app.service.userservise.UserService;
import com.example.api.app.shared.AddressDto;
import com.example.api.app.shared.UserDto;
import com.example.api.app.ui.model.request.UserDetailsRequestModel;
import com.example.api.app.ui.model.request.UserDetailsUpdateRequestModel;
import com.example.api.app.ui.model.response.AddressRest;
import com.example.api.app.ui.model.response.OperationModelResponse;
import com.example.api.app.ui.model.response.UserRest;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.api.app.ui.model.response.RequestOperationName.DELETE;
import static com.example.api.app.ui.model.response.RequestOperationStatus.SUCCESS;

@RestController
@RequestMapping("/users")
public class UserController {
    private ModelMapper modelMapper;
    private UserService userService;
    private AddressService addressService;

    @Autowired
    public UserController(UserService userService, AddressService addressService) {
        this.modelMapper = new ModelMapper();
        this.userService = userService;
        this.addressService = addressService;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Bearer JWT token", paramType = "header")
    })
    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "2") int limit) {
        List<UserDto> users = userService.getUsers(page, limit);

        Type listType = new TypeToken<List<UserRest>>() {
        }.getType();

        return new ModelMapper().map(users, listType);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Bearer JWT token", paramType = "header")
    })
    @GetMapping(path = "/{userId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(@PathVariable String userId) {
        UserDto userDto = userService.getUserById(userId);
        return modelMapper.map(userDto, UserRest.class);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        UserDto createdUser = userService.createUser(userDto);

        return modelMapper.map(createdUser, UserRest.class);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Bearer JWT token", paramType = "header")
    })
    @PutMapping(path = "/{userId}",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest updateUser(@PathVariable String userId,
                               @RequestBody UserDetailsUpdateRequestModel userDetailsUpdate) {
        UserRest userRest = new UserRest();
        UserDto userDto = new UserDto();

        BeanUtils.copyProperties(userDetailsUpdate, userDto);
        UserDto updatedUser = userService.updateUser(userId, userDto);
        BeanUtils.copyProperties(updatedUser, userRest);

        return userRest;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Bearer JWT token", paramType = "header")
    })
    @DeleteMapping(path = "/{userId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public OperationModelResponse deleteUser(@PathVariable String userId) {
        OperationModelResponse operationModelResponse = new OperationModelResponse();
        operationModelResponse.setOperationName(DELETE.name());
        userService.deleteUser(userId);
        operationModelResponse.setOperationResult(SUCCESS.name());

        return operationModelResponse;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Bearer JWT token", paramType = "header")
    })
    @GetMapping(path = "/{userId}/addresses",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<AddressRest> getAddresses(@PathVariable String userId) {
        List<AddressRest> restAddresses = new ArrayList<>();
        List<AddressDto> addressesDTO = addressService.getAddresses(userId);

        if (addressesDTO != null && !addressesDTO.isEmpty()) {

            Type listType = new TypeToken<List<AddressRest>>() {
            }.getType();
            restAddresses = modelMapper.map(addressesDTO, listType);
        }

        return restAddresses;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Bearer JWT token", paramType = "header")
    })
    @GetMapping(path = "/{userId}/addresses/{addressId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public AddressRest getAddress(@PathVariable("addressId") String addressId) {
        AddressDto addressesDTO = addressService.getAddress(addressId);
        return modelMapper.map(addressesDTO, AddressRest.class);
    }
}
