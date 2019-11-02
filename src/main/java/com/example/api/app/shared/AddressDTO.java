package com.example.api.app.shared;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Accessors(chain = true)
@Data
public class AddressDTO implements Serializable {
    private static final long serialVersionUID = 2942449281719400618L;
    private long id;
    private String addressId;
    private String city;
    private String country;
    private String street;
    private String postalCode;
    private String type;
    private UserDTO userDetails;
}
