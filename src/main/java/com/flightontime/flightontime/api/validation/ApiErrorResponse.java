package com.flightontime.flightontime.api.validation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ApiErrorResponse {

    private Integer status;

    @JsonProperty("error_code")
    private String errorCode;

    private List<String> errors;
}
