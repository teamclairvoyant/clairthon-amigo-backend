package com.services.dm.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@JsonPropertyOrder(value = {"error_type", "error_message"})
public class RestErrorResponse {

    @JsonProperty("error_type")
    private final HttpStatus type;

    @JsonProperty("error_message")
    private final String message;

}
