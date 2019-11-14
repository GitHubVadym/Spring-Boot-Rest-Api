package com.example.api.app.shared;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Accessors(chain = true)
@Data
public class AddressDto implements Serializable {
    private static final long serialVersionUID = 8714331148219987493L;
    private long id;
    private String addressId;
    private String city;
    private String country;
    private String street;
    private String postalCode;
    private String type;
    private UserDto userDetails;
}
