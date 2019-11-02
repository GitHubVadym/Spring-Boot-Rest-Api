package com.example.api.app.ui.model.response;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.hateoas.RepresentationModel;

@Accessors(chain = true)
@Data
public class AddressRest extends RepresentationModel {
    private String addressId;
    private String city;
    private String country;
    private String street;
    private String postalCode;
    private String type;
}
