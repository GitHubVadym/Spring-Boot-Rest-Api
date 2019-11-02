package com.example.api.app.ui.model.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class UserDetailsUpdateRequestModel {
    private String firstName;
    private String lastName;
}
