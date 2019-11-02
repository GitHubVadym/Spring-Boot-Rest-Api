package com.example.api.app.ui.model.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class AddressRequestModel {
    private String city;
    private String country;
    private String street;
    private String postalCode;
    private String type;
}
