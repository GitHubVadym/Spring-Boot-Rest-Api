package com.example.api.app.ui.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Accessors(chain = true)
@Data
@AllArgsConstructor
public class ErrorMessage {
    private Date timestamp;
    private String message;
}
