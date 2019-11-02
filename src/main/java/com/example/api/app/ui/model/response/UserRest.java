package com.example.api.app.ui.model.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Data
public class UserRest {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private List<AddressRest> addresses;
}