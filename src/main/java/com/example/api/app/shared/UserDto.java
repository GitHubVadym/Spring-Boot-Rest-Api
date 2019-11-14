package com.example.api.app.shared;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Accessors(chain = true)
@Data
public class UserDto implements Serializable {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String userId;
    private String password;
    private String encryptedPassword;
    private String emailVerificationToken;
    private Boolean emailVerificationStatus = false;
    private List<AddressDto> addresses;
}
