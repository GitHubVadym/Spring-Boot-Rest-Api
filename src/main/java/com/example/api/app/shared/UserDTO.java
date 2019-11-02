package com.example.api.app.shared;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Accessors(chain = true)
@Data
public class UserDTO implements Serializable {
    private static final long serialVersionUID = -3292703684731929103L;
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String userId;
    private String password;
    private String encryptedPassword;
    private String emailVerificationToken;
    private Boolean emailVerificationStatus = false;
    private List<AddressDTO> addresses;
}
