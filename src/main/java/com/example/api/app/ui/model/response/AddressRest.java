package com.example.api.app.ui.model.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class AddressRest {
    private String addressId;
    private String city;
    private String country;
    private String street;
    private String postalCode;
    private String type;
}
