package com.example.api.app.ui.controller;

import com.example.api.app.exceptions.UserServiceException;
import com.example.api.app.service.addressservice.AddressService;
import com.example.api.app.service.userservise.UserService;
import com.example.api.app.shared.AddressDTO;
import com.example.api.app.shared.UserDTO;
import com.example.api.app.ui.model.request.UserDetailsRequestModel;
import com.example.api.app.ui.model.request.UserDetailsUpdateRequestModel;
import com.example.api.app.ui.model.response.AddressRest;
import com.example.api.app.ui.model.response.OperationModelResponse;
import com.example.api.app.ui.model.response.UserRest;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.api.app.ui.model.response.RequestOperationName.DELETE;
import static com.example.api.app.ui.model.response.RequestOperationStatus.SUCCESS;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users") //http:localhost:8080/users
public class UserController {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserService userService;

    @Autowired
    AddressService addressService;

    Map<String, UserRest> userRestMap = new HashMap<>();

    @GetMapping
    public String getUsers() {
        return "You called for get user " + "page = ";
    }

    @GetMapping(path = "/{userId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(@PathVariable String userId) {
        UserDTO userDto = userService.getUserById(userId);
        UserRest userRest = modelMapper.map(userDto, UserRest.class);
        return userRest;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws UserServiceException {

        UserDTO userDTO = modelMapper.map(userDetails, UserDTO.class);

        UserDTO createdUser = userService.createUser(userDTO);
        UserRest userRest = modelMapper.map(createdUser, UserRest.class);

        return userRest;
    }

    @PutMapping(path = "/{userId}",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest updateUser(@PathVariable String userId,
                               @RequestBody UserDetailsUpdateRequestModel userDetailsUpdate) {

        UserRest userRest = new UserRest();
        UserDTO userDTO = new UserDTO();

        BeanUtils.copyProperties(userDetailsUpdate, userDTO);
        UserDTO updatedUser = userService.updateUser(userId, userDTO);
        BeanUtils.copyProperties(updatedUser, userRest);

        return userRest;
    }

    @DeleteMapping(path = "/{userId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public OperationModelResponse deleteUser(@PathVariable String userId) {
        OperationModelResponse operationModelResponse = new OperationModelResponse();
        operationModelResponse.setOperationName(DELETE.name());
        userService.deleteUser(userId);
        operationModelResponse.setOperationResult(SUCCESS.name());

        return operationModelResponse;
    }

    @GetMapping(path = "/{userId}/addresses",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public CollectionModel<AddressRest> getAddresses(@PathVariable String userId) {
        List<AddressRest> addressesListRestModel = new ArrayList<>();
        List<AddressDTO> addressesDTO = addressService.getAddresses(userId);

        if (addressesDTO != null && !addressesDTO.isEmpty()) {
            Type listType = new TypeToken<List<AddressRest>>() {
            }.getType();
            addressesListRestModel = modelMapper.map(addressesDTO, listType);
        }

        addressesListRestModel.forEach(addressRest -> {
            Link addressLink = linkTo(
                    methodOn(UserController.class).getAddress(userId, addressRest.getAddressId())).withSelfRel();
            addressRest.add(addressLink);

            Link userLink = linkTo(methodOn(UserController.class).getUser(userId)).withRel("user");
            addressRest.add(userLink);
        });

        return new CollectionModel<>(addressesListRestModel);
    }

    @GetMapping(path = "/{userId}/addresses/{addressId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public EntityModel<AddressRest> getAddress(@PathVariable("userId") String userId,
                                               @PathVariable("addressId") String addressId) {
        AddressDTO addressesDTO = addressService.getAddress(addressId);

        AddressRest addressRestModel = modelMapper.map(addressesDTO, AddressRest.class);
        Link addressLink = linkTo(methodOn(UserController.class).getAddress(userId, addressId)).withSelfRel();
        Link addressesLink = linkTo(methodOn(UserController.class).getAddresses(userId)).withRel("addresses");
        Link userLink = linkTo(methodOn(UserController.class).getUser(userId)).withRel("user");

        addressRestModel.add(addressLink);
        addressRestModel.add(userLink);
        addressRestModel.add(addressesLink);

        return new EntityModel<>(addressRestModel);
    }
}
