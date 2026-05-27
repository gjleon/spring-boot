package com.gjleon.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AnimePostRequest {
    @NotBlank(message = "The field 'name' is required")
    private String name;
}
